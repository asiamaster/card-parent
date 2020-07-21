package com.dili.card.service.recharge;

import com.dili.card.common.constant.Constant;
import com.dili.card.dto.FundRequestDto;
import com.dili.card.dto.SerialDto;
import com.dili.card.dto.UserAccountCardResponseDto;
import com.dili.card.dto.pay.CreateTradeRequestDto;
import com.dili.card.dto.pay.TradeRequestDto;
import com.dili.card.dto.pay.TradeResponseDto;
import com.dili.card.entity.BusinessRecordDo;
import com.dili.card.entity.SerialRecordDo;
import com.dili.card.service.IPayService;
import com.dili.card.service.ISerialService;
import com.dili.card.type.ActionType;
import com.dili.card.type.OperateType;
import com.dili.card.type.TradeType;
import com.dili.card.util.CurrencyUtils;
import com.dili.tcc.core.TccContextHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.stream.Collectors;

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
    public final void doPreRecharge(FundRequestDto requestDto) {
        //真正充值的金额
        Long rechargeAmount = this.getRechargeAmount(requestDto);
        BusinessRecordDo businessRecord = new BusinessRecordDo();
        serialService.buildCommonInfo(requestDto, businessRecord);

        UserAccountCardResponseDto userAccount = TccContextHolder.get().getAttr(Constant.USER_ACCOUNT, UserAccountCardResponseDto.class);
        CreateTradeRequestDto tradeRequest = CreateTradeRequestDto.createTrade(
                TradeType.DEPOSIT.getCode(),
                userAccount.getAccountId(),
                userAccount.getFundAccountId(),
                rechargeAmount, businessRecord.getSerialNo(),
                String.valueOf(businessRecord.getCycleNo()));
        //创建交易
        String tradeId = payService.createTrade(tradeRequest);
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
        TccContextHolder.get().addAttr(Constant.TRADE_ID_KEY, tradeId);
        TccContextHolder.get().addAttr(Constant.BUSINESS_RECORD_KEY, businessRecord);
    }

    /**
     * 充值操作入口封装
     * @author miaoguoxin
     * @date 2020/7/2
     */
    public final void doRecharge(FundRequestDto requestDto) {
        BusinessRecordDo record = TccContextHolder.get().getAttr(Constant.BUSINESS_RECORD_KEY, BusinessRecordDo.class);
        TradeRequestDto dto = this.buildTradeRequest(requestDto);
        //务必设置bizId
        dto.setBusinessId(record.getAccountId());
        TradeResponseDto tradeResponseDto = payService.commitTrade(dto);
        this.afterCommitRecharge(requestDto);
        //记录日志
        try {
            SerialDto serialDto = this.buildRechargeSerial(requestDto, record, tradeResponseDto);
            serialService.handleSuccess(serialDto);
        } catch (Exception e) {
            LOGGER.error("recharge", e);
        }
    }

    /**
    * 提交充值请求之后的一些逻辑
    * @author miaoguoxin
    * @date 2020/7/20
    */
    protected void afterCommitRecharge(FundRequestDto requestDto){

    }

    /**
     * 构建充值流水数据
     * @author miaoguoxin
     * @date 2020/7/8
     */
    protected SerialDto buildRechargeSerial(FundRequestDto fundRequestDto, BusinessRecordDo businessRecord, TradeResponseDto tradeResponseDto) {
        SerialDto serialDto = new SerialDto();
        serialDto.setSerialNo(businessRecord.getSerialNo());
        serialDto.setStartBalance(tradeResponseDto.getBalance());
        serialDto.setEndBalance(tradeResponseDto.getBalance() + tradeResponseDto.getAmount());
        if (!CollectionUtils.isEmpty(tradeResponseDto.getStreams())) {
            List<SerialRecordDo> serialRecordDos = tradeResponseDto.getStreams().stream().map(item -> {
                SerialRecordDo recordDo = new SerialRecordDo();
                serialService.copyCommonFields(recordDo, businessRecord);
                recordDo.setAction(item.getAmount() < 0L ? ActionType.EXPENSE.getCode() : ActionType.INCOME.getCode());
                recordDo.setStartBalance(item.getBalance());
                recordDo.setAmount(Math.abs(item.getAmount()));
                recordDo.setEndBalance(item.getBalance() + item.getAmount());
                recordDo.setFundItem(item.getType());
                recordDo.setFundItemName(item.getTypeName());
                recordDo.setOperateTime(tradeResponseDto.getWhen());
                return recordDo;
            }).collect(Collectors.toList());
            serialDto.setSerialRecordList(serialRecordDos);
        }
        return serialDto;
    }


    protected TradeRequestDto createRechargeRequestDto(FundRequestDto requestDto) {
        String tradeNo = TccContextHolder.get().getAttr(Constant.TRADE_ID_KEY, String.class);
        UserAccountCardResponseDto userAccount = TccContextHolder.get().getAttr(Constant.USER_ACCOUNT, UserAccountCardResponseDto.class);

        TradeRequestDto rechargeRequestDto = new TradeRequestDto();
        rechargeRequestDto.setTradeId(tradeNo);
        rechargeRequestDto.setAccountId(userAccount.getFundAccountId());
        rechargeRequestDto.setChannelId(requestDto.getTradeChannel());
        rechargeRequestDto.setPassword(requestDto.getTradePwd());
        return rechargeRequestDto;
    }

}
