package com.dili.card.dao;

import org.apache.ibatis.annotations.Mapper;
import java.util.List;
import com.dili.card.entity.FundLogItemEntity;

/**
 * 账户资金操作费用,在柜员办理的业务
 * @author bob<>
 */
@Mapper
public interface IFundLogItemDao {
	/**
     * 列表查询
     * @param page
     * @param search
     * @return
     */
	List<FundLogItemEntity> selectList(FundLogItemEntity fundLogItem);

    /**
     * 新增
     * @param fundLogItem
     * @return
     */
	int save(FundLogItemEntity fundLogItem);

    /**
     * 根据id查询
     * @param id
     * @return
     */
	FundLogItemEntity getById(Long id);

    /**
     * 修改
     * @param fundLogItem
     * @return
     */
	int update(FundLogItemEntity fundLogItem);

    /**
     * 删除
     * @param id
     * @return
     */
	int batchRemove(Long[] id);
}
