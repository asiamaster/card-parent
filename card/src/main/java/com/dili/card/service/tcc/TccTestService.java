package com.dili.card.service.tcc;

import com.dili.card.dao.IApplyRecordDao;
import com.dili.card.entity.ApplyRecordDo;
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
    private IApplyRecordDao applyRecordDao;
    @Autowired
    private TestTccRpc testTccRpc;

    @Override
    public void prepare(UserCardDo requestDto) {
        ApplyRecordDo applyRecordDo = new ApplyRecordDo();
        applyRecordDo.setId(1000L);
        applyRecordDo.setApplyUserCode("123Test");
        applyRecordDo.setModifyTime(LocalDateTime.now());
        applyRecordDao.update(applyRecordDo);
        BaseOutput<Long> output = testTccRpc.testTry(requestDto);
        if (!output.isSuccess()){
            throw new RuntimeException("测试try阶段失败");
        }
        requestDto.setId(output.getData());
    }

    @Override
    public Long confirm(UserCardDo requestDto) {
        ApplyRecordDo applyRecordDo = new ApplyRecordDo();
        applyRecordDo.setId(1000L);
        applyRecordDo.setApplyUserCode("222Test");
        applyRecordDo.setModifyTime(LocalDateTime.now());
        applyRecordDao.update(applyRecordDo);

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
