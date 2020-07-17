package com.dili.card.dto.pay;

import java.io.Serializable;

/**
 * 资金操作响应dto（冻结、解冻）
 * @Auther: miaoguoxin
 * @Date: 2020/7/16 13:39
 */
public class FundOpResponseDto implements Serializable {
    /**冻结资金id*/
    private Long frozenId;

    public Long getFrozenId() {
        return frozenId;
    }

    public void setFrozenId(Long frozenId) {
        this.frozenId = frozenId;
    }
}
