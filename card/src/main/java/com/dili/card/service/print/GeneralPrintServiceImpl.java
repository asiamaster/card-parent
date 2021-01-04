package com.dili.card.service.print;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.dili.card.dto.PrintDto;
import com.dili.card.entity.BusinessRecordDo;
import com.dili.card.type.PrintTemplate;
import com.dili.card.type.SerialRecordType;

/**
 * @description： 通用打印模板实现
 *          
 * @author     ：WangBo
 * @time       ：2021年1月4日上午11:22:19
 */
@Service
public class GeneralPrintServiceImpl extends PrintServiceImpl {

	private static final Logger log = LoggerFactory.getLogger(GeneralPrintServiceImpl.class);

	@Override
	public String getPrintTemplate(BusinessRecordDo recordDo) {
		switch (SerialRecordType.getOperateType(recordDo.getType())) {
		case LOSS_CARD:
			return PrintTemplate.LOSS_CARD.getType();
		case LOSS_REMOVE:
			return PrintTemplate.LOSS_REMOVE.getType();
		case PWD_CHANGE:
			return PrintTemplate.PWD_CHANGE.getType();
		case RESET_PWD:
			return PrintTemplate.RESET_PWD.getType();
		case LIFT_LOCKED:
			return PrintTemplate.LIFT_LOCKED.getType();
		default:
			log.warn("获取打印模板时，传入了一个未知的操作类型[{}]", recordDo.getType());
			return "";
		}

	}

	@Override
	public void createSpecial(PrintDto printDto, BusinessRecordDo recordDo, boolean reprint) {

	}

	@Override
	public Integer support() {
		return 0;
	}
}
