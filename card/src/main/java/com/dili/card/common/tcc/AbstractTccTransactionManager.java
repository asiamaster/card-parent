package com.dili.card.common.tcc;


import com.dili.card.exception.CardAppBizException;
import com.dili.ss.constant.ResultCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

/**
 * @Auther: miaoguoxin
 * @Date: 2020/7/3 11:31
 * @Description:
 */
public abstract class AbstractTccTransactionManager<R, T> {
    @Autowired
    private PlatformTransactionManager transactionManager;

    public void doTcc(T requestDto) {
        try {
            this.doPrepare(requestDto);
            this.doConfirm(requestDto);
        } finally {

        }
    }

    private void doConfirm(T requestDto) {
        DefaultTransactionDefinition def = new DefaultTransactionDefinition();
        def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
        TransactionStatus status = transactionManager.getTransaction(def);
        try {
            this.confirm(requestDto);
            transactionManager.commit(status);
        } catch (CardAppBizException e) {
            //TODO 处理系统级别异常
            if (e.getCode().equals(ResultCode.APP_ERROR)) {
                this.confirm(requestDto);
            }
            transactionManager.rollback(status);
            throw new CardAppBizException(e.getCode(),e.getMessage());
        } catch (Exception e){
            //TODO 需要重新提交本地事务和请求rpc
            this.confirm(requestDto);
            transactionManager.rollback(status);
            throw e;
        }
    }

    private void doPrepare(T requestDto) {
        DefaultTransactionDefinition prepareDef = new DefaultTransactionDefinition();
        prepareDef.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
        TransactionStatus prepareDefStatus = transactionManager.getTransaction(prepareDef);
        try {
            this.prepare(requestDto);
            transactionManager.commit(prepareDefStatus);
        } catch (CardAppBizException e) {
            transactionManager.rollback(prepareDefStatus);
            throw new CardAppBizException(e.getCode(),e.getMessage());
        } catch (Exception e){
            this.cancel(requestDto);
            transactionManager.rollback(prepareDefStatus);
            throw e;
        }
    }

    /**
     * try阶段
     * @author miaoguoxin
     * @date 2020/7/3
     */
    public abstract R prepare(T requestDto);

    /**
     * 提交
     * @author miaoguoxin
     * @date 2020/7/3
     */
    public abstract R confirm(T requestDto);

    public abstract void localCommit();

    /**
     * 取消try阶段
     * @author miaoguoxin
     * @date 2020/7/3
     */
    public abstract R cancel(T requestDto);

    /**
     *  清理资源
     * @author miaoguoxin
     * @date 2020/7/3
     */
    public  void cleanUp(){

    }
}
