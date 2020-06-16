package com.dili.card.dao;

import org.apache.ibatis.annotations.Mapper;
import java.util.List;
import com.dili.card.entity.CardAccountCycleDetailEntity;

/**
 * 柜员账务周期详情
 * @author bob<>
 */
@Mapper
public interface ICardAccountCycleDetailDao {
	/**
     * 列表查询
     * @param page
     * @param search
     * @return
     */
	List<CardAccountCycleDetailEntity> selectList(CardAccountCycleDetailEntity cardAccountCycleDetail);

    /**
     * 新增
     * @param cardAccountCycleDetail
     * @return
     */
	int save(CardAccountCycleDetailEntity cardAccountCycleDetail);

    /**
     * 根据id查询
     * @param id
     * @return
     */
	CardAccountCycleDetailEntity getById(Long id);

    /**
     * 修改
     * @param cardAccountCycleDetail
     * @return
     */
	int update(CardAccountCycleDetailEntity cardAccountCycleDetail);

    /**
     * 删除
     * @param id
     * @return
     */
	int batchRemove(Long[] id);
}
