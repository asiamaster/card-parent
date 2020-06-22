package com.dili.card.rpc;

import com.dili.card.dto.CardRequestDto;
import com.dili.card.entity.CardAggregationWrapper;
import com.dili.ss.domain.BaseOutput;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * 卡相关rpc
 */
@FeignClient(name = "account-service", contextId = "cardManageRpc")
public interface CardManageRpc {

    /**
     * 解挂卡片
     */
    @RequestMapping(value = "/api/card/unLostCard", method = RequestMethod.POST)
    BaseOutput<CardAggregationWrapper> unLostCard(@RequestBody CardRequestDto cardParam);
}
