package com.dili.card.dao;

import org.apache.ibatis.annotations.Mapper;
import java.util.List;
import com.dili.card.entity.BankCounterEntity;

/**
 * 银行存取款
 * @author bob<>
 */
@Mapper
public interface IBankCounterDao {
	/**
     * 列表查询
     * @param page
     * @param search
     * @return
     */
	List<BankCounterEntity> selectList(BankCounterEntity bankCounter);

    /**
     * 新增
     * @param bankCounter
     * @return
     */
	int save(BankCounterEntity bankCounter);

    /**
     * 根据id查询
     * @param id
     * @return
     */
	BankCounterEntity getById(Long id);

    /**
     * 修改
     * @param bankCounter
     * @return
     */
	int update(BankCounterEntity bankCounter);

    /**
     * 删除
     * @param id
     * @return
     */
	int batchRemove(Long[] id);
}
