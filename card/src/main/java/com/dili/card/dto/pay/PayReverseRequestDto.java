package com.dili.card.dto.pay;

import com.dili.card.dto.UserAccountCardResponseDto;
import com.dili.card.type.FundItem;

import java.io.Serializable;

/**
 * @Auther: miaoguoxin
 * @Date: 2021/1/7 13:44
 * @Description: 支付冲正请求
 */
public class PayReverseRequestDto implements Serializable {
    /** 交易号*/
    private String tradeId;
    /** 账户ID*/
    private Long accountId;
    /**金额*/
    private Long amount;
    /** 费用项*/
    private FeeItemDto fee;

    public static PayReverseRequestDto createReverse(UserAccountCardResponseDto userAccount, String tradeNo, Long amount) {
        PayReverseRequestDto tradeRequestDto = new PayReverseRequestDto();
        tradeRequestDto.setTradeId(tradeNo);
        tradeRequestDto.setAccountId(userAccount.getFundAccountId());
        tradeRequestDto.setAmount(amount);
        return tradeRequestDto;
    }

    /**
     * 添加手续费
     * @author miaoguoxin
     * @date 2020/7/6
     */
    public void addFee(Long serviceCost, FundItem fundItem) {
        if (fundItem == null){
            return;
        }
        if (this.fee == null) {
            this.fee = new FeeItemDto();
        }
        this.fee.setAmount(serviceCost);
        this.fee.setType(fundItem.getCode());
        this.fee.setTypeName(fundItem.getName());
    }

    public String getTradeId() {
        return tradeId;
    }

    public void setTradeId(String tradeId) {
        this.tradeId = tradeId;
    }

    public Long getAccountId() {
        return accountId;
    }

    public void setAccountId(Long accountId) {
        this.accountId = accountId;
    }

    public Long getAmount() {
        return amount;
    }

    public void setAmount(Long amount) {
        this.amount = amount;
    }

    public FeeItemDto getFee() {
        return fee;
    }

    public void setFee(FeeItemDto fee) {
        this.fee = fee;
    }
}
