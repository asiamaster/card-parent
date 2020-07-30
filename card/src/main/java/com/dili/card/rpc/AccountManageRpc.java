package com.dili.card.rpc;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

import com.dili.card.dto.CardRequestDto;
import com.dili.ss.domain.BaseOutput;


/**
 * 卡相关rpc
 */
@FeignClient(name = "account-service", contextId = "accountManageRpc",
        path = "/api/account"  , url = "http://127.0.0.1:8186")
public interface AccountManageRpc {

    /**
     * 解冻账户
     */
    @PostMapping("/unfrozen")
    BaseOutput<?> unfrozen(CardRequestDto cardParam);

    /**
     * 冻结账户
     */
    @PostMapping("/frozen")
    BaseOutput<?> frozen(CardRequestDto cardParam);

}
