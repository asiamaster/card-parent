package com.dili.card.dto;

import java.io.Serializable;
import java.util.List;

/**
 * @Auther: miaoguoxin
 * @Date: 2020/6/28 10:17
 * @Description: 卡账户关联
 */
public class AccountWithAssociationResponseDto implements Serializable {
    /**根据卡号查出来的主信息*/
    private UserAccountCardResponseDto primary;
    /**关联卡信息*/
    private List<UserAccountCardResponseDto> association;

    public UserAccountCardResponseDto getPrimary() {
        return primary;
    }

    public void setPrimary(UserAccountCardResponseDto primary) {
        this.primary = primary;
    }

    public List<UserAccountCardResponseDto> getAssociation() {
        return association;
    }

    public void setAssociation(List<UserAccountCardResponseDto> association) {
        this.association = association;
    }
}
