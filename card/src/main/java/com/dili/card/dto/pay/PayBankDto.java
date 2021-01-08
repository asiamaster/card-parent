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
	private String channelName;

	/** 开户行名称 */
	private String bankName;

	private Long mchId;

	/** 关键字搜索 */
	private String keyword;

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

	public String getChannelName() {
		return channelName;
	}

	public void setChannelName(String channelName) {
		this.channelName = channelName;
	}

	public String getKeyword() {
		return keyword;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}

	public String getBankName() {
		return bankName;
	}

	public void setBankName(String bankName) {
		this.bankName = bankName;
	}

	public Long getMchId() {
		return mchId;
	}

	public void setMchId(Long mchId) {
		this.mchId = mchId;
	}

}
