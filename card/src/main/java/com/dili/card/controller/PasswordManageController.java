package com.dili.card.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.dili.card.dto.CardRequestDto;
import com.dili.card.service.IPasswordService;
import com.dili.card.validator.CardValidator;
import com.dili.ss.domain.BaseOutput;

/**
 * 密码相关操作
 */
@RestController
@RequestMapping("/card")
public class PasswordManageController {

    @Autowired
    private IPasswordService passwordService;

    /**
     * 重置登陆密码
     */
    @RequestMapping(value = "/resetLoginPwd.action", method = RequestMethod.POST)
    public BaseOutput<Boolean> resetLoginPassword(@RequestBody @Validated(value = {CardValidator.Generic.class, CardValidator.ResetPassword.class}) CardRequestDto cardRequest) throws Exception {
        passwordService.resetLoginPwd(cardRequest);
        return BaseOutput.success();
    }

}
