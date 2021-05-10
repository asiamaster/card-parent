package com.dili.card.service;

import com.dili.card.dto.ETCQueryDto;
import com.dili.card.dto.ETCRequestDto;
import com.dili.card.dto.ETCResponseDto;
import com.dili.card.dto.UserAccountCardResponseDto;
import com.dili.ss.domain.PageOutput;

import java.util.List;

/**
 * @Auther: miaoguoxin
 * @Date: 2021/4/26 17:09
 * @Description:
 */
public interface IETCService {

    /**
    * 绑定新的车牌号
    * @author miaoguoxin
    * @date 2021/4/27
    */
    String bind(ETCRequestDto requestDto);

    /**
    * 解绑车牌
    * @author miaoguoxin
    * @date 2021/4/27
    */
    void unBind(ETCRequestDto requestDto);

    /**
    *  分页查询
    * @author miaoguoxin
    * @date 2021/4/27
    */
    PageOutput<List<ETCResponseDto>> getPage(ETCQueryDto queryDto);
}
