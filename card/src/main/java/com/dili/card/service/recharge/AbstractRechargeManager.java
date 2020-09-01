package com.dili.card.service.recharge;

import com.alibaba.fastjson.JSONObject;
import com.dili.card.common.constant.Constant;
import com.dili.card.dto.FundRequestDto;
import com.dili.card.dto.SerialDto;
import com.dili.card.dto.UserAccountCardResponseDto;
import com.dili.card.dto.pay.CreateTradeRequestDto;
import com.dili.card.dto.pay.TradeRequestDto;
import com.dili.card.dto.pay.TradeResponseDto;
import com.dili.card.entity.BusinessRecordDo;
import com.dili.card.service.IAccountQueryService;
import com.dili.card.service.IPayService;
import com.dili.card.service.ISerialService;
import com.dili.card.type.FeeType;
import com.dili.card.type.FundItem;
import com.dili.card.type.OperateType;
import com.dili.card.type.TradeChannel;
import com.dili.card.type.TradeType;
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


    /**
     * 充值操作入口封装
     * @author miaoguoxin
     * @date 2020/7/2
     */
    public final String doRecharge(FundRequestDto requestDto) {
        this.beforeRecharge(requestDto);

        UserAccountCardResponseDto userAccount = accountQueryService.getByAccountId(requestDto);
        Long rechargeAmount = requestDto.getAmount();
        BusinessRecordDo businessRecord = serialService.createBusinessRecord(requestDto, userAccount, record -> {
            record.setType(OperateType.ACCOUNT_CHARGE.getCode());
            record.setAmount(rechargeAmount);
            record.setTradeType(TradeType.DEPOSIT.getCode());
            record.setTradeChannel(requestDto.getTradeChannel());
            record.setServiceCost(requestDto.getServiceCost());
            record.setBankCardType(this.getBankType(requestDto));
            record.setPosType(this.getPosType(requestDto));
        });

        long l = System.currentTimeMillis();
        CreateTradeRequestDto tradeRequest = CreateTradeRequestDto.createTrade(
                this.getTradeType(requestDto).getCode(),
                userAccount.getAccountId(),
                userAccount.getFundAccountId(),
                rechargeAmount,
                businessRecord.getSerialNo(),
                String.valueOf(businessRecord.getCycleNo()));
        //创建交易
        String tradeNo = payService.createTrade(tradeRequest);
        LOGGER.info("创建充值交易耗费时间:{}ms", System.currentTimeMillis() - l);
        //保存业务办理记录
        businessRecord.setTradeNo(tradeNo);
        businessRecord.setNotes(this.buildBusinessRecordNote(requestDto));
        serialService.saveBusinessRecord(businessRecord);

        long l1 = System.currentTimeMillis();
        FundItem serviceCostItem = this.getServiceCostItem(requestDto);

        TradeRequestDto dto = TradeRequestDto.createTrade(userAccount, tradeNo, requestDto.getTradeChannel(), requestDto.getTradePwd());
        if (serviceCostItem != null && requestDto.getServiceCost() != null) {
            dto.addServiceFeeItem(requestDto.getServiceCost(), serviceCostItem);
        }
        TradeResponseDto tradeResponseDto = payService.commitTrade(dto);
        LOGGER.info("提交充值交易耗费时间:{}ms", System.currentTimeMillis() - l1);

        //没有手续费的时候需要添加一个空项
        if (serviceCostItem != null && this.canAddEmptyFundItem(requestDto)) {
            tradeResponseDto.addEmptyFeeItem(serviceCostItem);
        }
        this.afterRecharge(requestDto, businessRecord);
        //记录远程日志数据
        this.doRecordCompleteLog(requestDto, businessRecord, tradeResponseDto);
        return businessRecord.getSerialNo();
    }

    /**
    * 开始充值前的一些逻辑
    * @author miaoguoxin
    * @date 2020/9/1
    */
    protected void beforeRecharge(FundRequestDto requestDto){

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
            return extra.getInteger(Constant.BANK_TYPE);
        }
        return null;
    }

    private String getPosType(FundRequestDto requestDto) {
        if (requestDto.getTradeChannel() == TradeChannel.POS.getCode()) {
            JSONObject extra = requestDto.getExtra();
            if (extra == null) {
                extra = new JSONObject();
            }
            return extra.getString(Constant.POS_TYPE);
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
