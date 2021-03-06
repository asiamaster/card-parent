package com.dili.card.dto.pay;

import java.io.Serializable;

/**
 * 资金操作响应dto（冻结、解冻）
 * @Auther: miaoguoxin
 * @Date: 2020/7/16 13:39
 */
public class FundOpResponseDto implements Serializable {
    /** */
	private static final long serialVersionUID = 1231182278588249054L;
	/**冻结资金id*/
    private Long frozenId;
    /**资金事务*/
    private TradeResponseDto transaction;

    public Long getFrozenId() {
        return frozenId;
    }

    public void setFrozenId(Long frozenId) {
        this.frozenId = frozenId;
    }

    public TradeResponseDto getTransaction() {
        return transaction;
    }

    public void setTransaction(TradeResponseDto transaction) {
        this.transaction = transaction;
    }
}
