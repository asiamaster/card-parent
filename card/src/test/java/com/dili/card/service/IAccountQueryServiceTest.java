package com.dili.card.service;

import com.alibaba.fastjson.JSON;
import com.dili.card.BaseTest;
import com.dili.card.dto.AccountDetailResponseDto;
import com.dili.card.dto.AccountListResponseDto;
import com.dili.card.dto.CustomerResponseDto;
import com.dili.card.dto.UserAccountCardQuery;
import com.dili.card.dto.UserAccountCardResponseDto;
import com.dili.card.rpc.resolver.AccountQueryRpcResolver;
import com.dili.card.rpc.resolver.CustomerRpcResolver;
import com.dili.ss.domain.PageOutput;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doCallRealMethod;
import static org.mockito.Mockito.doReturn;

/**
 * @Auther: miaoguoxin
 * @Date: 2020/6/22 16:10
 * @Description:
 */
class IAccountQueryServiceTest extends BaseTest {
    @Autowired
    private IAccountQueryService accountQueryService;
    @SpyBean
    private AccountQueryRpcResolver accountQueryRpcResolver;
    @SpyBean
    private CustomerRpcResolver customerRpcResolver;

    @Test
    void testGetPageWithCustomer() {
        UserAccountCardQuery param = new UserAccountCardQuery();
        param.setPage(1);
        param.setRows(20);
        param.setAccountIds(Lists.newArrayList(768141L));
        doCallRealMethod().when(accountQueryRpcResolver).findPageByCondition(param);

        PageOutput<List<AccountListResponseDto>> page = accountQueryService.getPage(param);
        LOGGER.info("获取到的page:{}", JSON.toJSONString(page.getData()));
    }

    @Test
    void testGetPageWithMockCustomer() {
        UserAccountCardQuery param = new UserAccountCardQuery();
        param.setPage(1);
        param.setRows(20);
        param.setAccountIds(Lists.newArrayList(862066L));

        doCallRealMethod().when(accountQueryRpcResolver).findPageByCondition(param);

        PageOutput<List<UserAccountCardResponseDto>> accountCardPage = accountQueryRpcResolver.findPageByCondition(param);
        List<UserAccountCardResponseDto> data = accountCardPage.getData();

        if (!CollectionUtils.isEmpty(data)) {
            List<Long> customerIds = data.stream()
                    .map(UserAccountCardResponseDto::getCustomerId)
                    .distinct()
                    .collect(Collectors.toList());
            doReturn(this.createCustomerResponse(customerIds))
                    .when(customerRpcResolver)
                    .findCustomerByIdsWithConvert(customerIds);
        }

        PageOutput<List<AccountListResponseDto>> page = accountQueryService.getPage(param);

        assertTrue(page.isSuccess());
        assertTrue(page.getData().size() > 0);
        LOGGER.info("获取到结果:{}", JSON.toJSONString(page.getData()));
    }

    @Test
    void testGetDetailByCardNo() {
        AccountDetailResponseDto detail = accountQueryService.getDetailByCardNo("888800034670");
        doReturn(this.createCustomerResponse(Lists.newArrayList(105L)).get(0))
                .when(customerRpcResolver)
                .findCustomerByIdWithConvert(105L);
        assertNotNull(detail);
        assertNotNull(detail.getCardAssociation());
        assertNotNull(detail.getCardAssociation().getPrimary());
        LOGGER.info("获取到的详情:{}",JSON.toJSONString(detail));
    }

    private List<CustomerResponseDto> createCustomerResponse(List<Long> customerIds) {
        List<CustomerResponseDto> customerResponseDtos = new ArrayList<>();
        for (Long customerId : customerIds) {
            CustomerResponseDto customerResponseDto = new CustomerResponseDto();
            customerResponseDto.setId(customerId);
            customerResponseDto.setName("测试客户");
            customerResponseDto.setCode("123fffff");
            customerResponseDtos.add(customerResponseDto);
        }
        return customerResponseDtos;
    }


}
