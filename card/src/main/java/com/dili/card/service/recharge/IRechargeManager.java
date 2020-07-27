package com.dili.card.service.recharge;

import com.dili.card.dto.FundRequestDto;
import com.dili.card.dto.pay.TradeRequestDto;
import com.dili.card.type.FeeType;
import com.dili.card.type.FundItem;

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
    Long getRechargeAmount(FundRequestDto requestDto);

    /**
    *  构建业务流水备注
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
    * @author miaoguoxin
    * @date 2020/7/27
    */
    FundItem getServiceCostItem(FundRequestDto fundRequestDto);

    /**
    * 是否可以添加空的费用项
    * 现金没有手续费，不需要添加
    * pos和网银可能有手续费
    * @param
    * @return
    * @author miaoguoxin
    * @date 2020/7/27
    */
    boolean canAddEmptyFundItem(FundRequestDto fundRequestDto);
}
