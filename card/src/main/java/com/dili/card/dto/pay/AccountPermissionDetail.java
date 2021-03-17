package com.dili.card.dto.pay;

/**
 * 卡账户权限明细
 * @author yuehongbo
 * @Copyright 本软件源代码版权归农丰时代科技有限公司及其研发团队所有, 未经许可不得任意复制与传播.
 * @date 2021/3/4 15:28
 */
public class AccountPermissionDetail {

    /**
     * 单日限额度
     */
    private Long dailyAmount;
    /**
     * 单日限制次数
     */
    private Integer dailyTimes;

    public Long getDailyAmount() {
        return dailyAmount;
    }
    public void setDailyAmount(Long dailyAmount) {
        this.dailyAmount = dailyAmount;
    }
    public Integer getDailyTimes() {
        return dailyTimes;
    }
    public void setDailyTimes(Integer dailyTimes) {
        this.dailyTimes = dailyTimes;
    }
}
