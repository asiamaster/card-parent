package com.dili.card.service.impl;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.dili.assets.sdk.dto.BusinessChargeItemDto;
import com.dili.assets.sdk.rpc.BusinessChargeItemRpc;
import com.dili.card.exception.CardAppBizException;
import com.dili.card.exception.ErrorCode;
import com.dili.card.rpc.resolver.GenericRpcResolver;
import com.dili.card.service.IRuleFeeService;
import com.dili.card.type.RuleFeeBusinessType;
import com.dili.card.type.SystemSubjectType;
import com.dili.rule.sdk.domain.input.QueryFeeInput;
import com.dili.rule.sdk.domain.output.QueryFeeOutput;
import com.dili.rule.sdk.rpc.ChargeRuleRpc;
import com.dili.ss.domain.BaseOutput;
import com.dili.uap.sdk.domain.UserTicket;
import com.dili.uap.sdk.session.SessionContext;

/**
 * @description： 规则服务统一处理
 * 
 * @author ：WangBo
 * @time ：2020年7月14日下午5:39:08
 */
@Service
public class RuleFeeServiceImpl implements IRuleFeeService {
	private static final Logger log = LoggerFactory.getLogger(RuleFeeServiceImpl.class);

	@Resource
	private ChargeRuleRpc chargeRuleRpc;
	@Resource
	private BusinessChargeItemRpc businessChargeItemRpc;

	/**
	 * 获取收费项
	 * 
	 * @param firmId
	 * @param type
	 * @return
	 */
	private List<BusinessChargeItemDto> getChargeItem(Long firmId, RuleFeeBusinessType type) {
		BusinessChargeItemDto businessChargeItemDto = new BusinessChargeItemDto();
		businessChargeItemDto.setBusinessType(type.getCode());
		businessChargeItemDto.setMarketId(firmId);
		BaseOutput<List<BusinessChargeItemDto>> businessChargeList = businessChargeItemRpc
				.listByExample(businessChargeItemDto);
		List<BusinessChargeItemDto> chargeItemList = GenericRpcResolver.resolver(businessChargeList, "获取费用项");
		return chargeItemList;
	}

	@Override
	public BigDecimal getRuleFee(Long amount, RuleFeeBusinessType ruleFeeBusinessType,
			SystemSubjectType systemSubjectType) {
		UserTicket userTicket = SessionContext.getSessionContext().getUserTicket();
		List<BusinessChargeItemDto> chargeItemList = getChargeItem(userTicket.getFirmId(), ruleFeeBusinessType);
		if (chargeItemList == null || chargeItemList.size() < 0) {
			log.info("业务类型[{}]未查询到收费项!", ruleFeeBusinessType.getCode());
			return BigDecimal.valueOf(0);
		}
		// 判断系统科目 是否是工本费，只取第一个
		QueryFeeInput queryFeeInput = null;
		for (BusinessChargeItemDto item : chargeItemList) {
			// SystemSubjectType.CARD_OPEN_COST == item.getSystemSubject
			if (systemSubjectType.getCode() == 2) {
				queryFeeInput = new QueryFeeInput();
				queryFeeInput.setMarketId(userTicket.getFirmId());
				queryFeeInput.setBusinessType(ruleFeeBusinessType.getCode());
				queryFeeInput.setChargeItem(item.getId());
			}
		}
		if (queryFeeInput == null) {
			throw new CardAppBizException(ErrorCode.GENERAL_CODE, "请在规则系统中配置 {}!", systemSubjectType.getName());
		}
		// 计算条件
		if (amount != null && amount != 0) {
			HashMap<String, Object> calcParams = new HashMap<String, Object>();
			calcParams.put("amount", amount);
			queryFeeInput.setCalcParams(calcParams);
		}
		BaseOutput<QueryFeeOutput> queryFee = chargeRuleRpc.queryFee(queryFeeInput);
		QueryFeeOutput resolver = GenericRpcResolver.resolver(queryFee, "开卡查询费用规则");
		return resolver.getTotalFee();
	}

	@Override
	public BigDecimal getRuleFee(RuleFeeBusinessType ruleFeeBusinessType, SystemSubjectType systemSubjectType) {
		return this.getRuleFee(null, ruleFeeBusinessType, systemSubjectType);
	}
}
