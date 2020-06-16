package com.dili.card.dao;

import org.apache.ibatis.annotations.Mapper;
import java.util.List;
import com.dili.card.entity.AccountCycleEntity;

/**
 * 柜员账务周期
 * @author bob<>
 */
@Mapper
public interface IAccountCycleDao {
	/**
     * 列表查询
     * @param page
     * @param search
     * @return
     */
	List<AccountCycleEntity> selectList(AccountCycleEntity accountCycle);

    /**
     * 新增
     * @param accountCycle
     * @return
     */
	int save(AccountCycleEntity accountCycle);

    /**
     * 根据id查询
     * @param id
     * @return
     */
	AccountCycleEntity getById(Long id);

    /**
     * 修改
     * @param accountCycle
     * @return
     */
	int update(AccountCycleEntity accountCycle);

    /**
     * 删除
     * @param id
     * @return
     */
	int batchRemove(Long[] id);
}
