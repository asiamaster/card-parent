package com.dili.card.rpc;

import com.dili.card.dto.CardRequestDto;
import com.dili.ss.domain.BaseOutput;
import com.dili.tcc.common.Tcc;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;


/**
 * 卡相关rpc
 */
@FeignClient(name = "account-service", contextId = "cardManageRpc",
        path = "/api/card"  , url = "${accountService.url:}")
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
     * 修改密码
     */
    @PostMapping("/modifyLoginPwd")
    BaseOutput<?> modifyLoginPwd(CardRequestDto cardParam);

    /**
     * 密码校验
     */
    @PostMapping("/checkPassword")
    BaseOutput<?> checkPassword(CardRequestDto cardParam);

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
    @Tcc
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
