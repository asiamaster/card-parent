package com.dili.card.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.dili.card.common.constant.Constant;
import com.dili.card.config.SerialMQConfig;
import com.dili.card.dao.IBusinessRecordDao;
import com.dili.card.dto.AccountWithAssociationResponseDto;
import com.dili.card.dto.BusinessRecordResponseDto;
import com.dili.card.dto.CardRequestDto;
import com.dili.card.dto.SerialDto;
import com.dili.card.dto.SerialQueryDto;
import com.dili.card.dto.UserAccountCardQuery;
import com.dili.card.dto.UserAccountCardResponseDto;
import com.dili.card.dto.pay.FeeItemDto;
import com.dili.card.dto.pay.TradeResponseDto;
import com.dili.card.entity.AccountCycleDo;
import com.dili.card.entity.BusinessRecordDo;
import com.dili.card.entity.SerialRecordDo;
import com.dili.card.exception.CardAppBizException;
import com.dili.card.rpc.resolver.SerialRecordRpcResolver;
import com.dili.card.rpc.resolver.UidRpcResovler;
import com.dili.card.service.IAccountCycleService;
import com.dili.card.service.IAccountQueryService;
import com.dili.card.service.ICustomerService;
import com.dili.card.service.ISerialService;
import com.dili.card.service.serial.IAccountSerialFilter;
import com.dili.card.service.serial.IBusinessRecordFilter;
import com.dili.card.type.ActionType;
import com.dili.card.type.BizNoType;
import com.dili.card.type.CardType;
import com.dili.card.type.FeeType;
import com.dili.card.type.FundItem;
import com.dili.card.type.OperateState;
import com.dili.card.type.OperateType;
import com.dili.card.util.DateUtil;
import com.dili.card.util.PageUtils;
import com.dili.commons.rabbitmq.RabbitMQMessageService;
import com.dili.ss.domain.PageOutput;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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
    private IBusinessRecordDao businessRecordDao;
    @Resource
    private IAccountCycleService accountCycleService;
    @Resource
    private RabbitMQMessageService rabbitMQMessageService;
    @Autowired
    private IAccountQueryService accountQueryService;
    @Autowired
    private ICustomerService customerService;
    @Resource
    private SerialRecordRpcResolver serialRecordRpcResolver;

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
        serialRecord.setHoldName(businessRecord.getHoldName());
        serialRecord.setCustomerType(businessRecord.getCustomerType());
        serialRecord.setOperatorId(businessRecord.getOperatorId());
        serialRecord.setOperatorNo(businessRecord.getOperatorNo());
        serialRecord.setOperatorName(businessRecord.getOperatorName());
        serialRecord.setFirmId(businessRecord.getFirmId());
        serialRecord.setType(businessRecord.getType());
        serialRecord.setTradeType(businessRecord.getTradeType());
        serialRecord.setTradeChannel(businessRecord.getTradeChannel());
        serialRecord.setTradeNo(businessRecord.getTradeNo());
        serialRecord.setNotes(businessRecord.getNotes());
        serialRecord.setOperateTime(businessRecord.getOperateTime());
    }

    @Override
    public void handleFailure(SerialDto serialDto) {
        try {
            BusinessRecordDo businessRecord = new BusinessRecordDo();
            businessRecord.setSerialNo(serialDto.getSerialNo());
            businessRecord.setState(OperateState.FAILURE.getCode());
            businessRecordDao.doFailureUpdate(businessRecord);
        } catch (Exception e) {
            LOGGER.error(JSON.toJSONString(serialDto), e);//记录数据方便后期处理
            throw new CardAppBizException("修改办理状态失败");
        }
    }

    @Override
    public void handleSuccess(SerialDto serialDto) {
        handleSuccess(serialDto, true);
    }

    @Override
    public void handleProcessing(SerialDto serialDto) {
        //修改状态
        BusinessRecordDo businessRecord = new BusinessRecordDo();
        businessRecord.setSerialNo(serialDto.getSerialNo());
        businessRecord.setState(OperateState.PROCESSING.getCode());
        businessRecord.setStartBalance(serialDto.getStartBalance());
        businessRecord.setEndBalance(serialDto.getEndBalance());
        //用于意外情况时恢复数据
        businessRecord.setAttach(JSON.toJSONString(serialDto));
        businessRecordDao.doSuccessUpdate(businessRecord);
    }

    @Override
    public void handleSuccess(SerialDto serialDto, boolean saveSerial) {
        try {
            //修改状态
            BusinessRecordDo businessRecord = new BusinessRecordDo();
            businessRecord.setSerialNo(serialDto.getSerialNo());
            businessRecord.setState(OperateState.SUCCESS.getCode());
            businessRecord.setStartBalance(serialDto.getStartBalance());
            businessRecord.setEndBalance(serialDto.getEndBalance());
            businessRecordDao.doSuccessUpdate(businessRecord);
        } catch (Exception e) {
            LOGGER.error(JSON.toJSONString(serialDto), e);//记录数据方便后期处理
            throw new CardAppBizException("", "修改办理状态失败");
        }
        if (saveSerial) {
            saveSerialRecord(serialDto);
        }
    }

    @Override
    public void saveSerialRecord(SerialDto serialDto) {
        try {
            //保存流水
            //serialRecordRpcResolver.batchSave(serialDto.getSerialRecordList());
            //消息服务器
            rabbitMQMessageService.send(SerialMQConfig.EXCHANGE_ACCOUNT_SERIAL, SerialMQConfig.ROUTING_ACCOUNT_SERIAL, JSON.toJSONString(serialDto.getSerialRecordList()), null, null);
        } catch (Exception e) {
            LOGGER.error(JSON.toJSONString(serialDto), e);//记录数据方便后期处理
            //由于通过mq存储流水，本身带有重试机制，故屏蔽异常
            //throw new CardAppBizException("保存操作交易流水失败");
        }
    }

    @Override
    public List<BusinessRecordDo> cycleReprintList(SerialQueryDto serialQueryDto) {
        SerialQueryDto query = new SerialQueryDto();
        query.setFirmId(serialQueryDto.getFirmId());
        query.setOperatorId(serialQueryDto.getOperatorId());
        AccountCycleDo accountCycle = accountCycleService.findActiveCycleByUserId(serialQueryDto.getOperatorId());
        if (accountCycle == null) {
            throw new CardAppBizException("", "未查询到操作员账期");
        }
        query.setCycleNo(accountCycle.getCycleNo());
        query.setSort("operate_time");
        query.setOrder("desc");
        query.setState(OperateState.SUCCESS.getCode());
        query.setOperateTypeList(OperateType.createReprintList());
        query.setLimit(serialQueryDto.getLimit() != null ? serialQueryDto.getLimit() : 20);
        return businessRecordDao.list(query);
    }

    @Override
    public List<BusinessRecordDo> todayChargeList(SerialQueryDto serialQueryDto) {
        SerialQueryDto query = new SerialQueryDto();
        query.setFirmId(serialQueryDto.getFirmId());
        List<Long> accountIdList = new ArrayList<>();
        accountIdList.add(serialQueryDto.getAccountId());
        AccountWithAssociationResponseDto cardAssociation = accountQueryService.getAssociationByAccountId(serialQueryDto.getAccountId(), Constant.FALSE_INT_FLAG);

        if (!CollUtil.isEmpty(cardAssociation.getAssociation())) {
            accountIdList.addAll(cardAssociation.getAssociation().stream().map(UserAccountCardResponseDto::getAccountId).collect(Collectors.toList()));
        }
        query.setAccountIdList(accountIdList);
        query.setType(OperateType.ACCOUNT_CHARGE.getCode());
        query.setState(OperateState.SUCCESS.getCode());
        query.setOperateTimeStart(DateUtil.formatDate("yyyy-MM-dd") + " 00:00:00");
        query.setOperateTimeEnd(DateUtil.formatDate("yyyy-MM-dd") + " 23:59:59");
        query.setSort("operate_time");
        query.setOrder("desc");
        return businessRecordDao.list(query);
    }

    @Override
    public List<BusinessRecordDo> queryBusinessRecord(SerialQueryDto serialQueryDto) {
        return businessRecordDao.list(serialQueryDto);
    }

    @Override
    public PageOutput<List<BusinessRecordResponseDto>> queryPage(SerialQueryDto serialQueryDto) {
        Page<BusinessRecordDo> page = PageHelper.startPage(serialQueryDto.getPage(), serialQueryDto.getRows());
        List<BusinessRecordDo> businessRecordDos = this.queryBusinessRecord(serialQueryDto);
        List<BusinessRecordResponseDto> recordResponseDtos = businessRecordDos.stream().map(record -> {
            BusinessRecordResponseDto responseDto = new BusinessRecordResponseDto();
            BeanUtils.copyProperties(record, responseDto);
            return responseDto;
        }).collect(Collectors.toList());
        return PageUtils.convert2PageOutput(page, recordResponseDtos);
    }

    @Override
    public BusinessRecordDo createBusinessRecord(CardRequestDto cardRequestDto, UserAccountCardResponseDto accountCard, IBusinessRecordFilter filter) {
        //账务周期
        AccountCycleDo accountCycle = accountCycleService.findActiveCycleByUserId(cardRequestDto.getOpId(), cardRequestDto.getOpName(), cardRequestDto.getOpNo());
        return createBusinessRecord(cardRequestDto, accountCard, filter, accountCycle.getCycleNo());
    }

    @Override
    public BusinessRecordDo createBusinessRecord(CardRequestDto cardRequestDto, UserAccountCardResponseDto accountCard,
                                                 IBusinessRecordFilter filter, Long cycleNo) {
        BusinessRecordDo businessRecord = new BusinessRecordDo();
        //编号、卡号、账户id
        businessRecord.setSerialNo(uidRpcResovler.bizNumber(BizNoType.OPERATE_SERIAL_NO.getCode()));
        businessRecord.setAccountId(cardRequestDto.getAccountId());
        businessRecord.setCardNo(cardRequestDto.getCardNo());
        //客户信息 由于卡账户冗余了客户相关信息，所以直接获取
        businessRecord.setCustomerId(accountCard.getCustomerId());
        businessRecord.setCustomerNo(accountCard.getCustomerCode());
        businessRecord.setCustomerName(accountCard.getCustomerName());
        businessRecord.setCustomerType(customerService.getSubTypeCodes(accountCard.getCustomerId(), accountCard.getFirmId()));
        businessRecord.setHoldName(accountCard.getHoldName());
        businessRecord.setNotes(cardRequestDto.getNotes());
        //账务周期
        businessRecord.setCycleNo(cycleNo);
        //操作员信息
        businessRecord.setOperatorId(cardRequestDto.getOpId());
        businessRecord.setOperatorNo(cardRequestDto.getOpNo());
        businessRecord.setOperatorName(cardRequestDto.getOpName());
        businessRecord.setFirmId(cardRequestDto.getFirmId());
        //时间、默认状态等数据
        LocalDateTime localDateTime = LocalDateTime.now();
        businessRecord.setState(OperateState.PROCESSING.getCode());
        businessRecord.setOperateTime(localDateTime);
        businessRecord.setModifyTime(localDateTime);
        businessRecord.setVersion(1);
        if (filter != null) {
            filter.handle(businessRecord);
        }
        return businessRecord;
    }

    @Override
    public SerialDto createAccountSerial(BusinessRecordDo businessRecord, IAccountSerialFilter filter) {
        SerialDto serialDto = new SerialDto();
        serialDto.setSerialNo(businessRecord.getSerialNo());
        List<SerialRecordDo> serialRecordList = new ArrayList<>(1);
        SerialRecordDo serialRecordDo = new SerialRecordDo();
        this.copyCommonFields(serialRecordDo, businessRecord);
        if (filter != null) {
            filter.handle(serialRecordDo, null);
        }
        serialRecordList.add(serialRecordDo);
        serialDto.setSerialRecordList(serialRecordList);
        return serialDto;
    }

    @Override
    public SerialDto createAccountSerialWithFund(BusinessRecordDo businessRecord, TradeResponseDto tradeResponseDto, IAccountSerialFilter filter) {
        return this.createAccountSerialWithFund(businessRecord, tradeResponseDto, filter, false);
    }

    @Override
    public SerialDto createAccountSerialWithFund(BusinessRecordDo businessRecord, TradeResponseDto tradeResponseDto, IAccountSerialFilter filter, boolean isFrozen) {
        SerialDto serialDto = new SerialDto();
        serialDto.setSerialNo(businessRecord.getSerialNo());
        tradeResponseDto = tradeResponseDto == null ? new TradeResponseDto() : tradeResponseDto;
        Long totalFrozenAmount = countFrozenBalance(tradeResponseDto, isFrozen);
        Long opAmount = getOpAmount(tradeResponseDto, isFrozen);
        serialDto.setStartBalance(countStartBalance(tradeResponseDto.getBalance(), totalFrozenAmount));
        serialDto.setEndBalance(countEndBalance(serialDto.getStartBalance(), opAmount));
        serialDto.setTotalBalance(countTotalBalance(tradeResponseDto.getBalance(), opAmount, isFrozen));

        if (!CollUtil.isEmpty(tradeResponseDto.getStreams())) {
            List<FeeItemDto> feeItemList = tradeResponseDto.getStreams();
            List<SerialRecordDo> serialRecordList = new ArrayList<>(feeItemList.size());
            for (FeeItemDto feeItem : feeItemList) {
                SerialRecordDo serialRecord = new SerialRecordDo();
                this.copyCommonFields(serialRecord, businessRecord);
                serialRecord.setAction(feeItem.getAmount() == null ? null : feeItem.getAmount() < 0L ? ActionType.EXPENSE.getCode() : ActionType.INCOME.getCode());
                serialRecord.setStartBalance(countStartBalance(feeItem.getBalance(), totalFrozenAmount));
                serialRecord.setAmount(feeItem.getAmount() == null ? null : Math.abs(feeItem.getAmount()));//由于是通过标记位表示收入或支出，固取绝对值
                serialRecord.setEndBalance(countEndBalance(serialRecord.getStartBalance(), feeItem.getAmount()));
                // 操作时间-与支付系统保持一致
                serialRecord.setOperateTime(tradeResponseDto.getWhen());
                if (filter != null) {
                    filter.handle(serialRecord, feeItem.getType());
                }
                serialRecordList.add(serialRecord);
            }
            serialDto.setSerialRecordList(serialRecordList);
        }
        return serialDto;
    }

    @Override
    public BusinessRecordDo findBusinessRecordBySerialNo(String serialNo) {
        return businessRecordDao.getBySerialNo(serialNo);
    }

    @Override
    public PageOutput<List<SerialRecordDo>> getPage(SerialQueryDto serialQueryDto) {
        //若查询包含副卡，要先把该卡名下的所有副卡查出来
        this.setCardNosWhenIncludeSlave(serialQueryDto);
        return serialRecordRpcResolver.listPage(serialQueryDto);
    }

    @Override
    public Long countOperateAmount(SerialQueryDto serialQueryDto) {
        //若查询包含副卡，要先把该卡名下的所有副卡查出来
        this.setCardNosWhenIncludeSlave(serialQueryDto);
        return serialRecordRpcResolver.countOperateAmount(serialQueryDto);
    }

    @Override
    public BusinessRecordDo getBySerialNo(String serialNo) {
        return businessRecordDao.getBySerialNo(serialNo);
    }

    private void setCardNosWhenIncludeSlave(SerialQueryDto serialQueryDto) {
        Integer includeSlave = serialQueryDto.getIncludeSlave();
        String cardNo = serialQueryDto.getCardNo();
        if (NumberUtils.toInt(includeSlave + "") == 1 && StrUtil.isNotBlank(cardNo)) {
            //设置为Null防止sql错乱
            serialQueryDto.setCardNo(null);
            serialQueryDto.setCardNos(Lists.newArrayList(cardNo));

            UserAccountCardQuery masterParam = new UserAccountCardQuery();
            masterParam.setExcludeUnusualState(0);
            masterParam.setCardNos(Lists.newArrayList(cardNo));
            List<UserAccountCardResponseDto> masterCards = accountQueryService.getList(masterParam);
            if (CollectionUtil.isEmpty(masterCards)){
                return;
            }
            UserAccountCardResponseDto masterCard = masterCards.get(0);
            //组装名下所有副卡
            if (CardType.isMaster(masterCard.getCardType())) {
                UserAccountCardQuery slaveParam = new UserAccountCardQuery();
                slaveParam.setParentAccountId(masterCard.getAccountId());
                slaveParam.setExcludeUnusualState(0);
                List<UserAccountCardResponseDto> slaveCards = accountQueryService.getList(slaveParam);
                serialQueryDto.getCardNos().addAll(slaveCards.stream()
                        .map(UserAccountCardResponseDto::getCardNo)
                        .collect(Collectors.toList()));
            }
        }
    }

    private static Long getOpAmount(TradeResponseDto tradeResponseDto, boolean isFrozen) {
        if (isFrozen) {
            //冻结是正，解冻是负，这里需要倒一下正负
            return -tradeResponseDto.getFrozenAmount();
        } else {
            for (FeeItemDto feeItemDto : tradeResponseDto.getStreams()) {
                if (Integer.valueOf(FeeType.ACCOUNT.getCode()).equals(feeItemDto.getType())) {
                    return feeItemDto.getAmount();
                }
                if (Integer.valueOf(FundItem.RETURN_CARD_CHANGE.getCode()).equals(feeItemDto.getType())) {
                    return feeItemDto.getAmount();
                }
            }
            return 0L;
        }
    }

    private static Long countFrozenBalance(TradeResponseDto tradeResponseDto, boolean isFrozen) {
        if (isFrozen) {
            return tradeResponseDto.getFrozenBalance();
        } else {
            return tradeResponseDto.countTotalFrozenAmount();
        }
    }

    /**
     * 根据期初总余额、总冻结金额计算期初可用余额
     * @param balance 总余额（包含冻结）
     * @param totalFrozenAmount 冻结金额
     * @return
     */
    public static Long countStartBalance(Long balance, Long totalFrozenAmount) {
        if (balance == null || totalFrozenAmount == null) {
            return null;
        }
        return balance - totalFrozenAmount;
    }


    /**
     * 根据期初可用余额、操作金额计算期末可用余额
     * @param startBalance 期初可用余额（不含冻结）
     * @param amount 操作金额
     * @return
     */
    public static Long countEndBalance(Long startBalance, Long amount) {
        if (startBalance == null || amount == null) {
            return null;
        }
        return startBalance + amount;
    }


    /**
     * 根据期初可用余额、操作金额计算期末总余额(包含冻结)
     * @param balance 总余额（冻结）
     * @param amount 操作金额
     * @return
     */
    public static Long countTotalBalance(Long balance, Long amount, boolean isFrozen) {
        if (isFrozen) {//如果是冻结解冻，总余额不变
            return balance;
        }
        if (balance == null || amount == null) {
            return null;
        }
        return balance + amount;
    }
}
