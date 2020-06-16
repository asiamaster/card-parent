package com.dili.card.dao;

import org.apache.ibatis.annotations.Mapper;
import java.util.List;
import com.dili.card.entity.FundContractEntity;

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
	List<FundContractEntity> selectList(FundContractEntity fundContract);

    /**
     * 新增
     * @param fundContract
     * @return
     */
	int save(FundContractEntity fundContract);

    /**
     * 根据id查询
     * @param id
     * @return
     */
	FundContractEntity getById(Long id);

    /**
     * 修改
     * @param fundContract
     * @return
     */
	int update(FundContractEntity fundContract);

    /**
     * 删除
     * @param id
     * @return
     */
	int batchRemove(Long[] id);
}
