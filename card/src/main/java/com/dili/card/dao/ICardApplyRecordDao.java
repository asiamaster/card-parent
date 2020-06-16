package com.dili.card.dao;

import org.apache.ibatis.annotations.Mapper;
import java.util.List;
import com.dili.card.entity.CardApplyRecordEntity;

/**
 * 柜员申领卡片记录
 * @author bob<>
 */
@Mapper
public interface ICardApplyRecordDao {
	/**
     * 列表查询
     * @param page
     * @param search
     * @return
     */
	List<CardApplyRecordEntity> selectList(CardApplyRecordEntity cardApplyRecord);

    /**
     * 新增
     * @param cardApplyRecord
     * @return
     */
	int save(CardApplyRecordEntity cardApplyRecord);

    /**
     * 根据id查询
     * @param id
     * @return
     */
	CardApplyRecordEntity getById(Long id);

    /**
     * 修改
     * @param cardApplyRecord
     * @return
     */
	int update(CardApplyRecordEntity cardApplyRecord);

    /**
     * 删除
     * @param id
     * @return
     */
	int batchRemove(Long[] id);
}
