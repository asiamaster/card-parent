package com.dili.card.dao;

import org.apache.ibatis.annotations.Mapper;
import java.util.List;
import com.dili.card.entity.AccountCycleDetailEntity;

/**
 * 柜员账务周期详情
 * @author bob<>
 */
@Mapper
public interface IAccountCycleDetailDao {
	/**
     * 列表查询
     * @param page
     * @param search
     * @return
     */
	List<AccountCycleDetailEntity> selectList(AccountCycleDetailEntity accountCycleDetail);

    /**
     * 新增
     * @param accountCycleDetail
     * @return
     */
	int save(AccountCycleDetailEntity accountCycleDetail);

    /**
     * 根据id查询
     * @param id
     * @return
     */
	AccountCycleDetailEntity getById(Long id);

    /**
     * 修改
     * @param accountCycleDetail
     * @return
     */
	int update(AccountCycleDetailEntity accountCycleDetail);

    /**
     * 删除
     * @param id
     * @return
     */
	int batchRemove(Long[] id);
}
