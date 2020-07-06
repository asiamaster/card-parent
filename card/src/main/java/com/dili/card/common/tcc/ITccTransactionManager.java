package com.dili.card.common.tcc;

/**
 * @Auther: miaoguoxin
 * @Date: 2020/7/3 11:29
 * @Description:
 */
public interface ITccTransactionManager<T,R> {

    /**
    * try阶段
    * @author miaoguoxin
    * @date 2020/7/3
    */
    R prepare(T requestDto);

    /**
    * 提交
    * @author miaoguoxin
    * @date 2020/7/3
    */
    R confirm(T requestDto);

    /**
    * 取消try阶段
    * @author miaoguoxin
    * @date 2020/7/3
    */
    R cancel(T requestDto);

    /**
    *  清理资源
    * @author miaoguoxin
    * @date 2020/7/3
    */
    void cleanUp();
}
