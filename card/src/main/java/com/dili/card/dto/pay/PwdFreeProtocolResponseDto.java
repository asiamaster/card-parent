package com.dili.card.dto.pay;

import java.io.Serializable;

/**
 * @Auther: miaoguoxin
 * @Date: 2021/5/8 09:33
 * @Description: 免密协议
 */
public class PwdFreeProtocolResponseDto implements Serializable {
    /**协议id*/
    private Long protocolId;

    public Long getProtocolId() {
        return protocolId;
    }

    public void setProtocolId(Long protocolId) {
        this.protocolId = protocolId;
    }
}
