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
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.annotation.Resource;

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
        List<CustomerResponseDto> customers = customerRpcResolver.findCustomerByIdsWithConvert(customerIds, null);
        List<AccountListResponseDto> result = this.addCustomer2AccountList(data, customers);
        return PageUtils.convert2PageOutput(page, result);
    }

    @Override
    public AccountDetailResponseDto getDetailByCardNo(String cardNo) {
        AccountDetailResponseDto detail = new AccountDetailResponseDto();
        AccountWithAssociationResponseDto cardAssociation = accountQueryRpcResolver.findByCardNoWithAssociation(cardNo);
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
        return accountQueryRpcResolver.findByCardNo(cardNo);
    }

    @Override
    public UserAccountCardResponseDto getByCardNoWithReturnState(String cardNO) {
        return accountQueryRpcResolver.findByCardNoWithReturnState(cardNO);
    }

    @Override
    public UserAccountCardResponseDto getByAccountId(CardRequestDto requestDto) {
        UserAccountCardResponseDto accountCard = accountQueryRpcResolver.findByAccountId(requestDto.getAccountId());
        AccountValidator.validateMatchAccount(requestDto, accountCard);
        return accountCard;
    }

    @Override
    public UserAccountCardResponseDto getByAccountIdForRecharge(CardRequestDto requestDto) {
        //不需要校验账户是否存在，因为对端已经做过了判断
        AccountWithAssociationResponseDto result = accountQueryRpcResolver.findByAccountIdWithAssociation(requestDto.getAccountId());
        AccountValidator.validateMatchAccount(requestDto, result.getPrimary());

        UserAccountCardResponseDto primary = result.getPrimary();
        //挂失状态不能进行操作
        if (CardStatus.LOSS.getCode() == primary.getCardState()) {
            throw new CardAppBizException(ResultCode.DATA_ERROR, "该卡为挂失状态，不能进行此操作");
        }
        //该卡为副卡的时候需要校验关联主卡是否为挂失
        if (CardType.isSlave(primary.getCardType())) {
            //副卡肯定有关联的主卡，要是没的就是数据有问题，直接抛错，所以这里直接get(0)
            UserAccountCardResponseDto master = result.getAssociation().get(0);
            if (CardStatus.LOSS.getCode() == master.getCardState()) {
                throw new CardAppBizException(ResultCode.DATA_ERROR,
                        String.format("该卡关联的主卡【%s】为挂失状态，不能进行此操作", master.getCardNo()));
            }
        }
        return primary;
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

	@Override
	public AccountCustomerDto getAccountCustomerByCardNo(String cardNo) {
		AccountCustomerDto detail = new AccountCustomerDto();
        AccountWithAssociationResponseDto cardAssociation = accountQueryRpcResolver.findByCardNoWithAssociation(cardNo);
        UserAccountCardResponseDto primary = cardAssociation.getPrimary();
        Customer customer = GenericRpcResolver.resolver(customerRpc.get(primary.getCustomerId(), primary.getFirmId()), "测试获取用户信息");
        detail.setName(customer.getName());
        detail.setCustomerTypeName(CustomerType.getTypeName(customer.getCustomerMarket().getType()));
        detail.setAccountId(primary.getAccountId());
        detail.setCustomerId(customer.getId());
        detail.setCode(customer.getCode());
		return detail;
	}
}
