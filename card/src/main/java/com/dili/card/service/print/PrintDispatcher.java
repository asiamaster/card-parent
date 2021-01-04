package com.dili.card.service.print;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.dili.card.entity.BusinessRecordDo;
import com.dili.card.exception.CardAppBizException;
import com.dili.card.type.OperateType;

import cn.hutool.core.collection.CollUtil;

/**
 * 打印相关分发器
 */
@Component
public class PrintDispatcher {

	@Autowired
	private List<IPrintService> printServiceList;

	private Map<Integer, IPrintService> printServiceMap = new ConcurrentHashMap<>();

	/**
	 * 初始化
	 */
	@PostConstruct
	public void init() {
		if (CollUtil.isEmpty(printServiceList)) {
			return;
		}

		for (IPrintService service : printServiceList) {
			printServiceMap.put(service.support(), service);
		}
		// 通用打印service,见GeneralPrintServiceImpl.support()
		IPrintService generalService = printServiceMap.get(0);
		printServiceMap.put(OperateType.LOSS_CARD.getCode(), generalService);
		printServiceMap.put(OperateType.LOSS_REMOVE.getCode(), generalService);
		printServiceMap.put(OperateType.PWD_CHANGE.getCode(), generalService);
		printServiceMap.put(OperateType.RESET_PWD.getCode(), generalService);
		printServiceMap.put(OperateType.LIFT_LOCKED.getCode(), generalService);
	}

	/**
	 * 构建打印数据
	 * 
	 * @param recordDo
	 * @param reprint
	 * @return
	 */
	public Map<String, Object> create(BusinessRecordDo recordDo, boolean reprint) {
		IPrintService printService = printServiceMap.get(recordDo.getType());
		if (printService == null) {
			throw new CardAppBizException("", "不支持该业务");
		}
		return printService.create(recordDo, reprint);
	}
}
