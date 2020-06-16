package com.dili.card.dao;

import org.apache.ibatis.annotations.Mapper;
import java.util.List;
import com.dili.card.entity.CardFundLogEntity;

/**
 * 账户资金操作记录,在柜员办理的业务,仅用作记录，统计结账以支付系统为主
 * @author bob<>
 */
@Mapper
public interface ICardFundLogDao {
	/**
     * 列表查询
     * @param page
     * @param search
     * @return
     */
	List<CardFundLogEntity> selectList(CardFundLogEntity cardFundLog);

    /**
     * 新增
     * @param cardFundLog
     * @return
     */
	int save(CardFundLogEntity cardFundLog);

    /**
     * 根据id查询
     * @param id
     * @return
     */
	CardFundLogEntity getById(Long id);

    /**
     * 修改
     * @param cardFundLog
     * @return
     */
	int update(CardFundLogEntity cardFundLog);

    /**
     * 删除
     * @param id
     * @return
     */
	int batchRemove(Long[] id);
}
