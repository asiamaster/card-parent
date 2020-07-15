package com.dili.tcc.core;

import com.dili.tcc.common.TccRemoteInfo;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 *
 * @Auther: miaoguoxin
 * @Date: 2020/7/7 14:52
 */
public class MemoryTransactionRepository implements ITransactionRepository {
    private static final Map<String, TccRemoteInfo> id2Map = new ConcurrentHashMap<>();

    @Override
    public void putRemoteInfo(String txId, TccRemoteInfo remoteInfo) {
        id2Map.put(txId, remoteInfo);
    }

    @Override
    public void removeRemoteInfo(String txId) {
        id2Map.remove(txId);
    }
}
