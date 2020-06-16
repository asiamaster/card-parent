package com.dili.card.dao;

import org.apache.ibatis.annotations.Mapper;
import java.util.List;
import com.dili.card.entity.CardFundConsignorEntity;

/**
 * 受委托人
 * @author bob<>
 */
@Mapper
public interface ICardFundConsignorDao {
	/**
     * 列表查询
     * @param page
     * @param search
     * @return
     */
	List<CardFundConsignorEntity> selectList(CardFundConsignorEntity cardFundConsignor);

    /**
     * 新增
     * @param cardFundConsignor
     * @return
     */
	int save(CardFundConsignorEntity cardFundConsignor);

    /**
     * 根据id查询
     * @param id
     * @return
     */
	CardFundConsignorEntity getById(Long id);

    /**
     * 修改
     * @param cardFundConsignor
     * @return
     */
	int update(CardFundConsignorEntity cardFundConsignor);

    /**
     * 删除
     * @param id
     * @return
     */
	int batchRemove(Long[] id);
}
