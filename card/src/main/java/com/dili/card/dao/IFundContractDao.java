package com.dili.card.dao;

import org.apache.ibatis.annotations.Mapper;
import java.util.List;

import com.dili.card.dto.FundContractRequestDto;
import com.dili.card.entity.FundContractDo;

/**
 * 资金委托合同
 * @author bob<>
 */
@Mapper
public interface IFundContractDao {
	/**
     * 列表查询
     * @param page
     * @param search
     * @return
     */
	List<FundContractDo> selectList(FundContractDo fundContract);

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
	FundContractDo getById(Long id);

    /**
     * 修改
     * @param fundContract
     * @return
     */
	int update(FundContractDo fundContract);

    /**
     * 删除
     * @param id
     * @return
     */
	int batchRemove(Long[] id);

	/**
	 * 通过条件查询
	 */
	List<FundContractDo> findEntityByCondition(FundContractRequestDto fundContractRequest);
}
