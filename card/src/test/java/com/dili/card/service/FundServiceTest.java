package com.dili.card.service;

import com.alibaba.fastjson.JSON;
import com.dili.card.BaseTest;
import com.dili.card.dto.FundRequestDto;
import com.dili.card.dto.SerialDto;
import com.dili.card.entity.AccountCycleDo;
import com.dili.card.entity.BusinessRecordDo;
import com.dili.card.rpc.resolver.CustomerRpcResolver;
import com.dili.customer.sdk.domain.Customer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import static org.mockito.Mockito.doCallRealMethod;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;

/**
 * @Auther: miaoguoxin
 * @Date: 2020/6/30 15:28
 * @Description:
 */
class FundServiceTest extends BaseTest {
    @Autowired
    private IFundService fundService;
    @SpyBean
    private ISerialService serialService;
    @SpyBean
    private CustomerRpcResolver customerRpcResolver;
    @MockBean
    private IAccountCycleService accountCycleService;
    @SpyBean
    private IAccountQueryService accountQueryService;

    @Test
    @Transactional
    @Rollback
    void frozen() {
        FundRequestDto requestDto = new FundRequestDto();
        requestDto.setOpId(111111L);
        requestDto.setOpName("操作员1");
        requestDto.setOpNo("1111");
        requestDto.setFirmId(8890L);

        requestDto.setAccountId(101006L);
        requestDto.setCardNo("888800034671");
        requestDto.setCustomerId(105L);
        requestDto.setAmount(1L);

        BusinessRecordDo businessRecordDo = new BusinessRecordDo();
        doCallRealMethod().when(serialService).buildCommonInfo(requestDto, businessRecordDo);
        doNothing().when(serialService).handleSuccess(new SerialDto());
        doReturn(this.createCustomer()).when(customerRpcResolver)
                .getWithNotNull(requestDto.getCustomerId(), requestDto.getFirmId());

        fundService.frozen(requestDto);
        LOGGER.info("结果:{}", JSON.toJSONString(businessRecordDo));

        //serialService.buildCommonInfo(requestDto,businessRecordDo);
    }

    @Test
    void testRecharge() {
    }

    private FundRequestDto createRechargeRequest() {
        FundRequestDto fundRequestDto = new FundRequestDto();
        fundRequestDto.setAccountId(174L);
        fundRequestDto.setCardNo("888889690048");
        fundRequestDto.setCustomerId(105L);
        fundRequestDto.setAmount(1L);
        fundRequestDto.setOpId(73L);
        fundRequestDto.setOpName("王波");
        fundRequestDto.setOpNo("");
        fundRequestDto.setFirmId(1L);
        fundRequestDto.setFirmName("集团");
        fundRequestDto.setAmount(1L);
        return fundRequestDto;
    }

    private Customer createCustomer() {
        Customer customer = new Customer();
        customer.setId(105L);
        customer.setName("ffff");
        customer.setCode("123");
        return customer;
    }

    private AccountCycleDo createAccountCycle() {
        AccountCycleDo accountCycleDo = new AccountCycleDo();
        accountCycleDo.setCycleNo(123L);
        return accountCycleDo;
    }


}
