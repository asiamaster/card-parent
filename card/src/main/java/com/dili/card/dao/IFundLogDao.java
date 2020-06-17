package com.dili.card.dao;

import org.apache.ibatis.annotations.Mapper;
import java.util.List;
import com.dili.card.entity.FundLogDo;

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
	List<FundLogDo> selectList(FundLogDo fundLog);

    /**
     * 新增
     * @param fundLog
     * @return
     */
	int save(FundLogDo fundLog);

    /**
     * 根据id查询
     * @param id
     * @return
     */
	FundLogDo getById(Long id);

    /**
     * 修改
     * @param fundLog
     * @return
     */
	int update(FundLogDo fundLog);

    /**
     * 删除
     * @param id
     * @return
     */
	int batchRemove(Long[] id);
}
