package com.dili.card.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.dili.card.dto.FundContractQueryDto;
import com.dili.card.entity.FundContractDo;

/**
 * 资金委托合同
 * @author bob<>
 */
@Mapper
public interface IFundContractDao {

    /**
     * 新增
     * @param fundContract
     * @return
     */
	int save(FundContractDo fundContract);

    /**
     * 根据id查询
     * @param id
     * @return
     */
	FundContractDo getById(@Param("id") Long id);

    /**
     * 修改
     * @param fundContract
     * @return
     */
	int update(FundContractDo fundContract);

	/**
	 * 通过条件查询
	 */
	List<FundContractDo> findEntityByCondition(FundContractQueryDto contractQueryDto);

	/**
	 * 关闭合同
	 */
	int closeOverdueContract();
	
	/**
	 * 激活合同
	 */
	int activeOverdueContract();
}
