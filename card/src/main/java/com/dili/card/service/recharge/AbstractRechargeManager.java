package com.dili.card.service.recharge;

import com.dili.card.dto.FundRequestDto;
import com.dili.card.dto.SerialDto;
import com.dili.card.dto.UserAccountCardResponseDto;
import com.dili.card.dto.pay.CreateTradeRequestDto;
import com.dili.card.dto.pay.RechargeRequestDto;
import com.dili.card.entity.BusinessRecordDo;
import com.dili.card.service.IPayService;
import com.dili.card.service.ISerialService;
import com.dili.card.service.impl.FundServiceImpl;
import com.dili.card.type.OperateType;
import com.dili.card.type.TradeType;
import com.dili.card.util.CurrencyUtils;
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

    /**
    *  预创建充值订单
    * @author miaoguoxin
    * @date 2020/7/6
    */
    public final void doPreRecharge(FundRequestDto requestDto){
        //真正充值的金额
        Long rechargeAmount = this.getRechargeAmount(requestDto);
        BusinessRecordDo businessRecord = new BusinessRecordDo();
        serialService.buildCommonInfo(requestDto, businessRecord);

        UserAccountCardResponseDto userAccount = TradeContextHolder.getVal(TradeContextHolder.USER_ACCOUNT, UserAccountCardResponseDto.class);

        CreateTradeRequestDto createTradeRequest = new CreateTradeRequestDto();
        createTradeRequest.setType(TradeType.DEPOSIT.getCode());
        createTradeRequest.setAccountId(userAccount.getFundAccountId());
        createTradeRequest.setBusinessId(userAccount.getAccountId());
        createTradeRequest.setAmount(rechargeAmount);
        createTradeRequest.setSerialNo(businessRecord.getSerialNo());
        createTradeRequest.setCycleNo(String.valueOf(businessRecord.getCycleNo()));
        createTradeRequest.setDescription("");
        //创建交易
        String tradeId = payService.createTrade(createTradeRequest);
        //保存业务办理记录
        businessRecord.setTradeNo(tradeId);
        businessRecord.setType(OperateType.ACCOUNT_CHARGE.getCode());
        businessRecord.setAmount(rechargeAmount);
        businessRecord.setTradeType(TradeType.DEPOSIT.getCode());
        businessRecord.setTradeChannel(requestDto.getTradeChannel());
        businessRecord.setServiceCost(requestDto.getServiceCost());
        businessRecord.setNotes(requestDto.getServiceCost() == null ? null : String.format("手续费%s元", CurrencyUtils.toYuanWithStripTrailingZeros(requestDto.getServiceCost())));
        serialService.saveBusinessRecord(businessRecord);
        //保存线程变量
        TradeContextHolder.putVal(TradeContextHolder.TRADE_ID_KEY, tradeId);
        TradeContextHolder.putVal(TradeContextHolder.BUSINESS_RECORD_KEY, businessRecord);
    }

    /**
     * 充值操作入口封装
     * @author miaoguoxin
     * @date 2020/7/2
     */
    public final void doRecharge(FundRequestDto requestDto) {
        RechargeRequestDto dto = this.recharge(requestDto);
        payService.commitRecharge(dto);
        //记录日志
        try {
            //TODO 完善构建数据
            SerialDto serialDto = new SerialDto();
            serialService.handleSuccess(serialDto);
        } catch (Exception e) {
            LOGGER.error("withdraw", e);
        }

    }


    protected RechargeRequestDto createRechargeRequestDto(FundRequestDto requestDto) {
        String tradeNo = TradeContextHolder.getVal(TradeContextHolder.TRADE_ID_KEY, String.class);
        UserAccountCardResponseDto userAccount = TradeContextHolder.getVal(TradeContextHolder.USER_ACCOUNT, UserAccountCardResponseDto.class);

        RechargeRequestDto rechargeRequestDto = new RechargeRequestDto();
        rechargeRequestDto.setTradeId(tradeNo);
        rechargeRequestDto.setAccountId(userAccount.getFundAccountId());
        rechargeRequestDto.setChannelId(requestDto.getTradeChannel());
        rechargeRequestDto.setPassword(requestDto.getTradePwd());
        return rechargeRequestDto;
    }
}
