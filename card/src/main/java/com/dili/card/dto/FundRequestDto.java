package com.dili.card.dto;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import com.alibaba.fastjson.JSONObject;
import com.dili.card.dto.pay.ChannelAccountRequestDto;
import com.dili.card.validator.FundValidator;

/**
 * 用于资金操作相关dto
 * 
 * @author xuliang
 */
public class FundRequestDto extends CardRequestDto {

	/** */
	private static final long serialVersionUID = 7634360360368892537L;
	/** 交易渠道 */
	@NotNull(message = "交易渠道不能为空", groups = FundValidator.Trade.class)
	private Integer tradeChannel;
	/** 金额 */
	@NotNull(message = "金额不能为空", groups = { FundValidator.Trade.class })
	@Min(value = 1, message = "最少0.01元", groups = { FundValidator.Trade.class })
	@Max(value = 999999999L, message = "最多9999999.99元", groups = { FundValidator.Trade.class })
	private Long amount;
	/** 交易密码 */
	private String tradePwd;
	/** 手续费 */
	// @Min(value = 1, message = "手续费最少0.01元", groups = FundValidator.Trade.class)
	private Long serviceCost;
	/** 备注 */
	private String mark;
	/** 额外字段，由各业务逻辑自行决定 */
	private JSONObject extra;
	/** 委托合同编号 */
	private String contractNo;
	/** 委托人ID */
	private Long consignorId;
	/** 委托人姓名 */
	private String consignorName;
	/** 渠道账号,网银需要 */
	private ChannelAccountRequestDto channelAccount;
	/** 圈提选择的市场ID */
	private Long withdrawFirmId;
	/** 绑定银行卡ID */
	private Long bindBankCardId;
	/** 市场主帐号 */
	private Long profitAccount;

	public ChannelAccountRequestDto getChannelAccount() {
		return channelAccount;
	}

	public void setChannelAccount(ChannelAccountRequestDto channelAccount) {
		this.channelAccount = channelAccount;
	}

	public Integer getTradeChannel() {
		return tradeChannel;
	}

	public void setTradeChannel(Integer tradeChannel) {
		this.tradeChannel = tradeChannel;
	}

	public Long getAmount() {
		return amount;
	}

	public void setAmount(Long amount) {
		this.amount = amount;
	}

	public String getTradePwd() {
		return tradePwd;
	}

	public void setTradePwd(String tradePwd) {
		this.tradePwd = tradePwd;
	}

	public Long getServiceCost() {
		return serviceCost;
	}

	public void setServiceCost(Long serviceCost) {
		this.serviceCost = serviceCost;
	}

	public String getMark() {
		return mark;
	}

	public void setMark(String mark) {
		this.mark = mark;
	}

	public JSONObject getExtra() {
		return extra;
	}

	public void setExtra(JSONObject extra) {
		this.extra = extra;
	}

	public String getContractNo() {
		return contractNo;
	}

	public void setContractNo(String contractNo) {
		this.contractNo = contractNo;
	}

	public Long getConsignorId() {
		return consignorId;
	}

	public void setConsignorId(Long consignorId) {
		this.consignorId = consignorId;
	}

	public String getConsignorName() {
		return consignorName;
	}

	public void setConsignorName(String consignorName) {
		this.consignorName = consignorName;
	}

	public Long getWithdrawFirmId() {
		return withdrawFirmId;
	}

	public void setWithdrawFirmId(Long withdrawFirmId) {
		this.withdrawFirmId = withdrawFirmId;
	}

	public Long getProfitAccount() {
		return profitAccount;
	}

	public void setProfitAccount(Long profitAccount) {
		this.profitAccount = profitAccount;
	}

	public Long getBindBankCardId() {
		return bindBankCardId;
	}

	public void setBindBankCardId(Long bindBankCardId) {
		this.bindBankCardId = bindBankCardId;
	}

}
