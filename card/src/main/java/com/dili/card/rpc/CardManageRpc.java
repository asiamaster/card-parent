package com.dili.card.rpc;

import com.dili.card.dto.CardRequestDto;
import com.dili.ss.domain.BaseOutput;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;


/**
 * 卡相关rpc
 */
@FeignClient(name = "account-service", contextId = "cardManageRpc")
public interface CardManageRpc {

    /**
     * 解挂卡片
     */
    @PostMapping("/api/card/unLostCard")
    BaseOutput<?> unLostCard(CardRequestDto cardParam);
    
    /**
     * 退卡
     */
    @PostMapping("/api/card/returnCard")
    BaseOutput<?> returnCard(CardRequestDto cardParam);
    
    /**
     * 重置密码
     */
    @PostMapping("/api/card/resetLoginPwd")
    BaseOutput<?> resetLoginPwd(CardRequestDto cardParam);
}
