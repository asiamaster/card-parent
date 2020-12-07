package com.dili.card.entity.bo;

import cn.hutool.core.util.NumberUtil;
import com.dili.card.type.TradeType;

/**
 * @Auther: miaoguoxin
 * @Date: 2020/12/4 10:34
 * 取款业务：
 * 冲正后，客户账户变动金额= -（客户实际操作金额-原业务中操作金额） +（ 原业务手续费-  实际手续费）
 *                                        = -（100-1000）+（50-5）= 945
 *        园区收益账户变动金额=实际手续费-原业务手续费 = 5-50= - 45
 * 存款业务：
   冲正后，客户账户变动金额=（客户实际操作金额-原业务中操作金额） +（ 原业务手续费-  实际手续费）
                         =（1000-10000）+（60-6）= - 8946
          园区收益账户变动金额=实际手续费-原业务手续费 = 6-60= - 54
 */
public class ReverseAmountBo {
    /**实际操作金额*/
    private Long actualAmount;
    /**原业务操作金额*/
    private Long originalAmount;
    /**实际手续费*/
    private Long actualFee;
    /**原业务手续费*/
    private Long originalFee;
    /**交易类型 {@link com.dili.card.type.TradeType}*/
    private Integer tradeType;

    public ReverseAmountBo(Long actualAmount, Long originalAmount, Long actualFee, Long originalFee, Integer tradeType) {
        this.actualAmount = actualAmount;
        this.originalAmount = originalAmount;
        this.actualFee = actualFee;
        this.originalFee = originalFee;
        this.tradeType = tradeType;
    }

    /**
     * 计算冲正金额(不含手续费)
     * @author miaoguoxin
     * @date 2020/12/4
     */
    public Long calcReverseAmount() {
        long amount = NumberUtil.sub(this.actualAmount, this.originalAmount).longValue();
        if (TradeType.DEPOSIT.getCode() == this.tradeType) {
            return amount;
        }
        return -amount;
    }

    /**
     * 计算需要冲正的手续费
     * @author miaoguoxin
     * @date 2020/12/4
     */
    public Long calcReverseFee() {
        return NumberUtil.sub(this.originalFee, this.actualFee).longValue();
    }

    /**
     * 计算园区收益账户需要变动的金额
     * @author miaoguoxin
     * @date 2020/12/4
     */
    public Long calcInAccChangeAmount() {
        return -this.calcReverseFee();
    }

    /**
     * 计算真正需要冲正的金额
     * @author miaoguoxin
     * @date 2020/12/4
     */
    public Long calcFinalReverseAmount() {
        return NumberUtil.add(this.calcReverseAmount(), this.calcReverseFee()).longValue();
    }

    public Long getActualAmount() {
        return actualAmount;
    }

    public Long getOriginalAmount() {
        return originalAmount;
    }

    public Integer getTradeType() {
        return tradeType;
    }

    public Long getActualFee() {
        return actualFee;
    }

    public Long getOriginalFee() {
        return originalFee;
    }

}
