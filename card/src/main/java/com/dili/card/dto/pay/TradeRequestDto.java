package com.dili.card.dto.pay;

import com.dili.card.dto.UserAccountCardResponseDto;
import com.dili.card.type.FundItem;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 提现
 * @author xuliang
 */
public class TradeRequestDto {

    /** 交易号*/
    private String tradeId;
    /** 账户ID*/
    private Long accountId;
    /** 交易渠道*/
    private Integer channelId;
    /** 密码*/
    private String password;
    /** 费用项*/
    private List<FeeItemDto> fees;
    /** 业务账号ID*/
    private Long businessId;
    /**金额*/
    private Long amount;

    public static TradeRequestDto createTrade(UserAccountCardResponseDto userAccount, String tradeNo, Integer channelId, String password) {
        TradeRequestDto tradeRequestDto = new TradeRequestDto();
        tradeRequestDto.setTradeId(tradeNo);
        tradeRequestDto.setBusinessId(userAccount.getAccountId());
        tradeRequestDto.setAccountId(userAccount.getFundAccountId());
        tradeRequestDto.setChannelId(channelId);
        tradeRequestDto.setPassword(password);
        return tradeRequestDto;
    }




    /**
     * 添加手续费
     * @author miaoguoxin
     * @date 2020/7/6
     */
    public void addServiceFeeItem(Long serviceCost, FundItem fundItem) {
        if (CollectionUtils.isEmpty(this.fees)) {
            this.fees = new ArrayList<>();
        }
        FeeItemDto feeItem = new FeeItemDto();
        feeItem.setAmount(serviceCost);
        feeItem.setType(fundItem.getCode());
        feeItem.setTypeName(fundItem.getName());
        this.fees.add(feeItem);
    }

    public Long getAmount() {
        return amount;
    }

    public void setAmount(Long amount) {
        this.amount = amount;
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

    public Integer getChannelId() {
        return channelId;
    }

    public void setChannelId(Integer channelId) {
        this.channelId = channelId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<FeeItemDto> getFees() {
        return fees;
    }

    public void setFees(List<FeeItemDto> fees) {
        this.fees = fees;
    }

    public Long getBusinessId() {
        return businessId;
    }

    public void setBusinessId(Long businessId) {
        this.businessId = businessId;
    }
}
