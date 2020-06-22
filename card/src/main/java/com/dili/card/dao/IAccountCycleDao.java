package com.dili.card.dao;

import org.apache.ibatis.annotations.Mapper;
import java.util.List;
import com.dili.card.entity.AccountCycleDo;

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
	List<AccountCycleDo> selectList(AccountCycleDo accountCycle);

    /**
     * 新增
     * @param accountCycle
     * @return
     */
	int save(AccountCycleDo accountCycle);

    /**
     * 根据id查询
     * @param id
     * @return
     */
	AccountCycleDo getById(Long id);

    /**
     * 修改
     * @param accountCycle
     * @return
     */
	int update(AccountCycleDo accountCycle);

    /**
     * 删除
     * @param id
     * @return
     */
	int batchRemove(Long[] id);

	/**
	 * 根据用户id查询账务周期
	 */
	AccountCycleDo findActiveCycleByUserId(Long userId);
}
