package com.dili.card.service;

import java.util.List;

import com.dili.card.dto.CardStorageOutQueryDto;
import com.dili.card.entity.StorageInDo;
import com.dili.ss.domain.PageOutput;

/**
 * @description： 
 *          卡片入库相关功能接口
 * @author ：WangBo
 * @time ：2020年7月17日上午10:14:21
 */
public interface ICardStorageInService {

    /**
     * 按号段批量入库，保存入库记录和保存到卡片库存列表
     */
    void batchCardStorageIn(StorageInDo addParam);

    /**
     * 查询入库记录
     */
    PageOutput<List<StorageInDo>> list(CardStorageOutQueryDto queryParam);
}
