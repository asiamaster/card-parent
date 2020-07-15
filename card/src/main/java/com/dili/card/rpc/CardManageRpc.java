package com.dili.card.rpc;

import com.dili.card.dto.CardRequestDto;
import com.dili.ss.domain.BaseOutput;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;


/**
 * 卡相关rpc
 */
@FeignClient(name = "account-service", contextId = "cardManageRpc",
        path = "/api/card", url = "http://127.0.0.1:8186")
public interface CardManageRpc {

    /**
     * 解挂卡片
     */
    @PostMapping("/unLostCard")
    BaseOutput<?> unLostCard(CardRequestDto cardParam);

    /**
     * 退卡
     */
    @PostMapping("/returnCard")
    BaseOutput<?> returnCard(CardRequestDto cardParam);

    /**
     * 重置密码
     */
    @PostMapping("/resetLoginPwd")
    BaseOutput<?> resetLoginPwd(CardRequestDto cardParam);

    /**
     * 解锁卡片
     * @param cardParam
     * @return
     */
    @RequestMapping("/unLockCard")
    BaseOutput<?> unLockCard(CardRequestDto cardParam);

    /**
     * 换卡
     * @author miaoguoxin
     * @date 2020/7/14
     */
    @PostMapping("/changeCard")
    BaseOutput<?> changeCard(CardRequestDto cardParam);
    /**
    * 挂失
    * @author miaoguoxin
    * @date 2020/7/14
    */
    @PostMapping("/reportLossCard")
    BaseOutput<?> reportLossCard(CardRequestDto cardParam);
}
