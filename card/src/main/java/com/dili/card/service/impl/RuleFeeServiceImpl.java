package com.dili.card.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.dili.assets.sdk.dto.BusinessChargeItemDto;
import com.dili.assets.sdk.rpc.BusinessChargeItemRpc;
import com.dili.card.rpc.resolver.GenericRpcResolver;
import com.dili.card.service.IRuleFeeService;
import com.dili.card.type.RuleFeeBusinessType;
import com.dili.rule.sdk.domain.input.QueryFeeInput;
import com.dili.rule.sdk.domain.output.QueryFeeOutput;
import com.dili.rule.sdk.rpc.ChargeRuleRpc;
import com.dili.ss.domain.BaseOutput;

/**
 * @description： 规则服务统一处理
 * 
 * @author ：WangBo
 * @time ：2020年7月14日下午5:39:08
 */
@Service
public class RuleFeeServiceImpl implements IRuleFeeService {

	@Resource
	private ChargeRuleRpc chargeRuleRpc;
	@Resource
	private BusinessChargeItemRpc businessChargeItemRpc;

	@Override
	public Long getOpenCardFee(Long firmId) {
		List<BusinessChargeItemDto> chargeItemList = getChargeItem(firmId, RuleFeeBusinessType.CARD_OPEN_CARD);
		List<QueryFeeInput> feeInputs = new ArrayList<QueryFeeInput>();
		for (BusinessChargeItemDto item : chargeItemList) {
			QueryFeeInput queryFeeInput = new QueryFeeInput();
			queryFeeInput.setMarketId(firmId);
			queryFeeInput.setBusinessType("CARD_OPEN_CARD");
			queryFeeInput.setChargeItem(item.getId());
			feeInputs.add(queryFeeInput);
		}
		BaseOutput<List<QueryFeeOutput>> batchQueryFee = chargeRuleRpc.batchQueryFee(feeInputs);
		List<QueryFeeOutput> list = GenericRpcResolver.resolver(batchQueryFee, "开卡查询费用规则");
		return null;
	}

	private List<BusinessChargeItemDto> getChargeItem(Long firmId, RuleFeeBusinessType type) {
		BusinessChargeItemDto businessChargeItemDto = new BusinessChargeItemDto();
		businessChargeItemDto.setBusinessType(type.getCode());
		businessChargeItemDto.setMarketId(firmId);
		BaseOutput<List<BusinessChargeItemDto>> businessChargeList = businessChargeItemRpc
				.listByExample(businessChargeItemDto);
		List<BusinessChargeItemDto> chargeItemList = GenericRpcResolver.resolver(businessChargeList, "获取费用项");
		return chargeItemList;
	}
}
