package com.dili.card.dao;

import com.dili.card.dto.BankCounterQuery;
import org.apache.ibatis.annotations.Mapper;
import java.util.List;
import com.dili.card.entity.BankCounterDo;

/**
 * 银行存取款
 * @author bob<>
 */
@Mapper
public interface IBankCounterDao {
	/**
     * 列表查询
     * @return
     */
	List<BankCounterDo> findByCondition(BankCounterQuery query);

    /**
     * 新增
     * @param bankCounter
     * @return
     */
	int save(BankCounterDo bankCounter);

    /**
     * 根据id查询
     * @param id
     * @return
     */
	BankCounterDo getById(Long id);

    /**
     * 修改
     * @param bankCounter
     * @return
     */
	int update(BankCounterDo bankCounter);

    /**
     * 删除
     * @param id
     * @return
     */
	int batchRemove(Long[] id);
}
