package com.dili.card.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.dili.card.dto.UserCashDto;
import com.dili.card.entity.UserCashDo;

/**
 * 柜员交款领款
 * @author bob<>
 */
public interface IUserCashDao {

    /**
     * 新增
     * @param userCash
     * @return
     */
	int save(UserCashDo userCash);

    /**
     * 根据id查询
     * @param id
     * @return
     */
	UserCashDo getById(Long id);

    /**
     * 修改
     * @param userCash
     * @return
     */
	int update(UserCashDo userCash);

	/**
	 * 删除
	 */
	boolean delete(Long id);
	
	/**
     * 列表查询
     * @param page
     * @param search
     * @return
     */
	List<UserCashDo> findEntityByCondition(UserCashDto userCashDto);

	/**
	 * 更新领取款金额
	 */
	void updateAmount(@Param("id") Long id, @Param("notes") Long amount, @Param("notes") String notes);

	/**
	 * 根据账务周期更新状态
	 */
	int updateStateByCycle(@Param("cycleNo") Long cycleNo, @Param("state") Integer state);

	/**
	 * 获取交款取款分类处理的总金额
	 */
	Long findTotalAmountByUserId(UserCashDto userCashDto);
	
}
