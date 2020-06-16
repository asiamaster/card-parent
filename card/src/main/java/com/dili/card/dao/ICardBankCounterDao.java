package com.dili.card.dao;

import org.apache.ibatis.annotations.Mapper;
import java.util.List;
import com.dili.card.entity.CardBankCounterEntity;

/**
 * 银行存取款
 * @author bob<>
 */
@Mapper
public interface ICardBankCounterDao {
	/**
     * 列表查询
     * @param page
     * @param search
     * @return
     */
	List<CardBankCounterEntity> selectList(CardBankCounterEntity cardBankCounter);

    /**
     * 新增
     * @param cardBankCounter
     * @return
     */
	int save(CardBankCounterEntity cardBankCounter);

    /**
     * 根据id查询
     * @param id
     * @return
     */
	CardBankCounterEntity getById(Long id);

    /**
     * 修改
     * @param cardBankCounter
     * @return
     */
	int update(CardBankCounterEntity cardBankCounter);

    /**
     * 删除
     * @param id
     * @return
     */
	int batchRemove(Long[] id);
}
