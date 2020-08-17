package com.dili.card.dto;

import com.dili.card.dto.pay.BalanceResponseDto;

import java.io.Serializable;
import java.util.List;

/**
 * 账户信息（包含余额）
 * @Auther: miaoguoxin
 * @Date: 2020/7/7 15:21
 */
public class AccountSimpleResponseDto implements Serializable {
    /** */
	private static final long serialVersionUID = 7421319499161686882L;
	/**账户资金信息*/
    private BalanceResponseDto accountFund;
    /**账户信息*/
    private UserAccountCardResponseDto accountInfo;
    /**关联卡信息*/
    private List<UserAccountCardResponseDto> associationAccounts;

    public AccountSimpleResponseDto(BalanceResponseDto accountFund, UserAccountCardResponseDto accountInfo) {
        this.accountFund = accountFund;
        this.accountInfo = accountInfo;
    }

    public AccountSimpleResponseDto(BalanceResponseDto accountFund, UserAccountCardResponseDto accountInfo, List<UserAccountCardResponseDto> associationAccounts) {
        this.accountFund = accountFund;
        this.accountInfo = accountInfo;
        this.associationAccounts = associationAccounts;
    }

    public BalanceResponseDto getAccountFund() {
        return accountFund;
    }

    public void setAccountFund(BalanceResponseDto accountFund) {
        this.accountFund = accountFund;
    }

    public UserAccountCardResponseDto getAccountInfo() {
        return accountInfo;
    }

    public void setAccountInfo(UserAccountCardResponseDto accountInfo) {
        this.accountInfo = accountInfo;
    }

    public List<UserAccountCardResponseDto> getAssociationAccounts() {
        return associationAccounts;
    }

    public void setAssociationAccounts(List<UserAccountCardResponseDto> associationAccounts) {
        this.associationAccounts = associationAccounts;
    }
}

