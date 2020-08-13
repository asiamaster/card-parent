package com.dili.card.dao;

import org.apache.ibatis.annotations.Mapper;
import java.util.List;
import com.dili.card.entity.StorageOutDetailDo;

/**
 * 卡片出库，柜员领取激活的详细卡号
 * @author bob<>
 */
public interface IStorageOutDetailDao {
	/**
     * 列表查询
     * @param page
     * @param search
     * @return
     */
	List<StorageOutDetailDo> selectList(StorageOutDetailDo storageOutDetail);

    /**
     * 新增
     * @param storageOutDetail
     * @return
     */
	int save(StorageOutDetailDo storageOutDetail);
	/**
	* 批量新增
	* @author miaoguoxin
	* @date 2020/8/7
	*/
	int batchSave(List<StorageOutDetailDo> detailDos);

    /**
     * 根据id查询
     * @param id
     * @return
     */
	StorageOutDetailDo getById(Long id);

    /**
     * 修改
     * @param storageOutDetail
     * @return
     */
	int update(StorageOutDetailDo storageOutDetail);

    /**
     * 删除
     * @param id
     * @return
     */
	int batchRemove(Long[] id);
}
