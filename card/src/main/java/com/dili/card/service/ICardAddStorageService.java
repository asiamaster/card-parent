package com.dili.card.service;

import java.util.List;

import com.dili.card.dto.CardStorageDto;
import com.dili.ss.domain.PageOutput;

/**
 * @description： 
 *          卡片入库相关功能接口
 * @author ：WangBo
 * @time ：2020年7月17日上午10:14:21
 */
public interface ICardAddStorageService {

    /**
     * 入库
     * @param dto
     */
    void addCard(CardStorageDto addParam);

    /**
     * 卡片仓库列表
     * @param id
     * @return
     */
    PageOutput<List<CardStorageDto>> cardStorageList(CardStorageDto queryParam);

    /**
     * 作废卡片
     * @param cardNo
     * @param remark 备注（可不填）
     */
    void voidCard(String cardNo, String remark);
}
