package com.dili.card.service;

import com.dili.card.dto.FundRequestDto;
import com.dili.card.dto.UserAccountCardResponseDto;

/**
 * 资金操作service接口
 * @author xuliang
 */
public interface IFundService {

    /**
     * 提现
     * @param fundRequestDto
     */
    void withdraw(FundRequestDto fundRequestDto);

    /**
     *  冻结资金
     * @author miaoguoxin
     * @date 2020/6/29
     */
    void frozen(FundRequestDto fundRequestDto);

    /**
     * 充值
     * @author miaoguoxin
     * @date 2020/7/2
     */
    void recharge(FundRequestDto fundRequestDto);

    /**
     * 预创建充值订单
     * @author miaoguoxin
     * @date 2020/7/6
     */
    void createRecharge(FundRequestDto fundRequestDto);
}
