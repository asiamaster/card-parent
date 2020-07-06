package com.dili.card.rpc;

import com.dili.card.dto.CardRequestDto;
import com.dili.ss.domain.BaseOutput;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;


/**
 * 卡相关rpc
 */
@FeignClient(name = "account-service", contextId = "cardManageRpc", url = "http://127.0.0.1:8386")
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

    /**
     * 解锁卡片
     * @param cardParam
     * @return
     */
    @RequestMapping("/api/card/unLockCard")
    BaseOutput<?> unLockCard(CardRequestDto cardParam);
}
