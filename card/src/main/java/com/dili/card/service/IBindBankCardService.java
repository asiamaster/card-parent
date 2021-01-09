package com.dili.card.service;

import java.util.List;

import com.dili.card.dto.BindBankCardDto;
import com.dili.ss.domain.PageOutput;

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
	PageOutput<List<BindBankCardDto>> list(BindBankCardDto queryParam);

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

}
