package com.dili.card.service;

import com.dili.card.dto.ApplyRecordQueryDto;
import com.dili.card.dto.ApplyRecordResponseDto;
import com.dili.http.okhttp.utils.L;
import com.dili.ss.domain.PageOutput;
import com.github.pagehelper.Page;

import java.util.List;

/**
 * @Auther: miaoguoxin
 * @Date: 2020/7/1 16:19
 */
public interface ICardStorageService {

    /**
    * 卡申领记录分页查询
    * @author miaoguoxin
    * @date 2020/7/1
    */
    PageOutput<List<ApplyRecordResponseDto>> getPage(ApplyRecordQueryDto queryDto);

    /**
    * 多条件查询
    * @author miaoguoxin
    * @date 2020/7/1
    */
    List<ApplyRecordResponseDto> getByCondition(ApplyRecordQueryDto queryDto);
}
