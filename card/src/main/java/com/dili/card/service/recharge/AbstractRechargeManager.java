package com.dili.card.service.recharge;

import cn.hutool.core.thread.ThreadUtil;
import com.alibaba.fastjson.JSONObject;
import com.dili.card.common.constant.ReqParamExtra;
import com.dili.card.dto.FundRequestDto;
import com.dili.card.dto.SerialDto;
import com.dili.card.dto.UserAccountCardResponseDto;
import com.dili.card.dto.pay.CreateTradeRequestDto;
import com.dili.card.dto.pay.TradeRequestDto;
import com.dili.card.dto.pay.TradeResponseDto;
import com.dili.card.entity.BusinessRecordDo;
import com.dili.card.rpc.resolver.SmsMessageRpcResolver;
import com.dili.card.service.IAccountQueryService;
import com.dili.card.service.IMiscService;
import com.dili.card.service.IPayService;
import com.dili.card.service.ISerialService;
import com.dili.card.type.DictValue;
import com.dili.card.type.FeeType;
import com.dili.card.type.FundItem;
import com.dili.card.type.OperateType;
import com.dili.card.type.PaySubject;
import com.dili.card.type.TradeChannel;
import com.dili.card.type.TradeType;
import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @Auther: miaoguoxin
 * @Date: 2020/7/2 10:22
 */
public abstract class AbstractRechargeManager implements IRechargeManager {
    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractRechargeManager.class);

    @Autowired
    protected IPayService payService;
    @Autowired
    protected ISerialService serialService;
    @Autowired
    private IAccountQueryService accountQueryService;
    @Autowired
    private SmsMessageRpcResolver smsMessageRpcResolver;
    @Autowired
    private IMiscService miscService;


    /**
     * 充值操作入口封装
     * @author miaoguoxin
     * @date 2020/7/2
     */
    public final String doRecharge(FundRequestDto requestDto) {
        this.beforeRecharge(requestDto);
        //手续费可能null
        long serviceCost = NumberUtils.toLong(requestDto.getServiceCost() + "");

        UserAccountCardResponseDto userAccount = accountQueryService.getByAccountId(requestDto);
        Long rechargeAmount = requestDto.getAmount();
        BusinessRecordDo businessRecord = serialService.createBusinessRecord(requestDto, userAccount, record -> {
            record.setType(OperateType.ACCOUNT_CHARGE.getCode());
            record.setAmount(rechargeAmount);
            record.setTradeType(TradeType.DEPOSIT.getCode());
            record.setTradeChannel(requestDto.getTradeChannel());
            record.setServiceCost(serviceCost);
            record.setBankCardType(this.getBankType(requestDto));
            record.setPosType(this.getPosType(requestDto));
            if (requestDto.getExtra() != null) {
                record.setAttach(requestDto.getExtra().toJSONString());
            }
        });

        long l = System.currentTimeMillis();
        CreateTradeRequestDto tradeRequest = CreateTradeRequestDto.createTrade(
                this.getTradeType(requestDto).getCode(),
                userAccount.getAccountId(),
                userAccount.getFundAccountId(),
                rechargeAmount,
                businessRecord.getSerialNo(),
                String.valueOf(businessRecord.getCycleNo()));
        tradeRequest.setDescription(PaySubject.RECHARGE.getName());
        //创建交易
        String tradeNo = payService.createTrade(tradeRequest);
        LOGGER.info("创建充值交易耗费时间:{}ms", System.currentTimeMillis() - l);
        //保存业务办理记录
        businessRecord.setTradeNo(tradeNo);
        businessRecord.setNotes(this.buildBusinessRecordNote(requestDto));
        serialService.saveBusinessRecord(businessRecord);

        long l1 = System.currentTimeMillis();
        FundItem serviceCostItem = this.getServiceCostItem(requestDto);
        //支付那边只有有手续费>0才可以正常支付
        TradeRequestDto dto = TradeRequestDto.createTrade(userAccount, tradeNo, requestDto.getTradeChannel(), requestDto.getTradePwd());
        if (serviceCostItem != null && serviceCost > 0L) {
            dto.addServiceFeeItem(requestDto.getServiceCost(), serviceCostItem);
        }
        TradeResponseDto tradeResponseDto = payService.commitTrade(dto);
        LOGGER.info("提交充值交易耗费时间:{}ms", System.currentTimeMillis() - l1);

        //没有手续费的时候可能需要添加一个空项
        if (serviceCostItem != null && this.canAddEmptyFundItem(requestDto)) {
            tradeResponseDto.addEmptyFeeItem(serviceCostItem);
        }
        this.afterRecharge(requestDto, businessRecord);
        //记录远程日志数据
        this.doRecordCompleteLog(requestDto, businessRecord, tradeResponseDto);

        // 发送短信通知
        ThreadUtil.execute(() -> {
            String phone = userAccount.getCustomerContactsPhone();
            String cardNo = userAccount.getCardNo();
            DictValue dictValue = DictValue.RECHARGE_SMS_ALLOW_SEND;
            String val = miscService.getSingleDictVal(dictValue.getCode(), requestDto.getFirmId(), dictValue.getDefaultVal());
            if ("1".equals(val)) {
                smsMessageRpcResolver.rechargeNotice(phone, cardNo, requestDto.getFirmCode(), tradeResponseDto);
            }
        });
        return businessRecord.getSerialNo();
    }

    /**
     * 开始充值前的一些逻辑
     * @author miaoguoxin
     * @date 2020/9/1
     */
    protected void beforeRecharge(FundRequestDto requestDto) {

    }

    /**
     * 充值请求之后的一些逻辑
     * @author miaoguoxin
     * @date 2020/7/20
     */
    protected void afterRecharge(FundRequestDto requestDto, BusinessRecordDo record) {

    }

    private Integer getBankType(FundRequestDto requestDto) {
        if (requestDto.getTradeChannel() == TradeChannel.POS.getCode()) {
            JSONObject extra = requestDto.getExtra();
            if (extra == null) {
                extra = new JSONObject();
            }
            return extra.getInteger(ReqParamExtra.BANK_TYPE);
        }
        return null;
    }

    private String getPosType(FundRequestDto requestDto) {
        if (requestDto.getTradeChannel() == TradeChannel.POS.getCode()) {
            JSONObject extra = requestDto.getExtra();
            if (extra == null) {
                extra = new JSONObject();
            }
            return extra.getString(ReqParamExtra.POS_TYPE);
        }
        return null;
    }

    private void doRecordCompleteLog(FundRequestDto requestDto, BusinessRecordDo businessRecord, TradeResponseDto tradeResponseDto) {
        FundItem principalFundItem = this.getPrincipalFundItem(requestDto);
        SerialDto serialDto = serialService.createAccountSerialWithFund(businessRecord, tradeResponseDto, (serialRecord, feeType) -> {
            FundItem fundItem;
            if (Integer.valueOf(FeeType.ACCOUNT.getCode()).equals(feeType)) {
                fundItem = principalFundItem;
            } else {
                fundItem = FundItem.getByCode(feeType);
            }
            if (fundItem != null) {
                serialRecord.setFundItem(fundItem.getCode());
                serialRecord.setFundItemName(fundItem.getName());
            }
            serialRecord.setNotes(this.buildSerialRecordNote(requestDto));
        });
        serialService.handleSuccess(serialDto);
    }
}
