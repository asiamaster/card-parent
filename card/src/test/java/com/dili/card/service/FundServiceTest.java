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
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import static org.mockito.Mockito.*;

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
    @SpyBean
    private IAccountCycleService accountCycleService;

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
                .getWithNotNull(requestDto.getCustomerId(),requestDto.getFirmId());

        fundService.frozen(requestDto);
        LOGGER.info("结果:{}", JSON.toJSONString(businessRecordDo));

        //serialService.buildCommonInfo(requestDto,businessRecordDo);
    }

    private Customer createCustomer(){
        Customer customer = new Customer();
        customer.setId(105L);
        customer.setName("ffff");
        customer.setCode("123");
        return customer;
    }

    private AccountCycleDo createAccountCycle(){
        AccountCycleDo accountCycleDo = new AccountCycleDo();
        accountCycleDo.setCycleNo(1L);
        return accountCycleDo;
    }
}
