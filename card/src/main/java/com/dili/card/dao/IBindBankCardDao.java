package com.dili.card.dao;

import org.apache.ibatis.annotations.Mapper;
import java.util.List;

import com.dili.card.dto.BindBankCardDto;
import com.dili.card.entity.BindBankCardDo;

/**
 * 园区账户绑定银行卡
 * @author bob<>
 */
@Mapper
public interface IBindBankCardDao {
	/**
     * 列表查询
     * @param page
     * @param search
     * @return
     */
	List<BindBankCardDto> selectList(BindBankCardDto bankBinding);

    /**
     * 新增
     * @param bankBinding
     * @return
     */
	int save(BindBankCardDo bankBinding);

    /**
     * 根据id查询
     * @param id
     * @return
     */
	BindBankCardDo getById(Long id);

    /**
     * 修改
     * @param bankBinding
     * @return
     */
	int update(BindBankCardDo bankBinding);

    /**
     * 删除
     * @param id
     * @return
     */
	int batchRemove(Long[] id);
}
