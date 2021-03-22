package com.dili.card.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.dili.card.common.constant.CacheKey;
import com.dili.card.common.constant.Constant;
import com.dili.card.common.constant.ReqParamExtra;
import com.dili.card.common.constant.ServiceName;
import com.dili.card.dto.FundRequestDto;
import com.dili.card.dto.PipelineRecordQueryDto;
import com.dili.card.dto.SerialDto;
import com.dili.card.dto.UnfreezeFundDto;
import com.dili.card.dto.UserAccountCardResponseDto;
import com.dili.card.dto.pay.BalanceResponseDto;
import com.dili.card.dto.pay.CreateTradeRequestDto;
import com.dili.card.dto.pay.FreezeFundRecordDto;
import com.dili.card.dto.pay.FreezeFundRecordParam;
import com.dili.card.dto.pay.FundOpResponseDto;
import com.dili.card.dto.pay.PipelineRecordParam;
import com.dili.card.dto.pay.PipelineRecordResponseDto;
import com.dili.card.dto.pay.TradeResponseDto;
import com.dili.card.entity.AccountCycleDo;
import com.dili.card.entity.BusinessRecordDo;
import com.dili.card.entity.SerialRecordDo;
import com.dili.card.exception.CardAppBizException;
import com.dili.card.rpc.PayRpc;
import com.dili.card.rpc.resolver.CustomerRpcResolver;
import com.dili.card.rpc.resolver.GenericRpcResolver;
import com.dili.card.rpc.resolver.PayRpcResolver;
import com.dili.card.rpc.resolver.SmsMessageRpcResolver;
import com.dili.card.rpc.resolver.UidRpcResovler;
import com.dili.card.service.IAccountCycleService;
import com.dili.card.service.IAccountQueryService;
import com.dili.card.service.ICustomerService;
import com.dili.card.service.IFundService;
import com.dili.card.service.IMiscService;
import com.dili.card.service.ISerialService;
import com.dili.card.service.recharge.AbstractRechargeManager;
import com.dili.card.service.recharge.RechargeFactory;
import com.dili.card.service.withdraw.BankWithdrawServiceImpl;
import com.dili.card.type.ActionType;
import com.dili.card.type.BankWithdrawState;
import com.dili.card.type.BizNoType;
import com.dili.card.type.CardType;
import com.dili.card.type.DictValue;
import com.dili.card.type.FeeType;
import com.dili.card.type.FundItem;
import com.dili.card.type.OperateState;
import com.dili.card.type.OperateType;
import com.dili.card.type.PayPipelineType;
import com.dili.customer.sdk.domain.dto.CustomerExtendDto;
import com.dili.feign.support.UapSessionThreadUtils;
import com.dili.ss.domain.PageOutput;
import com.dili.ss.redis.service.RedisUtil;
import com.dili.uap.sdk.domain.Firm;
import com.dili.uap.sdk.rpc.FirmRpc;
import io.seata.spring.annotation.GlobalTransactional;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 资金操作service实现类
 *
 * @author xuliang
 */
@Service
public class FundServiceImpl implements IFundService {
    private static final Logger LOGGER = LoggerFactory.getLogger(FundServiceImpl.class);
    @Autowired
    private RedisUtil redisUtil;
    @Resource
    private ICustomerService customerService;
    @Resource
    private ISerialService serialService;
    @Autowired
    private IAccountQueryService accountQueryService;
    @Autowired
    private PayRpcResolver payRpcResolver;
    @Resource
    private UidRpcResovler uidRpcResovler;
    @Resource
    private PayRpc payRpc;
    @Autowired
    private RechargeFactory rechargeFactory;
    @Autowired
    private IAccountCycleService accountCycleService;
    @Autowired
    private CustomerRpcResolver customerRpcResolver;
    @Autowired
    private IMiscService miscService;
    @Autowired
    private SmsMessageRpcResolver smsMessageRpcResolver;
    @Autowired
    private BankWithdrawServiceImpl bankWithdrawService;
    @Autowired
    private FirmRpc firmRpc;

    @Override
    @GlobalTransactional(rollbackFor = Exception.class)
    @Transactional(rollbackFor = Exception.class)
    public void frozen(FundRequestDto requestDto) {
        UserAccountCardResponseDto accountCard = accountQueryService.getByAccountId(requestDto);
        if (!CardType.isMaster(accountCard.getCardType())) {
            throw new CardAppBizException("只有主卡可以冻结资金");
        }
        BalanceResponseDto balance = payRpcResolver.findBalanceByFundAccountId(accountCard.getFundAccountId());
        if (requestDto.getAmount() > balance.getAvailableAmount()) {
            throw new CardAppBizException("可用余额不足");
        }

        //账务周期
        AccountCycleDo accountCycle = accountCycleService.findLatestCycleByUserId(requestDto.getOpId());
        BusinessRecordDo businessRecord = serialService.createBusinessRecord(requestDto, accountCard, record -> {
            record.setType(OperateType.FROZEN_FUND.getCode());
            record.setAmount(requestDto.getAmount());
            record.setNotes(requestDto.getMark());
        }, accountCycle == null ? 0L : accountCycle.getCycleNo());
        CreateTradeRequestDto createTradeRequestDto = CreateTradeRequestDto
                .createFrozenAmount(accountCard.getFundAccountId(), accountCard.getAccountId(), requestDto.getAmount());
        createTradeRequestDto.setExtension(this.serializeFrozenExtra(requestDto));
        createTradeRequestDto.setDescription(requestDto.getMark());
        FundOpResponseDto fundOpResponseDto = payRpcResolver.postFrozenFund(createTradeRequestDto);

        businessRecord.setTradeNo(String.valueOf(fundOpResponseDto.getFrozenId()));
        serialService.saveBusinessRecord(businessRecord);

        TradeResponseDto transaction = fundOpResponseDto.getTransaction();
        transaction.addVirtualPrincipalFundItem(-requestDto.getAmount());
        SerialDto serialDto = serialService.createAccountSerialWithFund(businessRecord, transaction,
                (serialRecord, feeType) -> {
                    if (Integer.valueOf(FeeType.ACCOUNT.getCode()).equals(feeType)) {
                        serialRecord.setFundItem(FundItem.MANDATORY_FREEZE_FUND.getCode());
                        serialRecord.setFundItemName(FundItem.MANDATORY_FREEZE_FUND.getName());
                    }
                    serialRecord.setNotes(requestDto.getMark());
                }, true);
        serialService.handleSuccess(serialDto);
    }

    @Override
    @GlobalTransactional(rollbackFor = Exception.class)
    @Transactional(rollbackFor = Exception.class)
    public void unfrozen(UnfreezeFundDto unfreezeFundDto) {
        UserAccountCardResponseDto accountInfo = accountQueryService.getByAccountId(unfreezeFundDto.getAccountId());
        List<SerialRecordDo> serialList = new ArrayList<SerialRecordDo>();
        for (Long frozenId : unfreezeFundDto.getFrozenIds()) {
            // 对应支付的frozenId
            UnfreezeFundDto dto = new UnfreezeFundDto();
            dto.setFrozenId(frozenId);
            FundOpResponseDto payResponse = GenericRpcResolver.resolver(payRpc.unfrozenFund(dto), ServiceName.PAY);

            // 保存卡务操作记录
            BusinessRecordDo businessRecord = createBusinessRecord(accountInfo, unfreezeFundDto, payResponse);
            serialService.saveBusinessRecord(businessRecord);

            // 构建全局操作记录
            SerialRecordDo serialRecord = buildSerialRecord(accountInfo, unfreezeFundDto, payResponse);
            serialList.add(serialRecord);
        }

        SerialDto serialDto = new SerialDto();
        // 保存全局操作记录
        serialDto.setSerialRecordList(serialList);
        serialService.saveSerialRecord(serialDto);
    }

    @Override
    @GlobalTransactional(rollbackFor = Exception.class)
    @Transactional(rollbackFor = Exception.class)
    public String recharge(FundRequestDto requestDto) {
        AbstractRechargeManager rechargeManager = rechargeFactory.getRechargeManager(requestDto.getTradeChannel());
        return rechargeManager.doRecharge(requestDto);
    }

    @Override
    public PageOutput<List<PipelineRecordResponseDto>> todayBankWithdrawPage(PipelineRecordQueryDto param) {
        PipelineRecordParam query = new PipelineRecordParam();
        query.setType(PayPipelineType.BANK_WITHDRAW.getCode());
        String today = DateUtil.format(new Date(), "yyyy-MM-dd");
        query.setStartDate(today);
        query.setEndDate(today);
        query.setPageNo(param.getPage());
        query.setPageSize(param.getRows());
        PageOutput<List<PipelineRecordResponseDto>> result = GenericRpcResolver.resolver(payRpc.pipelineList(query), ServiceName.PAY);
        List<PipelineRecordResponseDto> data = result.getData();
        for (PipelineRecordResponseDto dto : data) {
            dto.setOperatorId(param.getOpId());
            dto.setOperatorName(param.getOpName());
        }
        return result;
    }

    /**
     *  由于支付那边回调都是相同结果，并且本地库只涉及更新操作，
     *  因此不需要考虑幂等性问题
     * @author miaoguoxin
     * @date 2021/1/18
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void handleBankWithdrawCallback(TradeResponseDto tradeResponseDto) {
        //支付系统存储的流水号有前缀，这里需要截掉
        String realSerialNo = StrUtil.removePrefix(tradeResponseDto.getSerialNo(), Constant.PAY_SERIAL_NO_PREFIX);
        String key = CacheKey.BANK_WITHDRAW_PROCESSING_SERIAL_PREFIX + realSerialNo;
        try {
            JSONObject jObj = redisUtil.get(key, JSONObject.class);
            SerialDto serial;
            if (jObj == null) {
                LOGGER.info("圈提回调redis找不到对应流水号:{}，可能已经过期，从数据库读取", realSerialNo);
                BusinessRecordDo businessRecordDo = serialService.getBySerialNo(realSerialNo);
                if (businessRecordDo == null) {
                    LOGGER.info("圈提回调数据库找不到对应流水号:{}，可能是非法请求", realSerialNo);
                    return;
                }
                jObj = JSON.parseObject(businessRecordDo.getAttach(), JSONObject.class);
                FundRequestDto fundRequestDto = jObj.getObject(ReqParamExtra.FUND_REQUEST, FundRequestDto.class);
                BusinessRecordDo record = jObj.getObject(ReqParamExtra.BUSINESS_RECORD, BusinessRecordDo.class);
                serial = bankWithdrawService.createAccountSerial(fundRequestDto, record, tradeResponseDto);
            } else {
                FundRequestDto fundRequestDto = jObj.getObject(ReqParamExtra.FUND_REQUEST, FundRequestDto.class);
                BusinessRecordDo record = jObj.getObject(ReqParamExtra.BUSINESS_RECORD, BusinessRecordDo.class);
                serial = bankWithdrawService.createAccountSerial(fundRequestDto, record, tradeResponseDto);
            }
            if (BankWithdrawState.FAILED.getCode() == tradeResponseDto.getState()) {
                LOGGER.info("圈提回调处理失败，对应流水号:{}", realSerialNo);
                serialService.handleFailure(serial);
            } else {
                serialService.handleSuccess(serial);
                LOGGER.info("圈提回调处理成功，开始发送短信，对应流水号:{}", realSerialNo);
                this.asyncSendSms(serial, tradeResponseDto);
            }
        } finally {
            redisUtil.remove(key);
        }
    }


    @Override
    public PageOutput<List<FreezeFundRecordDto>> frozenRecord(FreezeFundRecordParam queryParam) {
        // 从支付查询 默认查询从当日起一年内的未解冻记录
        PageOutput<List<FreezeFundRecordDto>> pageOutPut = GenericRpcResolver
                .resolver(payRpc.listFrozenRecord(queryParam), "pay-service");
        for (FreezeFundRecordDto record : pageOutPut.getData()) {
            if (StringUtils.isNoneBlank(record.getExtension())) {
                // 在冻结资金时会以json格式存入当时的操作人及其编号
                JSONObject jsonObject = JSONObject.parseObject(record.getExtension());
                record.setOpNo(jsonObject.getString(Constant.OP_NO));
                record.setOpName(jsonObject.getString(Constant.OP_NAME));
            }
        }
        return pageOutPut;
    }

    /**
     * 生成全局日志
     *
     * @param accountInfo
     * @param unfreezeFundDto
     */
    private SerialRecordDo buildSerialRecord(UserAccountCardResponseDto accountInfo,
                                             UnfreezeFundDto unfreezeFundDto, FundOpResponseDto payResponse) {
        SerialRecordDo record = new SerialRecordDo();
        record.setAccountId(accountInfo.getAccountId());
        record.setCardNo(accountInfo.getCardNo());
        record.setCustomerId(accountInfo.getCustomerId());
        record.setCustomerName(accountInfo.getCustomerName());
        record.setCustomerNo(accountInfo.getCustomerCode());
        record.setCustomerType(accountInfo.getCustomerCharacterType());
        record.setFirmId(unfreezeFundDto.getFirmId());
        record.setSerialNo(uidRpcResovler.bizNumber(BizNoType.OPERATE_SERIAL_NO.getCode()));
        record.setNotes(unfreezeFundDto.getRemark());
        record.setOperatorId(unfreezeFundDto.getOpId());
        record.setOperatorName(unfreezeFundDto.getOpName());
        record.setOperatorNo(unfreezeFundDto.getOpNo());
        record.setType(OperateType.UNFROZEN_FUND.getCode());
        record.setFundItem(FundItem.MANDATORY_UNFREEZE_FUND.getCode());
        record.setFundItemName(FundItem.MANDATORY_UNFREEZE_FUND.getName());
        record.setOperateTime(LocalDateTime.now());
        record.setAction(ActionType.INCOME.getCode());
        record.setHoldName(accountInfo.getHoldName());
        // 计算期初期末
        Long balance = payResponse.getTransaction().getBalance();
        Long frozenBalance = payResponse.getTransaction().getFrozenBalance();
        Long frozenAmount = payResponse.getTransaction().getFrozenAmount(); // 解冻金额该值为负数，冻结金额为正数
        record.setStartBalance(balance - frozenBalance);
        record.setEndBalance(balance - frozenBalance - frozenAmount);
        record.setAmount(Math.abs(frozenAmount));//操作记录对于可用余额要显示+
        return record;
    }

    /**
     * 保存本地操作记录
     * @return
     */
    private BusinessRecordDo createBusinessRecord(UserAccountCardResponseDto accountInfo,
                                                  UnfreezeFundDto unfreezeFundDto, FundOpResponseDto payResponse) {
        BusinessRecordDo businessRecord = new BusinessRecordDo();
        //编号、卡号、账户id
        businessRecord.setSerialNo(uidRpcResovler.bizNumber(BizNoType.OPERATE_SERIAL_NO.getCode()));
        businessRecord.setAccountId(accountInfo.getAccountId());
        businessRecord.setCardNo(accountInfo.getCardNo());
        //客户信息 由于卡账户冗余了客户相关信息，所以直接获取
        businessRecord.setCustomerId(accountInfo.getCustomerId());
        businessRecord.setCustomerNo(accountInfo.getCustomerCode());
        businessRecord.setCustomerName(accountInfo.getCustomerName());
        businessRecord.setCustomerType(customerService.getSubTypeCodes(accountInfo.getCustomerId(), accountInfo.getFirmId()));
        businessRecord.setHoldName(accountInfo.getHoldName());
        businessRecord.setNotes(unfreezeFundDto.getRemark());
        //账务周期
        AccountCycleDo accountCycle = accountCycleService.findLatestCycleByUserId(unfreezeFundDto.getOpId());
        businessRecord.setCycleNo(accountCycle == null ? 0L : accountCycle.getCycleNo());
        //操作员信息
        businessRecord.setOperatorId(unfreezeFundDto.getOpId());
        businessRecord.setOperatorNo(unfreezeFundDto.getOpNo());
        businessRecord.setOperatorName(unfreezeFundDto.getOpName());
        businessRecord.setFirmId(unfreezeFundDto.getFirmId());
        //时间、默认状态等数据
        LocalDateTime localDateTime = LocalDateTime.now();
        businessRecord.setState(OperateState.SUCCESS.getCode());
        businessRecord.setOperateTime(localDateTime);
        businessRecord.setModifyTime(localDateTime);
        businessRecord.setVersion(1);

        return businessRecord;
    }


    private String serializeFrozenExtra(FundRequestDto requestDto) {
        JSONObject extra = new JSONObject();
        extra.put(Constant.OP_NAME, requestDto.getOpName());
        extra.put(Constant.OP_NO, requestDto.getOpNo());
        return extra.toJSONString();
    }

    private void asyncSendSms(SerialDto finalSerialDto, TradeResponseDto tradeResponseDto) {
        Runnable runnable = UapSessionThreadUtils.wrapRunnable(() -> {
            List<SerialRecordDo> serialRecordList = finalSerialDto.getSerialRecordList();
            if (CollectionUtil.isEmpty(serialRecordList)) {
                return;
            }
            SerialRecordDo recordDo = serialRecordList.get(0);
            String cardNo = recordDo.getCardNo();
            //市场圈提没有卡号、客户记录等，不发送短信
            if (Constant.FIRM_WITHDRAW_CARD_NO.equals(cardNo)) {
                return;
            }
            Long customerId = recordDo.getCustomerId();
            Long firmId = recordDo.getFirmId();
            CustomerExtendDto customerInfo = customerRpcResolver.findCustomerById(customerId, firmId);
            String phone = customerInfo.getContactsPhone();
            DictValue dictValue = DictValue.WITHDRAW_SMS_ALLOW_SEND;
            String val = miscService.getSingleDictVal(dictValue.getCode(), firmId, dictValue.getDefaultVal());
            if ("1".equals(val)) {
                Firm firm = GenericRpcResolver.resolver(firmRpc.getById(firmId), ServiceName.UAP);
                smsMessageRpcResolver.withdrawNotice(phone, cardNo, firm.getCode(), tradeResponseDto);
            }
        });
        //发送短信
        ThreadUtil.execute(runnable);
    }
}
