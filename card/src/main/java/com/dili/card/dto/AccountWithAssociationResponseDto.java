package com.dili.card.dto;

import com.dili.card.dto.pay.BalanceResponseDto;

import java.io.Serializable;
import java.util.List;

/**
 * @Auther: miaoguoxin
 * @Date: 2020/6/28 10:17
 * @Description: 卡账户关联
 */
public class AccountWithAssociationResponseDto implements Serializable {
    /** */
	private static final long serialVersionUID = 4114799253667262855L;
	/**根据卡号查出来的主信息*/
    private UserAccountCardResponseDto primary;
    /**关联卡信息*/
    private List<UserAccountCardResponseDto> association;
    /**卡账户资金*/
    private BalanceResponseDto accountFund;

    public AccountWithAssociationResponseDto() {
    }

    public AccountWithAssociationResponseDto(UserAccountCardResponseDto primary, List<UserAccountCardResponseDto> association) {
        this.primary = primary;
        this.association = association;
    }

    public AccountWithAssociationResponseDto(UserAccountCardResponseDto primary, List<UserAccountCardResponseDto> association, BalanceResponseDto accountFund) {
        this.primary = primary;
        this.association = association;
        this.accountFund = accountFund;
    }

    public BalanceResponseDto getAccountFund() {
        return accountFund;
    }

    public void setAccountFund(BalanceResponseDto accountFund) {
        this.accountFund = accountFund;
    }

    public UserAccountCardResponseDto getPrimary() {
        return primary;
    }

    public void setPrimary(UserAccountCardResponseDto primary) {
        this.primary = primary;
    }

    public List<UserAccountCardResponseDto> getAssociation() {
        return association;
    }

    public void setAssociation(List<UserAccountCardResponseDto> association) {
        this.association = association;
    }
}
