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
     * 通过账号批量查询
     */
    public List<UserAccountCardResponseDto> findBacthByAccountIds(List<Long> accountIds) {
        UserAccountCardQuery userAccountCardQuery = new UserAccountCardQuery();
        userAccountCardQuery.setAccountIds(accountIds);
        return findByQueryCondition(userAccountCardQuery);
    }

    /**
     * 通过条件查询
     */
    public List<UserAccountCardResponseDto> findByQueryCondition(UserAccountCardQuery userAccountCardQuery) {
        BaseOutput<List<UserAccountCardResponseDto>> baseOutput = accountQueryRpc.findUserCards(userAccountCardQuery);
        List<UserAccountCardResponseDto> result = GenericRpcResolver.resolver(baseOutput, "account-service");
        if (CollectionUtils.isEmpty(result)) {
            throw new CardAppBizException(ResultCode.DATA_ERROR, "卡信息不存在");
        }
        return result;
    }

    /**
     * 通过账号查询单个账户信息
     */
    public UserAccountCardResponseDto findByAccountId(Long accountId) {
        return GenericRpcResolver.resolver(accountQueryRpc.findOneByAccountId(accountId), "account-service");
    }

    /**
     * 通过卡号批量查询多个账户信息
     */
    public List<UserAccountCardResponseDto> findBacthByCardNos(List<String> cardNos) {
        UserAccountCardQuery userAccountCardQuery = new UserAccountCardQuery();
        userAccountCardQuery.setCardNos(cardNos);
        BaseOutput<List<UserAccountCardResponseDto>> baseOutput = accountQueryRpc.findUserCards(userAccountCardQuery);
        this.validateSuccess(baseOutput);
        if (CollectionUtils.isEmpty(baseOutput.getData())) {
            throw new CardAppBizException(ResultCode.DATA_ERROR, "卡信息不存在");
        }
        return baseOutput.getData();
    }

    /**
     * 通过卡号查询单个账户信息
     */
    public UserAccountCardResponseDto findByCardNo(String cardNo) {
        return this.findBacthByCardNos(Collections.singletonList(cardNo)).get(0);
    }


    /**
     * 查询包含关联卡的信息
     * @author miaoguoxin
     * @date 2020/6/28
     */
    public AccountWithAssociationResponseDto findByCardNoWithAssociation(String cardNo) {
        BaseOutput<AccountWithAssociationResponseDto> result = accountQueryRpc.findAssociation(cardNo);
        return GenericRpcResolver.resolver(result);
    }

    /**
     * 根据账户ID查询包含关联卡的信息
     * @author miaoguoxin
     * @date 2020/6/28
     */
    public AccountWithAssociationResponseDto findByAccountIdWithAssociation(Long accountId) {
        BaseOutput<AccountWithAssociationResponseDto> result = accountQueryRpc.findAssociation(accountId);
        return GenericRpcResolver.resolver(result, "account-service");
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
