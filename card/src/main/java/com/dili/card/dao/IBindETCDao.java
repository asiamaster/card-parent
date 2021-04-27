package com.dili.card.dao;

import com.dili.card.dto.ETCQueryDto;
import com.dili.card.entity.BindETCDo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Auther: miaoguoxin
 * @Date: 2021/4/27 10:50
 * @Description:
 */
public interface IBindETCDao {

    /**
     * 新增
     * @author miaoguoxin
     * @date 2021/4/27
     */
    int insert(BindETCDo bindETCDo);

    /**
    * 修改操作
    * @author miaoguoxin
    * @date 2021/4/27
    */
    int updateById(BindETCDo bindETCDo);

    /**
     * 根据车牌号查找
     * @author miaoguoxin
     * @date 2021/4/27
     */
    BindETCDo findByPlateNo(@Param("plateNo") String plateNo,@Param("firmId") Long firmId);

    /**
    * 条件查询
    * @author miaoguoxin
    * @date 2021/4/27
    */
    List<BindETCDo> findByCondition(ETCQueryDto queryDto);

    /**
    * id查找
    * @author miaoguoxin
    * @date 2021/4/27
    */
    BindETCDo findById(@Param("id") Long id);

}
