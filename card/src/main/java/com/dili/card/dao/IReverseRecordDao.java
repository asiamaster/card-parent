package com.dili.card.dao;

import com.dili.card.dto.ReverseRecordQueryDto;
import com.dili.card.entity.ReverseRecord;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 冲正记录表(CardReverseRecord)表数据库访问层
 *
 * @author miaoguoxin
 * @since 2020-11-24 10:56:13
 */
public interface IReverseRecordDao {

    /**
     * 通过ID查询单条数据
     */
    ReverseRecord findById(@Param("reverseId") Long reverseId, @Param("firmId") Long firmId);

    /**
     *  根据原业务流水号查询
     */
    ReverseRecord findByBizSerialNo(@Param("bizSerialNo") String bizSerialNo, @Param("firmId") Long firmId);

    /**
     * 通过实体作为筛选条件查询
     */
    List<ReverseRecord> findByCondition(ReverseRecordQueryDto queryDto);

    /**
     * 新增数据
     */
    int save(ReverseRecord reverseRecord);


    /**
     * 通过主键删除数据
     * @return 影响行数
     */
    int deleteById(@Param("reverseId") Long reverseId, @Param("firmId") Long firmId);

}
