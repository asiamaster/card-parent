package com.dili.card.dto;

/**
 * @description： 卡片库存激活
 * 
 * @author ：WangBo
 * @time ：2020年7月17日上午10:07:34
 */
public class CardStorageActivateDto {
	/** 卡号 */
	private String cardNo;

	public String getCardNo() {
		return cardNo;
	}

	public void setCardNo(String cardNo) {
		this.cardNo = cardNo;
	}
	
	public static CardStorageActivateDto create(String cardNo) {
		CardStorageActivateDto cardStorageActivateDto = new CardStorageActivateDto();
		cardStorageActivateDto.setCardNo(cardNo);
		return cardStorageActivateDto;
	}

}
