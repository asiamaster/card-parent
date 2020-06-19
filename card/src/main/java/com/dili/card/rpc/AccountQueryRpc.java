package com.dili.card.rpc;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

import com.dili.card.dto.UserAccountCardQuery;
import com.dili.card.dto.UserAccountCardResponseDto;
import com.dili.ss.domain.BaseOutput;

/**
 * 卡相关rpc
 */
@FeignClient(name = "account-service", contextId = "accountQueryService")
public interface AccountQueryRpc {
	/**
     *查询卡信息列表
     */
    @PostMapping("api/account/getList")
    BaseOutput<List<UserAccountCardResponseDto>> findUserCards(UserAccountCardQuery cardQuery);
}
