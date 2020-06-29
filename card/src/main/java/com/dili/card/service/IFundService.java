package com.dili.card.service;

import com.dili.card.dto.FundRequestDto;

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
}
