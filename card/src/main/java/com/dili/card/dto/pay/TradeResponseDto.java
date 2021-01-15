package com.dili.card.dto.pay;

import com.alibaba.fastjson.annotation.JSONField;
import com.dili.card.type.FeeType;
import com.dili.card.type.FundItem;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * 提现返回值
 *
 * @author xuliang
 */
public class TradeResponseDto {

	/** 账户ID */
	private Long accountId;
	/** 余额 */
	private Long balance;
	/** 发生金额 */
	private Long amount;
	/** 期初冻结余额 */
	private Long frozenBalance;
	/** 冻结/解冻金额 */
	private Long frozenAmount;
	/** 操作时间 */
	@JSONField(format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private LocalDateTime when;
	/** 流水明细 */
	private List<FeeItemDto> streams;
	/** 交易号 */
	private String tradeId;

	/**圈提状态 {@link com.dili.card.type.BankWithdrawState}*/
	private Integer state;

	/**
	 * 添加一个空资金项（有些地方没有手续费的时候需要加）
	 *
	 * @author miaoguoxin
	 * @date 2020/7/24
	 */
	public void addEmptyFeeItem(FundItem fundItem) {
		if (fundItem == null){
			return;
		}
		FeeItemDto feeItemDto = new FeeItemDto();
		feeItemDto.setType(fundItem.getCode());
		feeItemDto.setAmount(null);
		feeItemDto.setBalance(null);
		if (CollectionUtils.isEmpty(this.streams)) {
			this.streams = new ArrayList<>();
		}
		this.streams.add(feeItemDto);
	}

	/**
	 * 添加一个本金项（有些接口没有返回资金项）
	 *
	 * @author miaoguoxin
	 * @date 2020/7/27
	 */
	public void addVirtualPrincipalFundItem(Long amount) {
		if (CollectionUtils.isEmpty(this.streams)) {
			this.streams = new ArrayList<>();
		}
		boolean exist = this.streams.stream().anyMatch(feeItemDto -> feeItemDto.getType() == FeeType.ACCOUNT.getCode());
		if (!exist) {
			FeeItemDto feeItemDto = new FeeItemDto();
			feeItemDto.setType(FeeType.ACCOUNT.getCode());
			feeItemDto.setBalance(this.balance);
			feeItemDto.setAmount(amount);
			this.streams.add(feeItemDto);
		}
	}

	public Integer getState() {
		return state;
	}

	public void setState(Integer state) {
		this.state = state;
	}

	public Long getAccountId() {
		return accountId;
	}

	public void setAccountId(Long accountId) {
		this.accountId = accountId;
	}

	public Long getBalance() {
		return balance;
	}

	public void setBalance(Long balance) {
		this.balance = balance;
	}

	public Long getAmount() {
		return amount;
	}

	public void setAmount(Long amount) {
		this.amount = amount;
	}

	public Long getFrozenBalance() {
		return frozenBalance;
	}

	public void setFrozenBalance(Long frozenBalance) {
		this.frozenBalance = frozenBalance;
	}

	public Long getFrozenAmount() {
		return frozenAmount;
	}

	public void setFrozenAmount(Long frozenAmount) {
		this.frozenAmount = frozenAmount;
	}

	public LocalDateTime getWhen() {
		return when;
	}

	public void setWhen(LocalDateTime when) {
		this.when = when;
	}

	public List<FeeItemDto> getStreams() {
		return streams;
	}

	public void setStreams(List<FeeItemDto> streams) {
		this.streams = streams;
	}

	public String getTradeId() {
		return tradeId;
	}

	public void setTradeId(String tradeId) {
		this.tradeId = tradeId;
	}

	public Long countTotalFrozenAmount() {
		return this.frozenBalance != null && this.frozenAmount != null ? this.frozenBalance + this.frozenAmount : null;
	}
}
