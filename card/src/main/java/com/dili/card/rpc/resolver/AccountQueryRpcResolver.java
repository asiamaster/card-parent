package com.dili.card.rpc.resolver;

import com.dili.card.dto.AccountWithAssociationResponseDto;
import com.dili.card.dto.UserAccountCardQuery;
import com.dili.card.dto.UserAccountCardResponseDto;
import com.dili.card.exception.CardAppBizException;
import com.dili.card.rpc.AccountQueryRpc;
import com.dili.ss.constant.ResultCode;
import com.dili.ss.domain.BaseOutput;
import com.dili.ss.domain.PageOutput;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 账户查询解析器
 * @author zhangxing
 */
@Component
public class AccountQueryRpcResolver {
    protected static Logger LOGGER = LoggerFactory.getLogger(AccountQueryRpcResolver.class);
    @Autowired
    private AccountQueryRpc accountQueryRpc;

    /**
     *  分页条件查询
     * @author miaoguoxin
     * @date 2020/6/22
     */
    public PageOutput<List<UserAccountCardResponseDto>> findPageByCondition(UserAccountCardQuery param) {
        PageOutput<List<UserAccountCardResponseDto>> page = accountQueryRpc.findPage(param);
        this.validateSuccess(page);
        return page;
    }


    /**
     * 通过账号批量查询map结构数据
     */
    public Map<Long, UserAccountCardResponseDto> findAccountCardsMapByAccountIds(UserAccountCardQuery userAccountCardQuery) {
        List<UserAccountCardResponseDto> userAccountCards = this.findByQueryCondition(userAccountCardQuery);
        return userAccountCards.stream()
                .collect(Collectors.toMap(UserAccountCardResponseDto::getAccountId,
                        a -> a,
                        (k1, k2) -> k1));
    }

    /**
    * 单个查询
    * @author miaoguoxin
    * @date 2020/7/28
    */
    public UserAccountCardResponseDto findSingle(UserAccountCardQuery userAccountCardQuery){
        return GenericRpcResolver.resolver(accountQueryRpc.findSingle(userAccountCardQuery),"account-service");
    }

    /**
     * 通过条件查询
     */
    public List<UserAccountCardResponseDto> findByQueryCondition(UserAccountCardQuery userAccountCardQuery) {
        BaseOutput<List<UserAccountCardResponseDto>> baseOutput = accountQueryRpc.findUserCards(userAccountCardQuery);
        return GenericRpcResolver.resolver(baseOutput, "account-service");
    }


    /**
     * 校验
     * @author miaoguoxin
     * @date 2020/6/22
     */
    private void validateSuccess(BaseOutput<?> baseOutput) {
        if (!baseOutput.isSuccess()) {
            LOGGER.warn("调用账户服务错误:{}", baseOutput.getMessage());
            throw new CardAppBizException(ResultCode.DATA_ERROR, baseOutput.getMessage());
        }
    }
}
