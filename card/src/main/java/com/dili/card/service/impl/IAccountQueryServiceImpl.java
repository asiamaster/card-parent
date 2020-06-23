package com.dili.card.service.impl;

import com.dili.card.dto.AccountListResponseDto;
import com.dili.card.dto.CustomerResponseDto;
import com.dili.card.dto.UserAccountCardQuery;
import com.dili.card.dto.UserAccountCardResponseDto;
import com.dili.card.rpc.resolver.AccountQueryRpcResolver;
import com.dili.card.rpc.resolver.CustomerRpcResolver;
import com.dili.card.service.IAccountQueryService;
import com.dili.card.util.PageUtils;
import com.dili.ss.domain.PageOutput;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @Auther: miaoguoxin
 * @Date: 2020/6/22 14:00
 * @Description:
 */
@Service
public class IAccountQueryServiceImpl implements IAccountQueryService {
    @Autowired
    private CustomerRpcResolver customerRpcResolver;
    @Autowired
    private AccountQueryRpcResolver accountQueryRpcResolver;

    @Override
    public PageOutput<List<AccountListResponseDto>> getPage(UserAccountCardQuery param) {
        PageOutput<List<UserAccountCardResponseDto>> page = accountQueryRpcResolver.findPageByCondition(param);
        List<UserAccountCardResponseDto> data = page.getData();
        if (CollectionUtils.isEmpty(data)) {
            return PageUtils.convert2PageOutput(page, new ArrayList<>());
        }
        List<Long> customerIds = data.stream()
                .map(UserAccountCardResponseDto::getCustomerId)
                .distinct()
                .collect(Collectors.toList());
        //List<CustomerResponseDto> customers = customerRpcResolver.findCustomerByIdsWithConvert(customerIds);
        List<CustomerResponseDto> customers = new ArrayList<>();
        CustomerResponseDto customerResponseDto = new CustomerResponseDto();
        customerResponseDto.setId(105L);
        customerResponseDto.setCode("testsss");
        customerResponseDto.setContactsPhone("13455435345");
        customers.add(customerResponseDto);
        List<AccountListResponseDto> result = this.convertListFromAccountCardUnionCustomer(data, customers);
        return PageUtils.convert2PageOutput(page, result);
    }

    private List<AccountListResponseDto> convertListFromAccountCardUnionCustomer(List<UserAccountCardResponseDto> accountCards,
                                                                                 List<CustomerResponseDto> customers) {
        Map<Long, CustomerResponseDto> customerMap = customers.stream()
                .collect(Collectors.toMap(CustomerResponseDto::getId, Function.identity(), (key1, key2) -> key2));
        List<AccountListResponseDto> accountListResponseDtos = new ArrayList<>(accountCards.size());
        for (UserAccountCardResponseDto accountCard : accountCards) {
            AccountListResponseDto accountList = new AccountListResponseDto();
            BeanUtils.copyProperties(accountCard, accountList);
            CustomerResponseDto customerResponseDto = customerMap.getOrDefault(accountCard.getCustomerId(),
                    new CustomerResponseDto());
            accountList.setCustomer(customerResponseDto);
            accountListResponseDtos.add(accountList);
        }
        return accountListResponseDtos;
    }
}
