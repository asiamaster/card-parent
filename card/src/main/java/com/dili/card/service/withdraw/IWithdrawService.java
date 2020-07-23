package com.dili.card.service.withdraw;

import com.dili.card.dto.FundRequestDto;

/**
 * 提现操作接口
 */
public interface IWithdrawService {

    /**
     * 取款
     * @param fundRequestDto
     */
    void withdraw(FundRequestDto fundRequestDto);

    /**
     * 支持的交易渠道
     * @return
     */
    Integer support();
}
