package com.dili.tcc.repository;

import com.dili.tcc.bean.TccTransaction;

/**
 * @Auther: miaoguoxin
 * @Date: 2020/7/29 16:27
 * @Description:
 */
public class RedisTccTransactionRepository implements TccTransactionRepository {

    @Override
    public int create(TccTransaction transaction) {
        return 1;
    }
}
