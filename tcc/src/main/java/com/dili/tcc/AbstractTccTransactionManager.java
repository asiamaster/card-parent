package com.dili.tcc;


import com.dili.tcc.common.TccStatus;
import com.dili.tcc.common.TransactionId;
import com.dili.tcc.core.TccContext;
import com.dili.tcc.core.TccContextHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import java.util.UUID;

/**
 * @Auther: miaoguoxin
 * @Date: 2020/7/3 11:31
 * @Description:
 */
public abstract class AbstractTccTransactionManager<R, T> {
    private static Logger LOGGER = LoggerFactory.getLogger(AbstractTccTransactionManager.class);

    private static final int MAX_RETRY = 2;

    private static final long RETRY_INTERVAL_MS = 5000;
    @Autowired
    private PlatformTransactionManager transactionManager;


    public final R doTcc(T requestDto) {
        try {
            this.initContext();
            this.doPrepare(requestDto);
            return this.doConfirm(requestDto);
        } finally {
            LOGGER.info("tcc执行结束，开始clean up");
            TccContextHolder.remove();
        }
    }


    /**
     * try阶段
     * @author miaoguoxin
     * @date 2020/7/3
     */
    protected abstract void prepare(T requestDto);

    /**
     * 提交
     * @author miaoguoxin
     * @date 2020/7/3
     */
    protected abstract R confirm(T requestDto);

    /**
     * cancel阶段(try阶段反向操作)
     * @author miaoguoxin
     * @date 2020/7/3
     */
    protected abstract void cancel(T requestDto);


    private void doPrepare(T requestDto) {
        DefaultTransactionDefinition prepareDef = new DefaultTransactionDefinition();
        prepareDef.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
        TransactionStatus prepareDefStatus = transactionManager.getTransaction(prepareDef);
        TccContext tccContext = TccContextHolder.get();
        try {
            tccContext.changeStatus(TccStatus.PRE);
            this.prepare(requestDto);
            LOGGER.info("开始执行try阶段");
            transactionManager.commit(prepareDefStatus);
        } catch (Exception e) {
            //这里考虑到try阶段通常是insert操作，应尽早结束事务，
            // 否则后续cancel操作如果重试多次，可能导致后续新业务失败
            transactionManager.rollback(prepareDefStatus);

            tccContext.changeStatus(TccStatus.CANCEL);
            //先清理上个阶段的
            tccContext.clearPrevious();
            this.doCancelAndRetry(requestDto, tccContext);
            throw e;
        }
    }

    /**
     * confirm阶段失败进行重试操作
     * @author miaoguoxin
     * @date 2020/7/14
     */
    private R doConfirm(T requestDto) {
        DefaultTransactionDefinition def = new DefaultTransactionDefinition();
        def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
        TransactionStatus status = transactionManager.getTransaction(def);
        TccContext tccContext = TccContextHolder.get();
        try {
            tccContext.changeStatus(TccStatus.CONFIRM);
            R result = this.confirm(requestDto);
            LOGGER.info("开始执行confirm阶段");
            transactionManager.commit(status);
            return result;
        } catch (Throwable e) {
            //先清理上个阶段的
            // tccContext.clearPrevious();
            this.doConfirmAndRetry(requestDto, tccContext);

            //进入confirm说明try阶段已经成功，资源已预添加，
            //confirm通常是update操作，因此需要等重试操作完成后再rollback
            //否则如果先rollback，可能导致重试confirm变成无效操作
            transactionManager.rollback(status);
            throw e;
        }
    }

    private void doCancelAndRetry(T requestDto, TccContext tccContext) {
        DefaultTransactionDefinition def = new DefaultTransactionDefinition();
        def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
        TransactionStatus status = transactionManager.getTransaction(def);
        try {
            LOGGER.info("try阶段发生异常，开始执行cancel阶段回滚");
            this.cancel(requestDto);
            transactionManager.commit(status);
        } catch (Throwable e) {
            transactionManager.rollback(status);
            if (tccContext.getRetryNum() < MAX_RETRY) {
                try {
                    Thread.sleep(RETRY_INTERVAL_MS);
                } catch (InterruptedException ex) {
                    Thread.currentThread().interrupt();
                    ex.printStackTrace();
                }
                tccContext.incRetryNum();
                LOGGER.info("cancel阶段异常，开始进行重试，当前第【{}】次重试", tccContext.getRetryNum());
                this.doCancelAndRetry(requestDto, tccContext);
            } else {
                throw e;
            }
        }
    }

    private R doConfirmAndRetry(T requestDto, TccContext tccContext) {
        R result;
        try {
            result = this.confirm(requestDto);
        } catch (Throwable e) {
            if (tccContext.getRetryNum() < MAX_RETRY) {
                try {
                    Thread.sleep(RETRY_INTERVAL_MS);
                } catch (InterruptedException ex) {
                    Thread.currentThread().interrupt();
                    ex.printStackTrace();
                }
                tccContext.incRetryNum();
                LOGGER.info("confirm阶段异常，开始进行重试，当前第【{}】次重试", tccContext.getRetryNum());
                result = this.doConfirmAndRetry(requestDto, tccContext);
            } else {
                throw e;
            }
        }
        return result;
    }

    private void initContext() {
        TccContext tccContext = new TccContext();
        TransactionId transactionId = new TransactionId(UUID.randomUUID().toString());
        tccContext.setTransactionId(transactionId);
        TccContextHolder.set(tccContext);
    }
}
