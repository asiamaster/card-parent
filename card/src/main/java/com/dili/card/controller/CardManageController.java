package com.dili.card.controller;

import cn.hutool.core.util.StrUtil;
import com.dili.card.dto.CardRequestDto;
import com.dili.card.rpc.CardManageRpc;
import com.dili.ss.domain.BaseOutput;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 卡片管理服务，退卡、挂失、解挂、补卡等
 * @author ：WangBo
 * @time ：2020年4月28日下午4:04:46
 */
@RestController
@RequestMapping(value = "/account")
public class CardManageController {
    private static Logger LOGGER = LoggerFactory.getLogger(CardManageController.class);

	@Resource
	private CardManageRpc cardManageRpc;

    /**
     * 解挂卡片
     */
    @PostMapping("/unLostCard")
    public BaseOutput<?> unLostCard(@RequestBody CardRequestDto cardParam) {
        try {
            if (cardParam.getAccountId() == null) {
                return BaseOutput.failure("账户ID为空");
            }
            if (StrUtil.isBlank(cardParam.getLoginPwd())) {
                return BaseOutput.failure("密码为空");
            }
            return cardManageRpc.unLostCard(cardParam);
        } catch (Exception e) {
            LOGGER.error("unLostCard", e);
            return BaseOutput.failure();
        }
    }
}
