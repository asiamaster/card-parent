package com.dili.card.service;

import com.dili.card.dto.CardStorageDto;
import com.dili.card.dto.CardStorageOutQueryDto;
import com.dili.card.dto.CardStorageOutRequestDto;
import com.dili.card.dto.CardStorageOutResponseDto;
import com.dili.ss.domain.PageOutput;

import java.util.List;

/**
 * @Auther: miaoguoxin
 * @Date: 2020/7/1 16:19
 */
public interface ICardStorageService {

	/**
	 * 保存卡申领记录
	 * 
	 * @author miaoguoxin
	 * @date 2020/7/3
	 */
	void saveOutRecord(CardStorageOutRequestDto requestDto);

	/**
	 * 根据卡号查询卡片在库存中的信息
	 */
	CardStorageDto getCardStorageByCardNo(String cardNo);

	/**
	 * 根据主键id查询单个
	 * 
	 * @author miaoguoxin
	 * @date 2020/7/2
	 */
	CardStorageOutResponseDto getById(Long id);

	/**
	 * 卡申领记录分页查询
	 * 
	 * @author miaoguoxin
	 * @date 2020/7/1
	 */
	PageOutput<List<CardStorageOutResponseDto>> getPage(CardStorageOutQueryDto queryDto);

	/**
	 * 多条件查询
	 * 
	 * @author miaoguoxin
	 * @date 2020/7/1
	 */
	List<CardStorageOutResponseDto> getByCondition(CardStorageOutQueryDto queryDto);

	/**
	 * 卡片仓库列表
	 * 
	 * @param id
	 * @return
	 */
	PageOutput<List<CardStorageDto>> cardStorageList(CardStorageDto queryParam);

	/**
	 * 作废卡片
	 * 
	 * @param cardNo
	 * @param remark 备注（可不填）
	 */
	void voidCard(String cardNo, String remark);

	/**
	 * 检查库中卡片状态，卡类型是否一致，如果卡面信息不为空则校验客户类型与库存中卡面信息是否合适,
	 * @param cardNo
	 * @param cardType
	 * @param customerId
	 * @return
	 */
	public CardStorageDto checkAndGetByCardNo(String cardNo, Integer cardType, Long customerId);
	
	/**
	 * 根据当前登录用户所属市场，判断卡面信息是否必须
	 * <br>目前只有寿光入库时必须要求选择卡面
	 * @return
	 */
	public boolean cardFaceIsMust();
}
