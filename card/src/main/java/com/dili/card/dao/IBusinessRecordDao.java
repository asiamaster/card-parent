package com.dili.card.dao;

import com.dili.card.entity.BusinessRecordDo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 柜台业务办理记录
 * @author bob<>
 */
@Mapper
public interface IBusinessRecordDao {
	/**
     * 列表查询
     * @param page
     * @param search
     * @return
     */
	List<BusinessRecordDo> selectList(BusinessRecordDo businessRecord);

    /**
     * 新增
     * @param businessRecord
     * @return
     */
	int save(BusinessRecordDo businessRecord);

    /**
     * 根据id查询
     * @param id
     * @return
     */
	BusinessRecordDo getById(Long id);

    /**
     * 修改
     * @param businessRecord
     * @return
     */
	int update(BusinessRecordDo businessRecord);

    /**
     * 删除
     * @param id
     * @return
     */
	int batchRemove(Long[] id);

	/**
	 * 根据操作流水号重置操作记录为失败
	 * @param businessRecord
	 */
    int doFailureUpdate(BusinessRecordDo businessRecord);

	/**
	 * 根据操作流水号重置操作记录为成功
	 * @param businessRecord
	 */
	int doSuccessUpdate(BusinessRecordDo businessRecord);
}
