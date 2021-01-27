package com.dili.card.util;

import org.apache.commons.lang3.StringUtils;

import cn.hutool.core.util.StrUtil;

/**
 * @description： 卡号处理
 * 
 * @author ：WangBo
 * @time ：2021年1月12日下午2:20:59
 */
public class CardNoUtil {

	/**
	 * 卡号只显示前4位和后4位，其余显示为"*"，不足4位显示全部
	 * @param cardNo
	 * @return
	 */
	public static String cipherCardNo(String cardNo) {
		if (StringUtils.isBlank(cardNo)) {
			return "";
		}
		if (cardNo.length() <= 4) {
			return cardNo;
		}
		String endNo = StrUtil.sub(cardNo, cardNo.length() - 4, cardNo.length());
		return StrUtil.sub(cardNo, 0, 4) + "****" + endNo;
	}
	
	
	/**
	 * 后四位
	 * @param cardNo
	 * @return
	 */
	public static String getTailNumber(String cardNo) {
		if (StringUtils.isBlank(cardNo)) {
			return "";
		}
		if (cardNo.length() <= 4) {
			return cardNo;
		}
		String endNo = StrUtil.sub(cardNo, cardNo.length() - 4, cardNo.length());
		return endNo;
	}
	
}
