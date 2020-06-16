package com.dili.card.dao;

import org.apache.ibatis.annotations.Mapper;
import java.util.List;
import com.dili.card.entity.UserCashEntity;

/**
 * 柜员交款领款
 * @author bob<>
 */
@Mapper
public interface IUserCashDao {
	/**
     * 列表查询
     * @param page
     * @param search
     * @return
     */
	List<UserCashEntity> selectList(UserCashEntity userCash);

    /**
     * 新增
     * @param userCash
     * @return
     */
	int save(UserCashEntity userCash);

    /**
     * 根据id查询
     * @param id
     * @return
     */
	UserCashEntity getById(Long id);

    /**
     * 修改
     * @param userCash
     * @return
     */
	int update(UserCashEntity userCash);

    /**
     * 删除
     * @param id
     * @return
     */
	int batchRemove(Long[] id);
}
