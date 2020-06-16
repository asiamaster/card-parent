package com.dili.card.dao;

import org.apache.ibatis.annotations.Mapper;
import java.util.List;
import com.dili.card.entity.UserLogEntity;

/**
 * 用户操作记录
 * @author bob<>
 */
@Mapper
public interface IUserLogDao {
	/**
     * 列表查询
     * @param page
     * @param search
     * @return
     */
	List<UserLogEntity> selectList(UserLogEntity userLog);

    /**
     * 新增
     * @param userLog
     * @return
     */
	int save(UserLogEntity userLog);

    /**
     * 根据id查询
     * @param id
     * @return
     */
	UserLogEntity getById(Long id);

    /**
     * 修改
     * @param userLog
     * @return
     */
	int update(UserLogEntity userLog);

    /**
     * 删除
     * @param id
     * @return
     */
	int batchRemove(Long[] id);
}
