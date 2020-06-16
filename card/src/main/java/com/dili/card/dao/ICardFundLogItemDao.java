package com.dili.card.dao;

import org.apache.ibatis.annotations.Mapper;
import java.util.List;
import com.dili.card.entity.CardFundLogItemEntity;

/**
 * 账户资金操作费用,在柜员办理的业务
 * @author bob<>
 */
@Mapper
public interface ICardFundLogItemDao {
	/**
     * 列表查询
     * @param page
     * @param search
     * @return
     */
	List<CardFundLogItemEntity> selectList(CardFundLogItemEntity cardFundLogItem);

    /**
     * 新增
     * @param cardFundLogItem
     * @return
     */
	int save(CardFundLogItemEntity cardFundLogItem);

    /**
     * 根据id查询
     * @param id
     * @return
     */
	CardFundLogItemEntity getById(Long id);

    /**
     * 修改
     * @param cardFundLogItem
     * @return
     */
	int update(CardFundLogItemEntity cardFundLogItem);

    /**
     * 删除
     * @param id
     * @return
     */
	int batchRemove(Long[] id);
}
