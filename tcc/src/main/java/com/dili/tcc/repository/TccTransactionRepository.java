package com.dili.tcc.repository;

import com.dili.tcc.bean.TccTransaction;

/**
 * @Auther: miaoguoxin
 * @Date: 2020/7/29 16:26
 * @Description:
 */
public interface TccTransactionRepository {

    int create(TccTransaction transaction);
}
