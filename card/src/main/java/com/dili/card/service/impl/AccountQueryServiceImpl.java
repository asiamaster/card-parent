package com.dili.card.service.impl;

import com.dili.card.dto.AccountCustomerDto;
import com.dili.card.dto.AccountDetailResponseDto;
import com.dili.card.dto.AccountListResponseDto;
import com.dili.card.dto.AccountSimpleResponseDto;
import com.dili.card.dto.AccountWithAssociationResponseDto;
import com.dili.card.dto.CardRequestDto;
import com.dili.card.dto.CustomerResponseDto;
import com.dili.card.dto.UserAccountCardQuery;
import com.dili.card.dto.UserAccountCardResponseDto;
import com.dili.card.dto.pay.BalanceRequestDto;
import com.dili.card.dto.pay.BalanceResponseDto;
import com.dili.card.exception.CardAppBizException;
import com.dili.card.rpc.resolver.AccountQueryRpcResolver;
import com.dili.card.rpc.resolver.CustomerRpcResolver;
import com.dili.card.rpc.resolver.GenericRpcResolver;
import com.dili.card.rpc.resolver.PayRpcResolver;
import com.dili.card.service.IAccountQueryService;
import com.dili.card.service.IPayService;
import com.dili.card.type.CardStatus;
import com.dili.card.type.CardType;
import com.dili.card.type.CustomerType;
import com.dili.card.util.PageUtils;
import com.dili.card.validator.AccountValidator;
import com.dili.customer.sdk.domain.Customer;
import com.dili.customer.sdk.rpc.CustomerRpc;
import com.dili.ss.constant.ResultCode;
import com.dili.ss.domain.PageOutput;
import com.google.common.collect.Lists;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
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
@Service("accountQueryService")
public class AccountQueryServiceImpl implements IAccountQueryService {
    @Autowired
    private CustomerRpcResolver customerRpcResolver;
    @Autowired
    private IPayService payService;
    @Autowired
    private PayRpcResolver payRpcResolver;
    @Autowired
    private AccountQueryRpcResolver accountQueryRpcResolver;
    @Resource
    CustomerRpc customerRpc;

    @Override
    public PageOutput<List<AccountListResponseDto>> getPage(UserAccountCardQuery param) {
        //默认查询所有卡状态，包含已禁用账户
        param.setExcludeReturn(0);
        param.setExcludeDisabled(0);
        PageOutput<List<UserAccountCardResponseDto>> page = accountQueryRpcResolver.findPageByCondition(param);
        List<UserAccountCardResponseDto> data = page.getData();
        if (CollectionUtils.isEmpty(data)) {
            return PageUtils.convert2PageOutput(page, new ArrayList<>());
        }
        List<Long> customerIds = data.stream()
                .map(UserAccountCardResponseDto::getCustomerId)
                .distinct()
                .collect(Collectors.toList());
        List<CustomerResponseDto> customers = customerRpcResolver.findCustomerByIdsWithConvert(customerIds, null);
        List<AccountListResponseDto> result = this.addCustomer2AccountList(data, customers);
        return PageUtils.convert2PageOutput(page, result);
    }

    @Override
    public List<UserAccountCardResponseDto> getList(UserAccountCardQuery param) {
        return accountQueryRpcResolver.findByQueryCondition(param);
    }

    @Override
    public AccountDetailResponseDto getDetailByCardNo(String cardNo) {
        AccountDetailResponseDto detail = new AccountDetailResponseDto();
        UserAccountCardQuery query = new UserAccountCardQuery();
        query.setCardNos(Lists.newArrayList(cardNo));
        query.setValidateLevel(AccountValidator.NONE);
        AccountWithAssociationResponseDto cardAssociation = this.getAssociation(query);

        UserAccountCardResponseDto primary = cardAssociation.getPrimary();
        CustomerResponseDto customer = customerRpcResolver.findCustomerByIdWithConvert(primary.getCustomerId(), primary.getFirmId());

        BalanceResponseDto fund = payRpcResolver.findBalanceByFundAccountId(primary.getFundAccountId());

        detail.setAccountFund(fund);
        detail.setCustomer(customer);
        detail.setCardAssociation(cardAssociation);
        return detail;
    }

    @Override
    public UserAccountCardResponseDto getByCardNo(String cardNo) {
        UserAccountCardQuery userAccountCardQuery = new UserAccountCardQuery();
        userAccountCardQuery.setCardNos(Lists.newArrayList(cardNo));
        return accountQueryRpcResolver.findSingle(userAccountCardQuery);
    }

    @Override
    public UserAccountCardResponseDto getByCardNo(String cardNo, Integer validateLevel) {
        UserAccountCardQuery userAccountCardQuery = new UserAccountCardQuery();
        userAccountCardQuery.setCardNos(Lists.newArrayList(cardNo));
        userAccountCardQuery.setValidateLevel(validateLevel);
        return accountQueryRpcResolver.findSingle(userAccountCardQuery);
    }

    @Override
    public UserAccountCardResponseDto getByAccountId(CardRequestDto requestDto) {
        UserAccountCardQuery query = new UserAccountCardQuery();
        query.setAccountIds(Lists.newArrayList(requestDto.getAccountId()));
        UserAccountCardResponseDto accountCard =  accountQueryRpcResolver.findSingle(query);
        AccountValidator.validateMatchAccount(requestDto, accountCard);
        return accountCard;
    }

    @Override
    public UserAccountCardResponseDto getByAccountId(Long accountId) {
        UserAccountCardQuery query = new UserAccountCardQuery();
        query.setAccountIds(Lists.newArrayList(accountId));
        return accountQueryRpcResolver.findSingle(query);
    }

    @Override
    public AccountWithAssociationResponseDto getAssociationByAccountId(Long accountId) {
        UserAccountCardQuery query = new UserAccountCardQuery();
        query.setAccountIds(Lists.newArrayList(accountId));
        return this.getAssociation(query);
    }


    @Override
    @Deprecated
    public AccountSimpleResponseDto getByCardNoWithBalance(String cardNo) {
        UserAccountCardResponseDto userAccount = this.getByCardNo(cardNo);
        BalanceResponseDto fund = payService.queryBalance(new BalanceRequestDto(userAccount.getFundAccountId()));

        return new AccountSimpleResponseDto(fund, userAccount);
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
            accountList.setCustomerId(customerResponseDto.getId());
            accountList.setCustomerName(customerResponseDto.getName());
            accountList.setCustomerCode(customerResponseDto.getCode());
            accountList.setCustomerCellphone(customerResponseDto.getContactsPhone());
            accountListResponseDtos.add(accountList);
        }
        return accountListResponseDtos;
    }

    /**
     * 获取包含关联卡的信息
     * @author miaoguoxin
     * @date 2020/7/28
     */
    private AccountWithAssociationResponseDto getAssociation(UserAccountCardQuery query) {
        UserAccountCardResponseDto primary =  accountQueryRpcResolver.findSingle(query);
        //查询关联卡，primaryCard为主卡就查副卡，副卡就查主卡
        UserAccountCardQuery param = new UserAccountCardQuery();
        if (CardType.isMaster(primary.getCardType())) {
            param.setParentAccountId(primary.getAccountId());
        } else if (CardType.isSlave(primary.getCardType())) {
            param.setAccountIds(Lists.newArrayList(primary.getParentAccountId()));
        }
        param.setExcludeReturn(0);
        param.setExcludeDisabled(0);
        List<UserAccountCardResponseDto> association = accountQueryRpcResolver.findByQueryCondition(param);
        return new AccountWithAssociationResponseDto(primary, association);
    }
}
