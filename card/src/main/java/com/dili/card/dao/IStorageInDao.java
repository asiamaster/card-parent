package com.dili.card.dao;

import org.apache.ibatis.annotations.Mapper;
import java.util.List;

import com.dili.card.dto.CardStorageOutQueryDto;
import com.dili.card.entity.StorageInDo;

/**
 * 卡片采购入库记录，从厂家到市场。
 * @author bob<>
 */
@Mapper
public interface IStorageInDao {
	/**
     * 列表查询
     * @param page
     * @param search
     * @return
     */
	List<StorageInDo> selectList(CardStorageOutQueryDto queryParam);

    /**
     * 新增
     * @param storageIn
     * @return
     */
	int save(StorageInDo storageIn);

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
