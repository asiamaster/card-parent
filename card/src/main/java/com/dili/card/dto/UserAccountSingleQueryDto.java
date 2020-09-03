package com.dili.card.dto;

import java.io.Serializable;

/**
 * 单个查询账户dto
 * @Auther: miaoguoxin
 * @Date: 2020/8/10 13:45
 */
public class UserAccountSingleQueryDto implements Serializable {
    /** */
	private static final long serialVersionUID = 3420897440951735189L;
	/**账户ID */
    private Long accountId;
    /** 卡号 */
    private String cardNo;

    public Long getAccountId() {
        return accountId;
    }

    public void setAccountId(Long accountId) {
        this.accountId = accountId;
    }

    public String getCardNo() {
        return cardNo;
    }

    public void setCardNo(String cardNo) {
        this.cardNo = cardNo;
    }
    
    public static UserAccountSingleQueryDto newDto(Long accountId) {
    	UserAccountSingleQueryDto userAccountSingleQueryDto = new UserAccountSingleQueryDto();
    	userAccountSingleQueryDto.setAccountId(accountId);
    	return userAccountSingleQueryDto;
    }
}
