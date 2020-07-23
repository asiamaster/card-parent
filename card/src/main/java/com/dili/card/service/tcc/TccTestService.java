package com.dili.card.service.tcc;

import com.dili.card.dao.IStorageOutDao;
import com.dili.card.entity.CardStorageOut;
import com.dili.card.entity.tcc.UserCardDo;
import com.dili.card.rpc.TestTccRpc;
import com.dili.ss.domain.BaseOutput;
import com.dili.tcc.AbstractTccTransactionManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * @Auther: miaoguoxin
 * @Date: 2020/7/15 10:11
 * @Description:
 */
@Service
public class TccTestService extends AbstractTccTransactionManager<Long, UserCardDo> {

    @Autowired
    private IStorageOutDao storageOutDao;
    @Autowired
    private TestTccRpc testTccRpc;

    @Override
    public void prepare(UserCardDo requestDto) {
        CardStorageOut cardStorageOut = new CardStorageOut();
        cardStorageOut.setId(1000L);
        cardStorageOut.setApplyUserCode("123Test");
        cardStorageOut.setModifyTime(LocalDateTime.now());
        storageOutDao.update(cardStorageOut);
        BaseOutput<Long> output = testTccRpc.testTry(requestDto);
        if (!output.isSuccess()){
            throw new RuntimeException("测试try阶段失败");
        }
        requestDto.setId(output.getData());
    }

    @Override
    public Long confirm(UserCardDo requestDto) {
        CardStorageOut cardStorageOut = new CardStorageOut();
        cardStorageOut.setId(1000L);
        cardStorageOut.setApplyUserCode("222Test");
        cardStorageOut.setModifyTime(LocalDateTime.now());
        storageOutDao.update(cardStorageOut);

        requestDto.setDeviceId("test_confirm_11111");
        BaseOutput<Long> output = testTccRpc.testConfirm(requestDto);
        throw new RuntimeException("测试本地confirm失败");
       // return requestDto.getId();
    }

    @Override
    public void cancel(UserCardDo requestDto) {
        BaseOutput<String> output = testTccRpc.testCancel(requestDto);
        if (!output.isSuccess()){
            throw new RuntimeException("测试cancel阶段失败");
        }
    }
}
