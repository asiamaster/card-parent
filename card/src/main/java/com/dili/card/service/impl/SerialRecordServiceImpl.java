package com.dili.card.service.impl;

import com.dili.card.dao.IBusinessRecordDao;
import com.dili.card.dto.CardRequestDto;
import com.dili.card.entity.BusinessRecordDo;
import com.dili.card.rpc.resolver.CustomerRpcResolver;
import com.dili.card.rpc.resolver.UidRpcResovler;
import com.dili.card.service.ISerialRecordService;
import com.dili.card.type.BizNoType;
import com.dili.card.type.OperateState;
import com.dili.customer.sdk.domain.Customer;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;

/**
 * 操作流水service实现类
 * @author xuliang
 */
@Service
public class SerialRecordServiceImpl implements ISerialRecordService {

    @Resource
    private UidRpcResovler uidRpcResovler;
    @Resource
    private CustomerRpcResolver customerRpcResolver;
    @Resource
    private IBusinessRecordDao businessRecordDao;

    @Override
    public void buildCommonInfo(CardRequestDto cardParam, BusinessRecordDo businessRecord) {
        //编号、卡号、账户id
        businessRecord.setSerialNo(uidRpcResovler.bizNumber(BizNoType.OPERATE_SERIAL_NO.getCode()));
        businessRecord.setAccountId(cardParam.getAccountId());
        businessRecord.setCardNo(cardParam.getCardNo());
        //客户信息
        Customer customer = customerRpcResolver.getWithNotNull(cardParam.getCustomerId(), cardParam.getFirmId());
        businessRecord.setCustomerId(customer.getId());
        businessRecord.setCustomerNo(customer.getCode());
        businessRecord.setCustomerName(customer.getName());
        //账务周期
        //TODO 待接入账务周期
        //操作员信息
        businessRecord.setOperatorId(cardParam.getOpId());
        businessRecord.setOperatorNo(cardParam.getOpNo());
        businessRecord.setOperatorName(cardParam.getOpName());
        businessRecord.setFirmId(cardParam.getFirmId());
        //时间、默认状态等数据
        LocalDateTime localDateTime = LocalDateTime.now();
        businessRecord.setState(OperateState.PROCESSING.getCode());
        businessRecord.setOperateTime(localDateTime);
        businessRecord.setModifyTime(localDateTime);
        businessRecord.setVersion(1);
    }

    @Override
    public void saveBusinessRecord(BusinessRecordDo businessRecord) {
        businessRecordDao.save(businessRecord);
    }
}
