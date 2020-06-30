package com.dili.card.validator;

import cn.hutool.core.lang.Assert;
import com.dili.card.dto.CardRequestDto;
import com.dili.card.dto.UserAccountCardResponseDto;
import com.dili.card.exception.CardAppBizException;
import com.dili.card.exception.ErrorCode;

/**
 * 卡账户校验器
 * @Auther: miaoguoxin
 * @Date: 2020/6/30 10:25
 */
public class AccountValidator {

    /**
     * 校验卡账户三要素匹配（cardNo、accountId、customerId）
     * @author miaoguoxin
     * @date 2020/6/30
     */
    public static void validateMatchAccount(CardRequestDto requestDto, UserAccountCardResponseDto userAccount) {
        Assert.notNull(requestDto);
        Assert.notNull(userAccount);
        if (!requestDto.getAccountId().equals(userAccount.getAccountId()) ||
                !requestDto.getCustomerId().equals(userAccount.getCustomerId()) ||
                !requestDto.getCardNo().equals(userAccount.getCardNo())) {
            throw new CardAppBizException(ErrorCode.GENERAL_CODE, "卡账户信息不匹配");
        }
    }
}
