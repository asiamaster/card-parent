package com.dili.card.dao;

import org.apache.ibatis.annotations.Mapper;
import java.util.List;
import com.dili.card.entity.CardUserCashEntity;

/**
 * 柜员交款领款
 * @author bob<>
 */
@Mapper
public interface ICardUserCashDao {
	/**
     * 列表查询
     * @param page
     * @param search
     * @return
     */
	List<CardUserCashEntity> selectList(CardUserCashEntity cardUserCash);

    /**
     * 新增
     * @param cardUserCash
     * @return
     */
	int save(CardUserCashEntity cardUserCash);

    /**
     * 根据id查询
     * @param id
     * @return
     */
	CardUserCashEntity getById(Long id);

    /**
     * 修改
     * @param cardUserCash
     * @return
     */
	int update(CardUserCashEntity cardUserCash);

    /**
     * 删除
     * @param id
     * @return
     */
	int batchRemove(Long[] id);
}
