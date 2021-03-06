package com.dili.card.service;

import com.dili.card.dto.BindBankCardDto;
import com.dili.card.entity.BindBankCardDo;
import com.dili.ss.domain.PageOutput;

import java.util.List;

/**
 * @description： 绑定银行卡相关业务
 *
 * @author ：WangBo
 * @time ：2020年11月30日下午3:11:29
 */
public interface IBindBankCardService {

	/**
	 * 查询入库记录
	 */
	PageOutput<List<BindBankCardDto>> page(BindBankCardDto queryParam);
	
	/**
	 * 根据ID获取
	 * @param id
	 * @return
	 */
	BindBankCardDo getById(Long id);

	/**
	 * 列表查询
	 * 
	 * @author miaoguoxin
	 * @date 2021/1/13
	 */
	List<BindBankCardDto> getList(BindBankCardDto queryParam);

	/**
	 * 绑定新的银行卡
	 *
	 * @param newData
	 * @return
	 */
	boolean addBind(BindBankCardDto newData);

	/**
	 * 解绑银行卡
	 *
	 * @param newData
	 * @return
	 */
	boolean unBind(BindBankCardDto data);

	/**
	 * 绑定的卡号是否重复
	 * @param bankNo 银行卡号 
	 * @param accountId
	 * @return
	 */
	boolean existsBankNo(String bankNo, Long accountId, Long firmId);

}
