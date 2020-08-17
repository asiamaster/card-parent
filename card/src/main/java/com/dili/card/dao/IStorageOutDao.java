package com.dili.card.dao;

import java.util.List;

import com.dili.card.dto.CardStorageOutQueryDto;
import com.dili.card.entity.CardStorageOut;

/**
 * 柜员申领卡片记录
 * @author bob<>
 */
public interface IStorageOutDao {

	/**
	* 多条件查询
	* @author miaoguoxin
	* @date 2020/7/1
	*/
	List<CardStorageOut> selectListByCondition(CardStorageOutQueryDto queryDto);

    /**
     * 新增
     * @param applyRecord
     * @return
     */
	int save(CardStorageOut applyRecord);

    /**
     * 根据id查询
     * @param id
     * @return
     */
	CardStorageOut getById(Long id);

    /**
     * 修改
     * @param applyRecord
     * @return
     */
	int update(CardStorageOut applyRecord);

    /**
     * 删除
     * @param id
     * @return
     */
	int batchRemove(Long[] id);
}
