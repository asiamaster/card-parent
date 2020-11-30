package com.dili.card.dao;

import org.apache.ibatis.annotations.Mapper;
import java.util.List;
import com.dili.card.entity.BankBindingDo;

/**
 * 园区账户绑定银行卡
 * @author bob<>
 */
@Mapper
public interface IBankBindingDao {
	/**
     * 列表查询
     * @param page
     * @param search
     * @return
     */
	List<BankBindingDo> selectList(BankBindingDo bankBinding);

    /**
     * 新增
     * @param bankBinding
     * @return
     */
	int save(BankBindingDo bankBinding);

    /**
     * 根据id查询
     * @param id
     * @return
     */
	BankBindingDo getById(Long id);

    /**
     * 修改
     * @param bankBinding
     * @return
     */
	int update(BankBindingDo bankBinding);

    /**
     * 删除
     * @param id
     * @return
     */
	int batchRemove(Long[] id);
}
