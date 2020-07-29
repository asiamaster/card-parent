package com.dili.card.validator;

import com.dili.card.dto.AccountWithAssociationResponseDto;
import com.dili.card.dto.CardRequestDto;
import com.dili.card.dto.UserAccountCardResponseDto;
import com.dili.card.dto.pay.BalanceResponseDto;
import com.dili.card.exception.CardAppBizException;
import com.dili.card.exception.ErrorCode;
import com.dili.card.rpc.resolver.PayRpcResolver;
import com.dili.card.type.CardStatus;
import com.dili.card.type.CardType;
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
    * 充值校验账户信息
    * @author miaoguoxin
    * @date 2020/7/28
    */
    public static void validateForRecharge(AccountWithAssociationResponseDto result){
        UserAccountCardResponseDto primary = result.getPrimary();
        //挂失状态不能进行操作
        if (CardStatus.LOSS.getCode() == primary.getCardState()) {
            throw new CardAppBizException(ResultCode.DATA_ERROR, "该卡为挂失状态，不能进行此操作");
        }
        //该卡为副卡的时候需要校验关联主卡是否为挂失
        if (CardType.isSlave(primary.getCardType())) {
            //副卡肯定有关联的主卡，要是没的就是数据有问题，直接抛错，所以这里直接get(0)
            UserAccountCardResponseDto master = result.getAssociation().get(0);
            if (CardStatus.LOSS.getCode() == master.getCardState()) {
                throw new CardAppBizException(ResultCode.DATA_ERROR,
                        String.format("该卡关联的主卡【%s】为挂失状态，不能进行此操作", master.getCardNo()));
            }
        }
    }
    
    /**
     * 验证余额
     * @param operationAmount 实际与余额可用金额比对的金额
     * @param fundAccountId 资金账号
     */
    protected static void checkAvailableAmount(Long operationAmount, Long fundAccountId) {
    	BalanceResponseDto balance = SpringUtil.getBean(PayRpcResolver.class).findBalanceByFundAccountId(fundAccountId);
        if (operationAmount > balance.getAvailableAmount()) {
            throw new CardAppBizException(ResultCode.DATA_ERROR, "可用余额不足");
        }
    }
}
