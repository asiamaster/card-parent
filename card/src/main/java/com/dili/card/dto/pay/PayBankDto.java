package com.dili.card.dto.pay;

/**
 * @description： 支付银行卡相关参数 <br>
 * <i>命名与卡务有区别
 * 
 * @author ：WangBo
 * @time ：2021年1月8日下午3:15:16
 */
public class PayBankDto {

	/** 银行卡号 */
	private String cardNo;

	/** 银行渠道ID,银行类型码 */
	private Integer channelId;

	/** 银行名称 */
	private Integer channelName;

	public String getCardNo() {
		return cardNo;
	}

	public void setCardNo(String cardNo) {
		this.cardNo = cardNo;
	}

	public Integer getChannelId() {
		return channelId;
	}

	public void setChannelId(Integer channelId) {
		this.channelId = channelId;
	}

	public Integer getChannelName() {
		return channelName;
	}

	public void setChannelName(Integer channelName) {
		this.channelName = channelName;
	}

}
