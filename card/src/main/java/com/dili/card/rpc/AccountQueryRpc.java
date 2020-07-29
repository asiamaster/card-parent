package com.dili.card.rpc;

import com.dili.card.dto.UserAccountCardQuery;
import com.dili.card.dto.UserAccountCardResponseDto;
import com.dili.ss.domain.BaseOutput;
import com.dili.ss.domain.PageOutput;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

/**
 * 卡相关rpc
 */
@FeignClient(name = "account-service", contextId = "accountQueryService",
        path = "api/account"/*, url = "http://127.0.0.1:8186"*/)
public interface AccountQueryRpc {

    /**
     *查询卡信息列表
     */
    @RequestMapping(value = "/getList", method = RequestMethod.POST)
    BaseOutput<List<UserAccountCardResponseDto>> findUserCards(UserAccountCardQuery cardQuery);

    /**
     * 查询单个
     * @author miaoguoxin
     * @date 2020/7/28
     */
    @PostMapping(value = "/getSingle")
    BaseOutput<UserAccountCardResponseDto> findSingle(UserAccountCardQuery cardQuery);


    /**
     * 查询分页
     * @author miaoguoxin
     * @date 2020/6/22
     */
    @PostMapping(value = "/getPage")
    PageOutput<List<UserAccountCardResponseDto>> findPage(UserAccountCardQuery cardQuery);
}
