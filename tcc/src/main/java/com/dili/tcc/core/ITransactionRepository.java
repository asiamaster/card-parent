package com.dili.tcc.core;

import com.dili.tcc.common.TccRemoteInfo;

/**
 *
 * @Auther: miaoguoxin
 * @Date: 2020/7/7 14:21
 */
public interface ITransactionRepository {

    /**
    *
    * @author miaoguoxin
    * @date 2020/7/7
    */
    void putRemoteInfo(String txId, TccRemoteInfo remoteInfo);

    /**
    *
    * @author miaoguoxin
    * @date 2020/7/13
    */
    void removeRemoteInfo(String txId);
}
