package com.dili.card.dto;

import com.dili.card.dto.pay.BalanceResponseDto;
import com.dili.card.dto.pay.MerAccountResponseDto;
import com.dili.uap.sdk.domain.Firm;

import java.io.Serializable;

/**
 * @Auther: miaoguoxin
 * @Date: 2021/1/22 15:55
 * @Description:
 */
public class FirmWithdrawInitResponseDto implements Serializable {
    /**市场信息*/
    private Firm firm;
    /**市场商户信息*/
    private MerAccountResponseDto merInfo;
    /**市场账户余额信息*/
    private BalanceResponseDto balanceInfo;

    public BalanceResponseDto getBalanceInfo() {
        return balanceInfo;
    }

    public void setBalanceInfo(BalanceResponseDto balanceInfo) {
        this.balanceInfo = balanceInfo;
    }

    public Firm getFirm() {
        return firm;
    }

    public void setFirm(Firm firm) {
        this.firm = firm;
    }

    public MerAccountResponseDto getMerInfo() {
        return merInfo;
    }

    public void setMerInfo(MerAccountResponseDto merInfo) {
        this.merInfo = merInfo;
    }

}


