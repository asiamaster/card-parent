package com.dili.card.service.withdraw;

import com.dili.card.dto.FundRequestDto;
import com.dili.card.entity.bo.MessageBo;
import org.springframework.amqp.core.Message;

/**
 * 提现操作接口
 */
public interface IWithdrawService {

    /**
     * 取款 返回操作流水号
     * @param fundRequestDto
     */
    MessageBo<String> withdraw(FundRequestDto fundRequestDto);

    /**
     * 支持的交易渠道
     * @return
     */
    Integer support();
}
