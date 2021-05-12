package com.dili.card.rpc.resolver;

import com.dili.card.common.constant.ServiceName;
import com.dili.card.dto.UserAccountCardQuery;
import com.dili.card.dto.UserAccountCardResponseDto;
import com.dili.card.dto.UserAccountSingleQueryDto;
import com.dili.card.exception.CardAppBizException;
import com.dili.card.rpc.AccountQueryRpc;
import com.dili.customer.sdk.domain.dto.CustomerExtendDto;
import com.dili.ss.constant.ResultCode;
import com.dili.ss.domain.BaseOutput;
import com.dili.ss.domain.PageOutput;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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
     * 单个查询
     * @author miaoguoxin
     * @date 2020/7/28
     */
    public UserAccountCardResponseDto findSingle(UserAccountSingleQueryDto userAccountCardQuery) {
        return GenericRpcResolver.resolver(accountQueryRpc.findSingle(userAccountCardQuery), ServiceName.ACCOUNT);
    }

    /**
     *  查询单个(返回所有状态)
     * @author miaoguoxin
     * @date 2020/7/30
     */
    public UserAccountCardResponseDto findSingleWithoutValidate(UserAccountSingleQueryDto userAccountCardQuery) {
        return GenericRpcResolver.resolver(accountQueryRpc.findSingleWithoutValidate(userAccountCardQuery), ServiceName.ACCOUNT);
    }


    /**
     * 通过条件查询
     */
    public List<UserAccountCardResponseDto> findByQueryCondition(UserAccountCardQuery userAccountCardQuery) {
        BaseOutput<List<UserAccountCardResponseDto>> baseOutput = accountQueryRpc.findUserCards(userAccountCardQuery);
        return GenericRpcResolver.resolver(baseOutput, ServiceName.ACCOUNT);
    }

    /**
    * 查询根据accountId分组（默认按照创建时间排序，这里分组后，只有最新的会加入map中，
     * 后面的重复key忽略）
    * @author miaoguoxin
    * @date 2021/5/11
    */
    public Map<Long,UserAccountCardResponseDto> findByQueryConditionWithAccountIdMap(UserAccountCardQuery userAccountCardQuery){
        BaseOutput<List<UserAccountCardResponseDto>> baseOutput = accountQueryRpc.findUserCards(userAccountCardQuery);
        List<UserAccountCardResponseDto> list = GenericRpcResolver.resolver(baseOutput, ServiceName.ACCOUNT);
        return list.stream()
                .collect(Collectors.toMap(UserAccountCardResponseDto::getAccountId,
                        a -> a,
                        (k1, k2) -> k1));
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
