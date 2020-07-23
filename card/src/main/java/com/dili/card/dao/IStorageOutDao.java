package com.dili.card.dao;

import com.dili.card.dto.CardStorageOutQueryDto;
import org.apache.ibatis.annotations.Mapper;
import java.util.List;
import com.dili.card.entity.CardStorageOut;

/**
 * 柜员申领卡片记录
 * @author bob<>
 */
@Mapper
public interface IStorageOutDao {
	/**
     * 列表查询
     * @param page
     * @param search
     * @return
     */
	List<CardStorageOut> selectList(CardStorageOut applyRecord);

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
