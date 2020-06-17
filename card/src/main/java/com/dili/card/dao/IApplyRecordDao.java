package com.dili.card.dao;

import org.apache.ibatis.annotations.Mapper;
import java.util.List;
import com.dili.card.entity.ApplyRecordDo;

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
	List<ApplyRecordDo> selectList(ApplyRecordDo applyRecord);

    /**
     * 新增
     * @param applyRecord
     * @return
     */
	int save(ApplyRecordDo applyRecord);

    /**
     * 根据id查询
     * @param id
     * @return
     */
	ApplyRecordDo getById(Long id);

    /**
     * 修改
     * @param applyRecord
     * @return
     */
	int update(ApplyRecordDo applyRecord);

    /**
     * 删除
     * @param id
     * @return
     */
	int batchRemove(Long[] id);
}
