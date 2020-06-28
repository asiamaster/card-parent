package com.dili.card.service.impl;

import com.alibaba.fastjson.JSON;
import com.dili.card.dao.IBusinessRecordDao;
import com.dili.card.dto.CardRequestDto;
import com.dili.card.dto.SerialDto;
import com.dili.card.entity.AccountCycleDo;
import com.dili.card.entity.BusinessRecordDo;
import com.dili.card.entity.SerialRecordDo;
import com.dili.card.exception.CardAppBizException;
import com.dili.card.rpc.resolver.CustomerRpcResolver;
import com.dili.card.rpc.resolver.SerialRecordRpcResolver;
import com.dili.card.rpc.resolver.UidRpcResovler;
import com.dili.card.service.IAccountCycleService;
import com.dili.card.service.ISerialService;
import com.dili.card.type.BizNoType;
import com.dili.card.type.OperateState;
import com.dili.card.type.OperateType;
import com.dili.card.util.DateUtil;
import com.dili.customer.sdk.domain.Customer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 操作流水service实现类
 * @author xuliang
 */
@Service
public class SerialServiceImpl implements ISerialService {
    private static final Logger LOGGER = LoggerFactory.getLogger(SerialServiceImpl.class);

    @Resource
    private UidRpcResovler uidRpcResovler;
    @Resource
    private CustomerRpcResolver customerRpcResolver;
    @Resource
    private IBusinessRecordDao businessRecordDao;
    @Resource
    private IAccountCycleService accountCycleService;
    @Resource
    private SerialRecordRpcResolver serialRecordRpcResolver;

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
        AccountCycleDo accountCycle = accountCycleService.findActiveCycleByUserId(cardParam.getOpId());
        businessRecord.setCycleNo(accountCycle.getCycleNo());
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

    @Transactional
    @Override
    public void saveBusinessRecord(BusinessRecordDo businessRecord) {
        businessRecordDao.save(businessRecord);
    }

    @Override
    public void copyCommonFields(SerialRecordDo serialRecord, BusinessRecordDo businessRecord) {
        serialRecord.setSerialNo(businessRecord.getSerialNo());
        serialRecord.setAccountId(businessRecord.getAccountId());
        serialRecord.setCardNo(businessRecord.getCardNo());
        serialRecord.setCustomerId(businessRecord.getCustomerId());
        serialRecord.setCustomerNo(businessRecord.getCustomerNo());
        serialRecord.setCustomerName(businessRecord.getCustomerName());
        serialRecord.setOperatorId(businessRecord.getOperatorId());
        serialRecord.setOperatorNo(businessRecord.getOperatorNo());
        serialRecord.setOperatorName(businessRecord.getOperatorName());
        serialRecord.setFirmId(businessRecord.getFirmId());
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
    @Override
    public void handleFailure(SerialDto serialDto) {
        try {
            BusinessRecordDo businessRecord = new BusinessRecordDo();
            businessRecord.setSerialNo(serialDto.getSerialNo());
            businessRecord.setState(OperateState.FAILURE.getCode());
            businessRecordDao.doFailureUpdate(businessRecord);
        } catch (Exception e) {
            LOGGER.error("", JSON.toJSONString(serialDto));//记录数据方便后期处理
            throw new CardAppBizException("修改办理状态失败");
        }
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
    @Override
    public void handleSuccess(SerialDto serialDto) {
        try {
            //修改状态
            BusinessRecordDo businessRecord = new BusinessRecordDo();
            businessRecord.setSerialNo(serialDto.getSerialNo());
            businessRecord.setState(OperateState.SUCCESS.getCode());
            businessRecord.setStartBalance(serialDto.getStartBalance());
            businessRecord.setEndBalance(serialDto.getEndBalance());
            businessRecordDao.doSuccessUpdate(businessRecord);
            //保存流水
            serialRecordRpcResolver.batchSave(serialDto);
        } catch (Exception e) {
            LOGGER.error("", JSON.toJSONString(serialDto));//记录数据方便后期处理
            throw new CardAppBizException("修改办理状态失败");
        }
    }

    @Override
    public List<BusinessRecordDo> cycleReprintList(SerialDto serialDto) {
        AccountCycleDo accountCycle = accountCycleService.findActiveCycleByUserId(serialDto.getOperatorId());
        serialDto.setCycleNo(accountCycle.getCycleNo());
        serialDto.setSort("operate_time");
        serialDto.setOrder("desc");
        serialDto.setState(OperateState.SUCCESS.getCode());
        serialDto.setOperateTypeList(OperateType.createReprintList());
        return businessRecordDao.list(serialDto);
    }

    @Override
    public List<BusinessRecordDo> todayChargeList(SerialDto serialDto) {
        serialDto.setOperateTimeStart(DateUtil.formatDate("yyyy-MM-dd") + " 00:00:00");
        serialDto.setOperateTimeEnd(DateUtil.formatDate("yyyy-MM-dd") + " 23:59:59");
        serialDto.setSort("operate_time");
        serialDto.setOrder("desc");
        return businessRecordDao.list(serialDto);
    }
}
