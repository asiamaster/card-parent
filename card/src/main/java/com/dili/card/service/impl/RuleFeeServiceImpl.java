package com.dili.card.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

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
		if(chargeItemList == null || chargeItemList.size() < 0) {
			throw new CardAppBizException(ErrorCode.GENERAL_CODE, "开卡没有配置任何收费项！");
		}
		// 只收取工本费
		List<QueryFeeInput> feeInputs = new ArrayList<QueryFeeInput>();
		for (BusinessChargeItemDto item : chargeItemList) {
			// 判断系统科目 是否是工本费，只取第一个
			// systemSubject == SystemSubjectType.CARD_OPEN_COST
			if (SystemSubjectType.CARD_OPEN_COST.getCode() == 2) {
				QueryFeeInput queryFeeInput = new QueryFeeInput();
				queryFeeInput.setMarketId(firmId);
				queryFeeInput.setBusinessType("CARD_OPEN_CARD");
				queryFeeInput.setChargeItem(item.getId());
				feeInputs.add(queryFeeInput);
			}
		}
		if(feeInputs.size() == 0) {
			throw new CardAppBizException(ErrorCode.GENERAL_CODE, "请在规则系统中配置开卡工本费！");
		}
		BaseOutput<List<QueryFeeOutput>> batchQueryFee = chargeRuleRpc.batchQueryFee(feeInputs);
		List<QueryFeeOutput> list = GenericRpcResolver.resolver(batchQueryFee, "开卡查询费用规则");
		return null;
	}

	/**
	 * 获取收费项
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
}
