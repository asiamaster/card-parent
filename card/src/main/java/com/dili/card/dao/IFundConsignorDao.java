package com.dili.card.dao;

import org.apache.ibatis.annotations.Mapper;
import java.util.List;
import com.dili.card.entity.FundConsignorEntity;

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
	List<FundConsignorEntity> selectList(FundConsignorEntity fundConsignor);

    /**
     * 新增
     * @param fundConsignor
     * @return
     */
	int save(FundConsignorEntity fundConsignor);

    /**
     * 根据id查询
     * @param id
     * @return
     */
	FundConsignorEntity getById(Long id);

    /**
     * 修改
     * @param fundConsignor
     * @return
     */
	int update(FundConsignorEntity fundConsignor);

    /**
     * 删除
     * @param id
     * @return
     */
	int batchRemove(Long[] id);
}
