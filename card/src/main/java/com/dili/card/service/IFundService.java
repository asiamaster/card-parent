package com.dili.card.service;

import com.dili.card.dto.FundRequestDto;
import com.dili.card.dto.PipelineRecordQueryDto;
import com.dili.card.dto.UnfreezeFundDto;
import com.dili.card.dto.pay.FreezeFundRecordDto;
import com.dili.card.dto.pay.FreezeFundRecordParam;
import com.dili.card.dto.pay.PipelineRecordResponseDto;
import com.dili.card.dto.pay.TradeResponseDto;
import com.dili.ss.domain.PageOutput;

import java.util.List;

/**
 * 资金操作service接口
 *
 * @author xuliang
 */
public interface IFundService {
    /**
     * 冻结资金
     * @author miaoguoxin
     * @date 2020/6/29
     */
    void frozen(FundRequestDto fundRequestDto);

    /**
     * 从支付查询人工冻结资金记录
     *
     * @param fundRequestDto
     */
    PageOutput<List<FreezeFundRecordDto>> frozenRecord(FreezeFundRecordParam queryParam);

    /**
     * 解冻资金,支持批量操作
     */
    void unfrozen(UnfreezeFundDto unfreezeFundDto);

    /**
     *  充值
     * @author miaoguoxin
     * @date 2020/8/13
     */
    String recharge(FundRequestDto requestDto);

    /**
     * 银行圈提记录分页
     * @author miaoguoxin
     * @date 2020/12/24
     */
    PageOutput<List<PipelineRecordResponseDto>> bankWithdrawPage(PipelineRecordQueryDto param);

    /**
    * 银行圈提回调
    * @author miaoguoxin
    * @date 2021/1/18
    */
    void handleBankWithdrawCallback(TradeResponseDto tradeResponseDto);
}
