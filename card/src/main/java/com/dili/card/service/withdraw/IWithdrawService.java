package com.dili.card.service.withdraw;

import com.dili.card.dto.FundRequestDto;

/**
 * 提现操作接口
 */
public interface IWithdrawService {

    /**
     * 取款 返回操作流水号
     * @param fundRequestDto
     */
    String withdraw(FundRequestDto fundRequestDto);

    /**
     * 支持的交易渠道
     * @return
     */
    Integer support();
}
