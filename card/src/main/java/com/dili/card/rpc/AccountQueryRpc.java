package com.dili.card.rpc;

import com.dili.card.dto.AccountWithAssociationResponseDto;
import com.dili.card.dto.UserAccountCardQuery;
import com.dili.card.dto.UserAccountCardResponseDto;
import com.dili.ss.domain.BaseOutput;
import com.dili.ss.domain.PageOutput;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

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
    * 查询单个卡账户信息
    * @author miaoguoxin
    * @date 2020/7/27
    */
    @GetMapping(value = "/getOneByAccountId/{accountId}")
    BaseOutput<UserAccountCardResponseDto> findOneByAccountId(@PathVariable Long accountId);

    /**
     * 查询包含关联卡的信息
     * @author miaoguoxin
     * @date 2020/6/28
     */
    @RequestMapping(value = "/getAssociation/{cardNo}", method = RequestMethod.GET)
    BaseOutput<AccountWithAssociationResponseDto> findAssociation(@PathVariable String cardNo);

    /**
     *  根据accountId查询包含关联卡的信息
     * @author miaoguoxin
     * @date 2020/6/28
     */
    @RequestMapping(value = "/getAssociationByAccountId/{accountId}", method = RequestMethod.GET)
    BaseOutput<AccountWithAssociationResponseDto> findAssociation(@PathVariable Long accountId);

    /**
     * 查询分页
     * @author miaoguoxin
     * @date 2020/6/22
     */
    @PostMapping(value = "/getPage")
    PageOutput<List<UserAccountCardResponseDto>> findPage(UserAccountCardQuery cardQuery);
}
