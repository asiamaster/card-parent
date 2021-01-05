package com.dili.card.service.print;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.dili.card.common.constant.Constant;
import com.dili.card.dto.PrintDto;
import com.dili.card.entity.BusinessRecordDo;
import com.dili.card.exception.CardAppBizException;
import com.dili.card.type.CardType;
import com.dili.card.type.OperateType;
import com.dili.card.type.PrintTemplate;
import com.dili.card.util.CurrencyUtils;

import cn.hutool.core.convert.Convert;

/**
 * @description： 主卡打印
 *
 * @author ：WangBo
 * @time ：2020年9月7日下午2:01:30
 */
@Service
public class OpenCardPrintServiceImpl extends PrintServiceImpl {

	private static final Logger LOG = LoggerFactory.getLogger(OpenCardPrintServiceImpl.class);

	@Override
	public String getPrintTemplate(BusinessRecordDo recordDo) {
		String attach = recordDo.getAttach();
		if (StringUtils.isNotBlank(attach)) {
			Integer cardType = JSONObject.parseObject(attach).getInteger(Constant.BUSINESS_RECORD_ATTACH_CARDTYPE);
			if (CardType.isMaster(cardType)) {
				return PrintTemplate.OPEN_MASTER_CARD.getType();
			} else if (CardType.isSlave(cardType)) {
				return PrintTemplate.OPEN_SLAVE_CARD.getType();
			} else {
				LOG.error("开卡操作记录扩展字段无法获取卡类型,流水号[{}],attach[{}]", recordDo.getSerialNo(), attach);
			}
		}
		throw new CardAppBizException("未找到合适的票据模板");
	}


	@Override
	public Integer support() {
		return OperateType.ACCOUNT_TRANSACT.getCode();
	}


	@Override
	public void createSpecial(PrintDto printDto, BusinessRecordDo recordDo, boolean reprint) {
		// 开卡的操作金额就是工本费,打印时取其中一个就可以了
		if(recordDo.getAmount() == null) {
			recordDo.setAmount(0L);
		}
		printDto.setTotalAmount(CurrencyUtils.cent2TenNoSymbol(recordDo.getAmount()));
		printDto.setTotalAmountWords(Convert.digitToChinese(Double.valueOf(recordDo.getAmount())));
		
		if(StringUtils.isNotBlank(recordDo.getAttach())) {
			Integer cardType = JSONObject.parseObject(recordDo.getAttach()).getInteger(Constant.BUSINESS_RECORD_ATTACH_CARDTYPE);
			printDto.setCardType(CardType.getName(cardType));
		}else {
			throw new CardAppBizException("开卡操作记录中未找到打印需要的卡类型");
		}
		printDto.setSerialNo(recordDo.getSerialNo());
		printDto.setFirmName(recordDo.getFirmName());
		printDto.setHoldName(recordDo.getHoldName());
		printDto.setTradeChannel(recordDo.getTradeChannelName());
	}
}
