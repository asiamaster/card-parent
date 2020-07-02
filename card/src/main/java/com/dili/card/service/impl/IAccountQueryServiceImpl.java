package com.dili.card.service.impl;

import com.dili.card.dto.AccountDetailResponseDto;
import com.dili.card.dto.AccountListResponseDto;
import com.dili.card.dto.AccountWithAssociationResponseDto;
import com.dili.card.dto.CustomerResponseDto;
import com.dili.card.dto.UserAccountCardQuery;
import com.dili.card.dto.UserAccountCardResponseDto;
import com.dili.card.dto.pay.BalanceResponseDto;
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
        //默认查询所有卡状态，并排除掉已禁用账户
        param.setDefExcludeReturn(0).setDefExcludeDisabled(1);
        PageOutput<List<UserAccountCardResponseDto>> page = accountQueryRpcResolver.findPageByCondition(param);
        List<UserAccountCardResponseDto> data = page.getData();
        if (CollectionUtils.isEmpty(data)) {
            return PageUtils.convert2PageOutput(page, new ArrayList<>());
        }
        List<Long> customerIds = data.stream()
                .map(UserAccountCardResponseDto::getCustomerId)
                .distinct()
                .collect(Collectors.toList());
        List<CustomerResponseDto> customers = customerRpcResolver.findCustomerByIdsWithConvert(customerIds);
        List<AccountListResponseDto> result = this.addCustomer2AccountList(data, customers);
        return PageUtils.convert2PageOutput(page, result);
    }

    @Override
    public AccountDetailResponseDto getDetailByCardNo(String cardNo) {
        AccountDetailResponseDto detail = new AccountDetailResponseDto();
        AccountWithAssociationResponseDto cardAssociation = accountQueryRpcResolver.findByCardNoWithAssociation(cardNo);
        CustomerResponseDto customer = customerRpcResolver.findCustomerByIdWithConvert(cardAssociation.getPrimary().getCustomerId());
        //TODO 资金需要调用rpc
        BalanceResponseDto fund = new BalanceResponseDto();
        fund.setBalance(1L);
        fund.setAvailableAmount(1L);
        fund.setFrozenAmount(1000L);

        detail.setAccountFund(fund);
        detail.setCustomer(customer);
        detail.setCardAssociation(cardAssociation);
        return detail;
    }

    @Override
    public UserAccountCardResponseDto getByCardNo(String cardNo) {

        return  accountQueryRpcResolver.findByCardNo(cardNo);
    }

    private List<AccountListResponseDto> addCustomer2AccountList(List<UserAccountCardResponseDto> accountCards,
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
