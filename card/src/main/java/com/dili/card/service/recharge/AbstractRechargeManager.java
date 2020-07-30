package com.dili.card.service.recharge;

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
import com.dili.card.type.TradeType;
import com.dili.tcc.core.TccContextHolder;
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
     *  预创建充值订单
     * @author miaoguoxin
     * @date 2020/7/6
     */
    public final void doPreRecharge(FundRequestDto requestDto) {
        UserAccountCardResponseDto userAccount = accountQueryService.getByAccountId(requestDto);

        //真正充值的金额
        Long rechargeAmount = this.getRechargeAmount(requestDto);
        BusinessRecordDo businessRecord = serialService.createBusinessRecord(requestDto, userAccount, record -> {
            record.setType(OperateType.ACCOUNT_CHARGE.getCode());
            record.setAmount(requestDto.getAmount());
            record.setTradeType(TradeType.DEPOSIT.getCode());
            record.setTradeChannel(requestDto.getTradeChannel());
            record.setServiceCost(requestDto.getServiceCost());
        });

        CreateTradeRequestDto tradeRequest = CreateTradeRequestDto.createTrade(
                TradeType.DEPOSIT.getCode(),
                userAccount.getAccountId(),
                userAccount.getFundAccountId(),
                rechargeAmount,
                businessRecord.getSerialNo(),
                String.valueOf(businessRecord.getCycleNo()));
        //创建交易
        String tradeId = payService.createTrade(tradeRequest);
        //保存业务办理记录
        businessRecord.setTradeNo(tradeId);
        businessRecord.setNotes(this.buildBusinessRecordNote(requestDto));
        serialService.saveBusinessRecord(businessRecord);
        //保存线程变量
        TccContextHolder.get().addAttr(Constant.TRADE_ID_KEY, tradeId);
        TccContextHolder.get().addAttr(Constant.BUSINESS_RECORD_KEY, businessRecord);
        TccContextHolder.get().addAttr(Constant.USER_ACCOUNT, userAccount);
    }

    /**
     * 充值操作入口封装
     * @author miaoguoxin
     * @date 2020/7/2
     */
    public final void doRecharge(FundRequestDto requestDto) {
        FundItem serviceCostItem = this.getServiceCostItem(requestDto);
        BusinessRecordDo record = TccContextHolder.get().getAttr(Constant.BUSINESS_RECORD_KEY, BusinessRecordDo.class);
        String tradeNo = TccContextHolder.get().getAttr(Constant.TRADE_ID_KEY, String.class);
        UserAccountCardResponseDto userAccount = TccContextHolder.get().getAttr(Constant.USER_ACCOUNT, UserAccountCardResponseDto.class);

        TradeRequestDto dto = TradeRequestDto.createTrade(userAccount, tradeNo, requestDto.getTradeChannel(), requestDto.getLoginPwd());
        if (serviceCostItem != null && requestDto.getServiceCost() != null) {
            dto.addServiceFeeItem(requestDto.getServiceCost(), serviceCostItem);
        }

        TradeResponseDto tradeResponseDto = payService.commitTrade(dto);
        //没有手续费的时候需要添加一个空项
        if (serviceCostItem != null && this.canAddEmptyFundItem(requestDto)) {
            tradeResponseDto.addEmptyFeeItem(serviceCostItem);
        }
        this.afterCommitRecharge(requestDto);
        FundItem principalFundItem = this.getPrincipalFundItem(requestDto);
        //记录日志
        try {
            SerialDto serialDto = serialService.createAccountSerialWithFund(record, tradeResponseDto, (serialRecord, feeType) -> {
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
        } catch (Exception e) {
            LOGGER.error("recharge", e);
        }
    }

    /**
     * 提交充值请求之后的一些逻辑
     * @author miaoguoxin
     * @date 2020/7/20
     */
    protected void afterCommitRecharge(FundRequestDto requestDto) {

    }
}
