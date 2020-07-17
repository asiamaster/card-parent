package com.dili.card.rpc;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

import com.dili.card.dto.CardStorageDto;
import com.dili.ss.domain.BaseOutput;
import com.dili.ss.domain.PageOutput;

/**
 * @description： 卡片仓库服务接口
 * 
 * @author ：WangBo
 * @time ：2020年7月17日上午10:22:18
 */
@FeignClient(name = "account-service", contextId = "cardStorgeRpc", path = "/api/account/cardStorage", url ="http://127.0.0.1:8186")
public interface CardStorageRpc {

	/**
	 * 仓库中卡片列表，包括状态及入库信息
	 * 
	 * @param cardParam
	 * @return
	 */
	@PostMapping("/pageList")
	PageOutput<List<CardStorageDto>> pageList(CardStorageDto queryParam);

	/**
	 * 入库
	 */
	@PostMapping("/add")
	BaseOutput<?> addCard(CardStorageDto addParam);

	/**
	 * 作库
	 */
	@PostMapping("/void")
	BaseOutput<?> voidCard(CardStorageDto param);
}
