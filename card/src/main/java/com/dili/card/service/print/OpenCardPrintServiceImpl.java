package com.dili.card.service.print;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.dili.card.common.constant.Constant;
import com.dili.card.dto.PrintDto;
import com.dili.card.entity.BusinessRecordDo;
import com.dili.card.type.OperateType;
import com.dili.card.type.PrintTemplate;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

/**
 * @description： 主卡打印
 * 
 * @author ：WangBo
 * @time ：2020年9月7日下午2:01:30
 */
@Service
public class OpenCardPrintServiceImpl extends PrintServiceImpl {
	@Override
	public String getPrintTemplate(BusinessRecordDo recordDo) {
		return PrintTemplate.OPEN_CARD.getType();
	}

	@Override
	public void createSpecial(PrintDto printDto, BusinessRecordDo recordDo, boolean reprint) {
	}

	@Override
	public Integer support() {
		return OperateType.ACCOUNT_TRANSACT.getCode();
	}
}
