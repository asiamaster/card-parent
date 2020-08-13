package com.dili.card.service.recharge;

import com.dili.card.dto.FundRequestDto;
import com.dili.card.dto.pay.TradeRequestDto;
import com.dili.card.type.FeeType;
import com.dili.card.type.FundItem;
import com.dili.card.type.TradeType;

/**
 * @Auther: miaoguoxin
 * @Date: 2020/7/2 10:20
 */
public interface IRechargeManager {
    /**
    * 获取充值金额（这里是包含计算手续费和本金）
    * @author miaoguoxin
    * @date 2020/7/6
    */
    @Deprecated
    Long getRechargeAmount(FundRequestDto requestDto);

    /**
    *  构建业务流水备注信息
    * @author miaoguoxin
    * @date 2020/7/24
    */
    String buildBusinessRecordNote(FundRequestDto requestDto);

    /**
    * 构建操作流水备注信息
    * @author miaoguoxin
    * @date 2020/7/24
    */
    String buildSerialRecordNote(FundRequestDto requestDto);

    /**
    * 获取本金费用项类型
    * @author miaoguoxin
    * @date 2020/7/24
    */
    FundItem getPrincipalFundItem(FundRequestDto fundRequestDto);

    /**
    * 获取手续费项目类型
     *  现金没有手续费，是null
    * @author miaoguoxin
    * @date 2020/7/27
    */
    FundItem getServiceCostItem(FundRequestDto fundRequestDto);

    /**
    * 是否可以添加空的费用项
    * 现金没有手续费，不需要添加
    * pos和网银可能有手续费
    *
    * Ps:需求要求有些渠道在没有手续费的情况下，也需要添加一个空的记录
    * @author miaoguoxin
    * @date 2020/7/27
    */
    boolean canAddEmptyFundItem(FundRequestDto fundRequestDto);

    /**
    *  交易类型，其中网银充值是个特殊操作，类型不同
    * @author miaoguoxin
    * @date 2020/8/6
    */
    TradeType getTradeType(FundRequestDto fundRequestDto);
}
