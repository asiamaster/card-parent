package com.dili.card.dao;

import org.apache.ibatis.annotations.Mapper;
import java.util.List;
import com.dili.card.entity.ApplyRecordEntity;

/**
 * 柜员申领卡片记录
 * @author bob<>
 */
@Mapper
public interface IApplyRecordDao {
	/**
     * 列表查询
     * @param page
     * @param search
     * @return
     */
	List<ApplyRecordEntity> selectList(ApplyRecordEntity applyRecord);

    /**
     * 新增
     * @param applyRecord
     * @return
     */
	int save(ApplyRecordEntity applyRecord);

    /**
     * 根据id查询
     * @param id
     * @return
     */
	ApplyRecordEntity getById(Long id);

    /**
     * 修改
     * @param applyRecord
     * @return
     */
	int update(ApplyRecordEntity applyRecord);

    /**
     * 删除
     * @param id
     * @return
     */
	int batchRemove(Long[] id);
}
