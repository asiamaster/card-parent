package com.dili.card.rpc;

import com.dili.card.dto.UserAccountCardQuery;
import com.dili.card.dto.UserAccountCardResponseDto;
import com.dili.ss.domain.BaseOutput;
import com.dili.ss.domain.PageOutput;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

/**
 * 卡相关rpc
 */
@FeignClient(name = "account-service", contextId = "accountQueryService", path = "api/account")
public interface AccountQueryRpc {
    /**
     *查询卡信息列表
     */
    @RequestMapping(value = "/getList", method = RequestMethod.POST)
    BaseOutput<List<UserAccountCardResponseDto>> findUserCards(@RequestBody UserAccountCardQuery cardQuery);

    /**
     * 查询分页
     * @author miaoguoxin
     * @date 2020/6/22
     */
    @RequestMapping(value = "/getPage", method = RequestMethod.POST)
    PageOutput<List<UserAccountCardResponseDto>> findPage(@RequestBody UserAccountCardQuery cardQuery);
}
