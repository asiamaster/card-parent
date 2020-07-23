package com.dili.card.service.withdraw;

import cn.hutool.core.collection.CollUtil;
import com.dili.card.dto.FundRequestDto;
import com.dili.card.exception.CardAppBizException;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 提现分发
 */
@Component
public class WithdrawDispatcher {

    @Resource
    private List<IWithdrawService> withdrawServiceList;

    private Map<Integer, IWithdrawService> withdrawServiceMap = new ConcurrentHashMap<>();

    @PostConstruct
    public void init() {
        if (CollUtil.isEmpty(withdrawServiceList)) {
            return;
        }
        for (IWithdrawService service : withdrawServiceList) {
            withdrawServiceMap.put(service.support(), service);
        }
    }
    /**
     * 提现分发
     * @param fundRequestDto
     */
    public void dispatch(FundRequestDto fundRequestDto) {
        if (fundRequestDto.getTradeChannel() == null) {
            throw new CardAppBizException("", "请选择提款方式");
        }
        IWithdrawService service = withdrawServiceMap.get(fundRequestDto.getTradeChannel());
        if (service == null) {
            throw new CardAppBizException("", "不支持该提款方式");
        }
        service.withdraw(fundRequestDto);
    }
}
