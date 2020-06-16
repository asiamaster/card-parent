package com.dili.card.dao;

import org.apache.ibatis.annotations.Mapper;
import java.util.List;
import com.dili.card.entity.CardUserLogEntity;

/**
 * 用户操作记录
 * @author bob<>
 */
@Mapper
public interface ICardUserLogDao {
	/**
     * 列表查询
     * @param page
     * @param search
     * @return
     */
	List<CardUserLogEntity> selectList(CardUserLogEntity cardUserLog);

    /**
     * 新增
     * @param cardUserLog
     * @return
     */
	int save(CardUserLogEntity cardUserLog);

    /**
     * 根据id查询
     * @param id
     * @return
     */
	CardUserLogEntity getById(Long id);

    /**
     * 修改
     * @param cardUserLog
     * @return
     */
	int update(CardUserLogEntity cardUserLog);

    /**
     * 删除
     * @param id
     * @return
     */
	int batchRemove(Long[] id);
}
