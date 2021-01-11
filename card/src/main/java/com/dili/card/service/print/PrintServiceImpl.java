package com.dili.card.service.print;

import com.dili.card.dto.PrintDto;
import com.dili.card.dto.UserAccountCardResponseDto;
import com.dili.card.dto.UserAccountSingleQueryDto;
import com.dili.card.dto.pay.BalanceResponseDto;
import com.dili.card.entity.BusinessRecordDo;
import com.dili.card.exception.CardAppBizException;
import com.dili.card.rpc.resolver.AccountQueryRpcResolver;
import com.dili.card.rpc.resolver.PayRpcResolver;
import com.dili.card.type.CardStatus;
import com.dili.card.type.OperateType;
import com.dili.card.type.TradeChannel;
import com.dili.card.util.CurrencyUtils;
import com.dili.card.util.DateUtil;

import cn.hutool.core.util.StrUtil;

import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 打印基础实现类
 */
public abstract class PrintServiceImpl implements IPrintService {

	@Autowired
	private PayRpcResolver payRpcResolver;
	@Autowired
	private AccountQueryRpcResolver accountQueryRpcResolver;

	@Override
	public Map<String, Object> create(BusinessRecordDo recordDo, boolean reprint) {
		Map<String, Object> result = new ConcurrentHashMap<>();
		result.put("template", getPrintTemplate(recordDo));
		PrintDto printDto = new PrintDto();
		printDto.setName(OperateType.getName(recordDo.getType()));
		printDto.setOperateTime(DateUtil.formatDateTime(recordDo.getOperateTime(), "yyyy-MM-dd HH:mm:ss"));
		printDto.setReprint(reprint ? "(补打)" : "");
		printDto.setCustomerName(recordDo.getCustomerName());
		String cardNo = recordDo.getCardNo();
		printDto.setCardNo(cardNo);
		String endNo = StrUtil.sub(cardNo, cardNo.length()-4, cardNo.length());
		printDto.setCardNoCipher(StrUtil.sub(cardNo, 1, 4) + "****" + endNo);
		printDto.setAmount(CurrencyUtils.cent2TenNoSymbol(recordDo.getAmount()));
		// 根据需求实时获取最新余额 2020-10-19
		printDto.setBalance(CurrencyUtils.cent2TenNoSymbol(queryTotalBalance(recordDo.getAccountId())));
		printDto.setTradeChannel(
				recordDo.getTradeChannel() != null ? TradeChannel.getNameByCode(recordDo.getTradeChannel()) : "");
		printDto.setDeposit(CurrencyUtils.cent2TenNoSymbol(recordDo.getDeposit()));
		printDto.setCardCost(CurrencyUtils.cent2TenNoSymbol(recordDo.getCardCost()));
		printDto.setServiceCost(CurrencyUtils.cent2TenNoSymbol(recordDo.getServiceCost()));
		printDto.setOperatorNo(recordDo.getOperatorNo());
		printDto.setOperatorName(recordDo.getOperatorName());
		printDto.setNotes(recordDo.getNotes());
		printDto.setSerialNo(recordDo.getSerialNo());
		printDto.setFirmName(recordDo.getFirmName());
		printDto.setHoldName(recordDo.getHoldName());
		createSpecial(printDto, recordDo, reprint);
		result.put("data", printDto);
		return result;
	}

	/**
	 * 根据卡账户ID查询余额
	 * 
	 * @param accountId
	 * @return
	 */
	private Long queryTotalBalance(Long accountId) {
		UserAccountCardResponseDto account = null;
		try {
			UserAccountSingleQueryDto userAccountSingleQuery = new UserAccountSingleQueryDto();
			userAccountSingleQuery.setAccountId(accountId);
			account = accountQueryRpcResolver
					.findSingleWithoutValidate(userAccountSingleQuery);
			if (account == null) {
				return 0L;
			}
			if (account.getCardState() == CardStatus.RETURNED.getCode()) {
				return 0L;
			}
		} catch (CardAppBizException e) {
			// TODO优化
			if ("10001".equals(e.getCode())) {
				return 0L;
			}
		}
		BalanceResponseDto balanceResponse = payRpcResolver.findBalanceByFundAccountId(account.getFundAccountId());
		return balanceResponse != null ? balanceResponse.getBalance() : 0L;
	}
}
