package com.dili.card.dao;

import org.apache.ibatis.annotations.Mapper;
import java.util.List;
import com.dili.card.entity.FundLogEntity;

/**
 * 账户资金操作记录,在柜员办理的业务,仅用作记录，统计结账以支付系统为主
 * @author bob<>
 */
@Mapper
public interface IFundLogDao {
	/**
     * 列表查询
     * @param page
     * @param search
     * @return
     */
	List<FundLogEntity> selectList(FundLogEntity fundLog);

    /**
     * 新增
     * @param fundLog
     * @return
     */
	int save(FundLogEntity fundLog);

    /**
     * 根据id查询
     * @param id
     * @return
     */
	FundLogEntity getById(Long id);

    /**
     * 修改
     * @param fundLog
     * @return
     */
	int update(FundLogEntity fundLog);

    /**
     * 删除
     * @param id
     * @return
     */
	int batchRemove(Long[] id);
}
