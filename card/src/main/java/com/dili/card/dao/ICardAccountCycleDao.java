package com.dili.card.dao;

import org.apache.ibatis.annotations.Mapper;
import java.util.List;
import com.dili.card.entity.CardAccountCycleEntity;

/**
 * 柜员账务周期
 * @author bob<>
 */
@Mapper
public interface ICardAccountCycleDao {
	/**
     * 列表查询
     * @param page
     * @param search
     * @return
     */
	List<CardAccountCycleEntity> selectList(CardAccountCycleEntity cardAccountCycle);

    /**
     * 新增
     * @param cardAccountCycle
     * @return
     */
	int save(CardAccountCycleEntity cardAccountCycle);

    /**
     * 根据id查询
     * @param id
     * @return
     */
	CardAccountCycleEntity getById(Long id);

    /**
     * 修改
     * @param cardAccountCycle
     * @return
     */
	int update(CardAccountCycleEntity cardAccountCycle);

    /**
     * 删除
     * @param id
     * @return
     */
	int batchRemove(Long[] id);
}
