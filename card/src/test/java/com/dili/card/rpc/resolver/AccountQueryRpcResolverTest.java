package com.dili.card.rpc.resolver;

import com.alibaba.fastjson.JSON;
import com.dili.card.BaseTest;
import com.dili.card.dto.UserAccountCardQuery;
import com.dili.card.dto.UserAccountCardResponseDto;
import com.dili.ss.domain.PageOutput;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doCallRealMethod;

/**
 * @Auther: miaoguoxin
 * @Date: 2020/6/22 16:12
 * @Description:
 */
class AccountQueryRpcResolverTest extends BaseTest {
    @SpyBean
    private AccountQueryRpcResolver accountQueryRpcResolver;

    @Test
    void findPageByCondition() {
        UserAccountCardQuery param = new UserAccountCardQuery();
        param.setPageNum(1);
        param.setPageSize(20);
        param.setAccountIds(Lists.newArrayList(768141L));
        doCallRealMethod().when(accountQueryRpcResolver).findPageByCondition(param);

        PageOutput<List<UserAccountCardResponseDto>> pageByCondition = accountQueryRpcResolver.findPageByCondition(param);
        assertTrue(pageByCondition.getData().size() > 0);
        LOGGER.info("获取的分页结果:{}", JSON.toJSONString(pageByCondition.getData()));
    }
}
