package com.dili.card.rpc;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

import com.dili.card.dto.OpenCardDto;
import com.dili.card.dto.OpenCardResponseDto;
import com.dili.ss.domain.BaseOutput;

/**
 * @description： 开卡RPC
 * 
 * @author ：WangBo
 * @time ：2020年6月30日下午2:19:18
 */
@FeignClient(name = "account-service", contextId = "openCard")
public interface OpenCardRpc {

	/**
	 * 主卡
	 */
	@PostMapping("/api/account/openCard")
	BaseOutput<OpenCardResponseDto> openCard(OpenCardDto openCardInfo);
}
