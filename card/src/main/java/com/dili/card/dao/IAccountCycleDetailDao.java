package com.dili.card.dao;

import org.apache.ibatis.annotations.Mapper;
import java.util.List;
import com.dili.card.entity.AccountCycleDetailDo;

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
	List<AccountCycleDetailDo> selectList(AccountCycleDetailDo accountCycleDetail);

    /**
     * 新增
     * @param accountCycleDetail
     * @return
     */
	int save(AccountCycleDetailDo accountCycleDetail);

    /**
     * 根据id查询
     * @param id
     * @return
     */
	AccountCycleDetailDo getById(Long id);

    /**
     * 修改
     * @param accountCycleDetail
     * @return
     */
	int update(AccountCycleDetailDo accountCycleDetail);

    /**
     * 删除
     * @param id
     * @return
     */
	int batchRemove(Long[] id);
}
