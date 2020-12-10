package com.dili.card.service.impl;

import com.dili.card.entity.tcc.UserCardDo;
import com.dili.card.rpc.TestTccRpc;
import com.dili.card.service.ITestSerivce;
import com.dili.ss.domain.BaseOutput;
import io.seata.spring.annotation.GlobalTransactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @Auther: miaoguoxin
 * @Date: 2020/11/30 11:39
 * @Description:
 */
@Service
public class TestSerivce implements ITestSerivce {
    @Autowired
    private TestTccRpc testTccRpc;

    @Override
    @GlobalTransactional
    @Transactional
    public void test(UserCardDo cardDo) {
        BaseOutput<Long> bout = testTccRpc.testConfirm(cardDo);
        if (!bout.isSuccess()){
            throw new RuntimeException("事务错误");
        }

    }
}
