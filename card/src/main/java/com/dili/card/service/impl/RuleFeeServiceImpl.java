package com.dili.card.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.dili.assets.sdk.dto.BusinessChargeItemDto;
import com.dili.assets.sdk.rpc.BusinessChargeItemRpc;
import com.dili.card.common.constant.ServiceName;
import com.dili.card.exception.CardAppBizException;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.List;

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
        BaseOutput<List<BusinessChargeItemDto>> businessChargeList = null;
        try {
            businessChargeList = businessChargeItemRpc.listByExample(businessChargeItemDto);
        } catch (Exception e) {
            log.error("收费项查询失败", e);
            throw e;
        }
        log.info("收费项>" + JSONObject.toJSONString(businessChargeList));
        List<BusinessChargeItemDto> chargeItemList = GenericRpcResolver.resolver(businessChargeList,
                ServiceName.ASSETS);
        return chargeItemList;
    }

    @Override
    public BigDecimal getRuleFee(Long amount, RuleFeeBusinessType ruleFeeBusinessType,
                                 SystemSubjectType systemSubjectType) {
        UserTicket userTicket = SessionContext.getSessionContext().getUserTicket();
        List<BusinessChargeItemDto> chargeItemList = getChargeItem(userTicket.getFirmId(), ruleFeeBusinessType);
        log.info("费用项返回>" + JSONObject.toJSONString(chargeItemList));
        if (chargeItemList == null || chargeItemList.size() < 0) {
            log.info("业务类型[{}]未查询到收费项!", ruleFeeBusinessType.getCode());
            return BigDecimal.valueOf(0);
        }
        BaseOutput<QueryFeeOutput> queryFee = null;
        try {
            // 判断系统科目 是否是工本费，只取第一个
            QueryFeeInput queryFeeInput = null;
            for (BusinessChargeItemDto item : chargeItemList) {
                if (Integer.valueOf(systemSubjectType.getCode()).equals(item.getSystemSubject())) {
                    queryFeeInput = new QueryFeeInput();
                    queryFeeInput.setMarketId(userTicket.getFirmId());
                    queryFeeInput.setBusinessType(ruleFeeBusinessType.getCode());
                    queryFeeInput.setChargeItem(item.getId());
                    break;
                }
            }
            if (queryFeeInput == null) {
                throw new CardAppBizException("请在规则系统中配置" + systemSubjectType.getName() + " ,并选择对应的系统科目!");
            }
            // 计算条件及判断条件，金额由分改为元避免计算公式里面有加减法时数据错误
            if (amount != null && amount != 0) {
                BigDecimal b = new BigDecimal(amount);
                BigDecimal div = b.divide(new BigDecimal(100), 2, RoundingMode.HALF_UP);
                HashMap<String, Object> calcParams = new HashMap<String, Object>();
//			String amountStr = CurrencyUtils.cent2TenNoSymbol(amount);
                calcParams.put("amount", div.doubleValue());
                queryFeeInput.setCalcParams(calcParams);

                HashMap<String, Object> conditionParams = new HashMap<String, Object>();
                conditionParams.put("amount", div.doubleValue());
                queryFeeInput.setConditionParams(conditionParams);
            }

            log.info("查询费用>" + JSONObject.toJSONString(queryFeeInput));
            queryFee = chargeRuleRpc.queryFee(queryFeeInput);
        } catch (Exception e) {
            log.error("规则费用查询失败", e);
            throw e;
        }
        log.info("费用>" + JSONObject.toJSONString(queryFee));
        QueryFeeOutput resolver = GenericRpcResolver.resolver(queryFee, ServiceName.RULE);
        return resolver.getTotalFee();
    }

    @Override
    public BigDecimal getRuleFee(RuleFeeBusinessType ruleFeeBusinessType, SystemSubjectType systemSubjectType) {
        return this.getRuleFee(null, ruleFeeBusinessType, systemSubjectType);
    }
}
