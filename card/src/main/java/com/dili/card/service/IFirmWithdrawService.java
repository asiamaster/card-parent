package com.dili.card.service;

import com.dili.card.dto.FirmWithdrawAuthRequestDto;
import com.dili.card.dto.FirmWithdrawInitResponseDto;
import com.dili.card.dto.FundRequestDto;
import com.dili.card.dto.PipelineRecordQueryDto;
import com.dili.card.dto.pay.PipelineRecordResponseDto;
import com.dili.card.entity.bo.MessageBo;
import com.dili.ss.domain.PageOutput;

import java.util.List;

/**
 * @Auther: miaoguoxin
 * @Date: 2021/1/22 15:54
 * @Description:
 */
public interface IFirmWithdrawService {

    /**
    * 初始化市场提款信息
    * @author miaoguoxin
    * @date 2021/1/22
    */
    FirmWithdrawInitResponseDto init(Long firmId);

    /**
     * 市场圈提
     * @author miaoguoxin
     * @date 2021/1/25
     */
    MessageBo<String> doMerWithdraw(FundRequestDto requestDto);

    /**
    * 授权校验
    * @author miaoguoxin
    * @date 2021/1/26
    */
    void checkAuth(FirmWithdrawAuthRequestDto requestDto);

    /**
    *
    * @author miaoguoxin
    * @date 2021/1/25
    */
    PageOutput<List<PipelineRecordResponseDto>> bankWithdrawPage(PipelineRecordQueryDto param);
}
