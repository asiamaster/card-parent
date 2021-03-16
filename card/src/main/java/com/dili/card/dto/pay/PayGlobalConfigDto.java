package com.dili.card.dto.pay;

import com.dili.card.common.annotation.TextDisplay;
import com.dili.card.common.provider.FenToYuanProvider;

/**
 * <B>Description</B>
 * 本软件源代码版权归农丰时代及其团队所有,未经许可不得任意复制与传播
 * <B>农丰时代科技有限公司</B>
 *
 * @Description 支付全局参数
 * @author yangfan
 * @date 2021年3月9日
 */
public class PayGlobalConfigDto {
	private TradeDto trade;
	private WithdrawDto withdraw;
	private DepositDto deposit;
	private String mchId; 
	class TradeDto{
		@TextDisplay(FenToYuanProvider.class)
		private Long maxAmount;
		@TextDisplay(FenToYuanProvider.class)
		private Long dailyAmount;
		private Long dailyTimes;
		@TextDisplay(FenToYuanProvider.class)
		private Long monthlyAmount;
		public Long getMaxAmount() {
			return maxAmount;
		}
		public void setMaxAmount(Long maxAmount) {
			this.maxAmount = maxAmount;
		}
		public Long getDailyAmount() {
			return dailyAmount;
		}
		public void setDailyAmount(Long dailyAmount) {
			this.dailyAmount = dailyAmount;
		}
		public Long getDailyTimes() {
			return dailyTimes;
		}
		public void setDailyTimes(Long dailyTimes) {
			this.dailyTimes = dailyTimes;
		}
		public Long getMonthlyAmount() {
			return monthlyAmount;
		}
		public void setMonthlyAmount(Long monthlyAmount) {
			this.monthlyAmount = monthlyAmount;
		}
		 
	}
	class WithdrawDto{
		@TextDisplay(FenToYuanProvider.class)
		private Long maxAmount;
		@TextDisplay(FenToYuanProvider.class)
		private Long dailyAmount;
		private Long dailyTimes;
		@TextDisplay(FenToYuanProvider.class)
		private Long monthlyAmount;
		public Long getMaxAmount() {
			return maxAmount;
		}
		public void setMaxAmount(Long maxAmount) {
			this.maxAmount = maxAmount;
		}
		public Long getDailyAmount() {
			return dailyAmount;
		}
		public void setDailyAmount(Long dailyAmount) {
			this.dailyAmount = dailyAmount;
		}
		public Long getDailyTimes() {
			return dailyTimes;
		}
		public void setDailyTimes(Long dailyTimes) {
			this.dailyTimes = dailyTimes;
		}
		public Long getMonthlyAmount() {
			return monthlyAmount;
		}
		public void setMonthlyAmount(Long monthlyAmount) {
			this.monthlyAmount = monthlyAmount;
		}
		 
	}
	class DepositDto{
		@TextDisplay(FenToYuanProvider.class)
		private Long maxAmount;

		public Long getMaxAmount() {
			return maxAmount;
		}

		public void setMaxAmount(Long maxAmount) {
			this.maxAmount = maxAmount;
		}
		 
	}
	public TradeDto getTrade() {
		return trade;
	}
	public void setTrade(TradeDto trade) {
		this.trade = trade;
	}
	public WithdrawDto getWithdraw() {
		return withdraw;
	}
	public void setWithdraw(WithdrawDto withdraw) {
		this.withdraw = withdraw;
	}
	public DepositDto getDeposit() {
		return deposit;
	}
	public void setDeposit(DepositDto deposit) {
		this.deposit = deposit;
	}
	public String getMchId() {
		return mchId;
	}
	public void setMchId(String mchId) {
		this.mchId = mchId;
	}
	
	
}
