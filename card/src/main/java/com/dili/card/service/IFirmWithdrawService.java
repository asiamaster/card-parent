package com.dili.card.service;

import com.dili.card.dto.FirmWithdrawInitResponseDto;

/**
 * @Auther: miaoguoxin
 * @Date: 2021/1/22 15:54
 * @Description:
 */
public interface IFirmWithdrawService {

    /**
    * 初始化市场提款信息
    * @author miaoguoxin
    * @date 2021/1/22
    */
    FirmWithdrawInitResponseDto init(Long firmId);
}
