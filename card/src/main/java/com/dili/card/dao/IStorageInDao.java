package com.dili.card.dao;

import java.util.List;

import com.dili.card.dto.CardStorageOutQueryDto;
import com.dili.card.dto.StorageInDto;
import com.dili.card.entity.StorageInDo;

/**
 * 卡片采购入库记录，从厂家到市场。
 * @author bob<>
 */
public interface IStorageInDao {
	/**
     * 列表查询
     * @param page
     * @param search
     * @return
     */
	List<StorageInDto> selectList(CardStorageOutQueryDto queryParam);

    /**
     * 新增
     * @param storageIn
     * @return
     */
	Long save(StorageInDo storageIn);

    /**
     * 根据id查询
     * @param id
     * @return
     */
	StorageInDo getById(Long id);

    /**
     * 修改
     * @param storageIn
     * @return
     */
	int update(StorageInDo storageIn);

    /**
     * 删除
     * @param id
     * @return
     */
	int batchRemove(Long[] id);
}
