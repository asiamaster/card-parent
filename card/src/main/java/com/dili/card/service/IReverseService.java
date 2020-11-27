package com.dili.card.service;

import com.dili.card.dto.ReverseDetailResponseDto;
import com.dili.card.dto.ReverseRecordQueryDto;
import com.dili.card.dto.ReverseRecordResponseDto;
import com.dili.card.dto.ReverseRequestDto;
import com.dili.ss.domain.PageOutput;

import java.util.List;

/**
 * @Auther: miaoguoxin
 * @Date: 2020/11/24 14:30
 * @Description:
 */
public interface IReverseService {

    /**
     * 冲正记录分页查询
     * @author miaoguoxin
     * @date 2020/11/24
     */
    PageOutput<List<ReverseRecordResponseDto>> getPage(ReverseRecordQueryDto queryDto);

    /**
     * 冲正时查询详情
     * @author miaoguoxin
     * @date 2020/11/25
     */
    ReverseDetailResponseDto getDetail(String serialNo, Long firmId);

    /**
     * 发起冲正
     * @author miaoguoxin
     * @date 2020/11/25
     */
    Long add(ReverseRequestDto requestDto);
}
