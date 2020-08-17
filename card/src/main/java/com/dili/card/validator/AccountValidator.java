package com.dili.card.validator;

import com.dili.card.dto.CardRequestDto;
import com.dili.card.dto.UserAccountCardResponseDto;
import com.dili.card.dto.pay.BalanceResponseDto;
import com.dili.card.exception.CardAppBizException;
import com.dili.card.exception.ErrorCode;
import com.dili.card.rpc.resolver.PayRpcResolver;
import com.dili.ss.constant.ResultCode;
import com.dili.ss.util.SpringUtil;

import cn.hutool.core.lang.Assert;

/**
 * 卡账户校验器
 * @Auther: miaoguoxin
 * @Date: 2020/6/30 10:25
 */
public class AccountValidator {
    /**只校验退卡*/
    public static final int ONLY_RETURNED = 1;
    /**只校验账户禁用*/
    public static final int ONLY_DISABLED = 2;
    /**以上两个都校验*/
    public static final int ALL = 3;
    /**都不校验*/
    public static final int NONE = 4;

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


    /**
     * 验证余额
     * @param operationAmount 实际与余额可用金额比对的金额
     * @param fundAccountId 资金账号
     */
    public static void checkAvailableAmount(Long operationAmount, Long fundAccountId) {
        checkAvailableAmountWithMsg(operationAmount, fundAccountId, "可用余额不足");
    }
    
    /**
     * 验证余额 附加信息
     * @param operationAmount 实际与余额可用金额比对的金额
     * @param fundAccountId 资金账号
     */
    public static void checkAvailableAmountWithMsg(Long operationAmount, Long fundAccountId, String msg) {
    	BalanceResponseDto balance = SpringUtil.getBean(PayRpcResolver.class).findBalanceByFundAccountId(fundAccountId);
        if (operationAmount > balance.getAvailableAmount()) {
            throw new CardAppBizException(ResultCode.DATA_ERROR, msg);
        }
    }
}
