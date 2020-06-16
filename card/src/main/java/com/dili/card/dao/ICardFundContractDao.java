package com.dili.card.dao;

import org.apache.ibatis.annotations.Mapper;
import java.util.List;
import com.dili.card.entity.CardFundContractEntity;

/**
 * 资金委托合同
 * @author bob<>
 */
@Mapper
public interface ICardFundContractDao {
	/**
     * 列表查询
     * @param page
     * @param search
     * @return
     */
	List<CardFundContractEntity> selectList(CardFundContractEntity cardFundContract);

    /**
     * 新增
     * @param cardFundContract
     * @return
     */
	int save(CardFundContractEntity cardFundContract);

    /**
     * 根据id查询
     * @param id
     * @return
     */
	CardFundContractEntity getById(Long id);

    /**
     * 修改
     * @param cardFundContract
     * @return
     */
	int update(CardFundContractEntity cardFundContract);

    /**
     * 删除
     * @param id
     * @return
     */
	int batchRemove(Long[] id);
}
