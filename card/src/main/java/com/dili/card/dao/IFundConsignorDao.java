package com.dili.card.dao;

import org.apache.ibatis.annotations.Mapper;
import java.util.List;
import com.dili.card.entity.FundConsignorDo;

/**
 * 受委托人
 * @author bob<>
 */
@Mapper
public interface IFundConsignorDao {
	/**
     * 列表查询
     * @param page
     * @param search
     * @return
     */
	List<FundConsignorDo> selectList(FundConsignorDo fundConsignor);

    /**
     * 新增
     * @param fundConsignor
     * @return
     */
	int save(FundConsignorDo fundConsignor);

    /**
     * 根据id查询
     * @param id
     * @return
     */
	FundConsignorDo getById(Long id);

    /**
     * 修改
     * @param fundConsignor
     * @return
     */
	int update(FundConsignorDo fundConsignor);

    /**
     * 删除
     * @param id
     * @return
     */
	int batchRemove(Long[] id);

	/**
	 * 批量保存
	 * @param consignors
	 */
	void saveBatch(List<FundConsignorDo> consignors);

	/**
	 * 通过合同编号查询被委托人信息
	 */
	List<FundConsignorDo> FindConsignorsByContractNo(String contractNo);
}
