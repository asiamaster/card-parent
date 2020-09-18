package com.dili.card.service.impl;

import com.dili.card.common.constant.Constant;
import com.dili.card.dto.AccountDetailResponseDto;
import com.dili.card.dto.AccountListResponseDto;
import com.dili.card.dto.AccountSimpleResponseDto;
import com.dili.card.dto.AccountWithAssociationResponseDto;
import com.dili.card.dto.CardRequestDto;
import com.dili.card.dto.CustomerResponseDto;
import com.dili.card.dto.UserAccountCardQuery;
import com.dili.card.dto.UserAccountCardResponseDto;
import com.dili.card.dto.UserAccountSingleQueryDto;
import com.dili.card.dto.pay.BalanceResponseDto;
import com.dili.card.exception.CardAppBizException;
import com.dili.card.rpc.resolver.AccountQueryRpcResolver;
import com.dili.card.rpc.resolver.CustomerRpcResolver;
import com.dili.card.rpc.resolver.PayRpcResolver;
import com.dili.card.service.IAccountQueryService;
import com.dili.card.type.CardStatus;
import com.dili.card.type.CardType;
import com.dili.card.type.DisableState;
import com.dili.card.util.PageUtils;
import com.dili.card.validator.AccountValidator;
import com.dili.ss.constant.ResultCode;
import com.dili.ss.domain.PageOutput;
import com.google.common.collect.Lists;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
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
    private PayRpcResolver payRpcResolver;
    @Autowired
    private AccountQueryRpcResolver accountQueryRpcResolver;

    @Override
    public PageOutput<List<AccountListResponseDto>>  getPage(UserAccountCardQuery param) {
        //查询所有卡状态，包含已禁用账户
        param.setExcludeUnusualState(0);
        PageOutput<List<UserAccountCardResponseDto>> page = accountQueryRpcResolver.findPageByCondition(param);
        List<UserAccountCardResponseDto> data = page.getData();
        if (CollectionUtils.isEmpty(data)) {
            return PageUtils.convert2PageOutput(page, new ArrayList<>());
        }
        List<AccountListResponseDto> result = this.addCustomer2AccountList(data);
        return PageUtils.convert2PageOutput(page, result);
    }

    @Override
    public List<UserAccountCardResponseDto> getList(UserAccountCardQuery param) {
        return accountQueryRpcResolver.findByQueryCondition(param);
    }

    @Override
    public AccountDetailResponseDto getDetail(Long cardPkId, Long accountPkId) {
        AccountDetailResponseDto detail = new AccountDetailResponseDto();
        UserAccountSingleQueryDto query = new UserAccountSingleQueryDto();
        query.setAccountPkId(accountPkId);
        query.setCardPkId(cardPkId);
        AccountWithAssociationResponseDto cardAssociation = this.getAssociation(query, Constant.FALSE_INT_FLAG);
        //排除掉“退还”状态关联卡
        List<UserAccountCardResponseDto> collect = cardAssociation.getAssociation().stream()
                .filter(c -> c.getCardState() != CardStatus.RETURNED.getCode())
                .collect(Collectors.toList());
        cardAssociation.setAssociation(collect);

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
        UserAccountSingleQueryDto query = new UserAccountSingleQueryDto();
        query.setCardNo(cardNo);
        return accountQueryRpcResolver.findSingle(query);
    }


    @Override
    public UserAccountCardResponseDto getByCardNoWithoutValidate(String cardNo) {
        UserAccountSingleQueryDto query = new UserAccountSingleQueryDto();
        query.setCardNo(cardNo);
        return accountQueryRpcResolver.findSingleWithoutValidate(query);
    }

    @Override
    public UserAccountCardResponseDto getByAccountIdWithoutValidate(Long accountId) {
        UserAccountSingleQueryDto query = new UserAccountSingleQueryDto();
        query.setAccountId(accountId);
        return accountQueryRpcResolver.findSingleWithoutValidate(query);
    }

    @Override
    public UserAccountCardResponseDto getByAccountId(CardRequestDto requestDto) {
        UserAccountSingleQueryDto query = new UserAccountSingleQueryDto();
        query.setAccountId(requestDto.getAccountId());
        UserAccountCardResponseDto accountCard = accountQueryRpcResolver.findSingle(query);
        AccountValidator.validateMatchAccount(requestDto, accountCard);
        return accountCard;
    }

    @Override
    public UserAccountCardResponseDto getByAccountId(Long accountId) {
        UserAccountSingleQueryDto query = new UserAccountSingleQueryDto();
        query.setAccountId(accountId);
        return accountQueryRpcResolver.findSingle(query);
    }

    @Override
    public AccountWithAssociationResponseDto getAssociationByAccountId(Long accountId, int excludeUnusualState) {
        UserAccountSingleQueryDto query = new UserAccountSingleQueryDto();
        query.setAccountId(accountId);
        return this.getAssociation(query, excludeUnusualState);
    }


    @Override
    public AccountSimpleResponseDto getByCardNoWithBalance(String cardNo) {
        UserAccountCardResponseDto userAccount = this.getByCardNo(cardNo);
        BalanceResponseDto fund = payRpcResolver.findBalanceByFundAccountId(userAccount.getFundAccountId());
        return new AccountSimpleResponseDto(fund, userAccount);
    }


    @Override
    public UserAccountCardResponseDto getForUnLostCard(UserAccountSingleQueryDto query) {
        UserAccountCardResponseDto single = accountQueryRpcResolver.findSingleWithoutValidate(query);
        if (CardStatus.LOSS.getCode() != single.getCardState()) {
            throw new CardAppBizException(ResultCode.DATA_ERROR, "该卡不是挂失状态，不能进行此操作");
        }
        if (DisableState.DISABLED.getCode().equals(single.getDisabledState())) {
            throw new CardAppBizException(ResultCode.DATA_ERROR, "该账户为禁用状态，不能进行此操作");
        }
        return single;
    }


    private List<AccountListResponseDto> addCustomer2AccountList(List<UserAccountCardResponseDto> accountCards) {
        return accountCards.stream().map(accountCard -> {
            AccountListResponseDto accountList = new AccountListResponseDto();
            BeanUtils.copyProperties(accountCard, accountList);
            return accountList;
        }).collect(Collectors.toList());
    }


    /**
     * 获取包含关联卡的信息
     * @author miaoguoxin
     * @date 2020/7/28
     */
    private AccountWithAssociationResponseDto getAssociation(UserAccountSingleQueryDto query, int excludeUnusualState) {
        UserAccountCardResponseDto primary = accountQueryRpcResolver.findSingleWithoutValidate(query);
        //查询关联卡，primaryCard为主卡就查副卡，副卡就查主卡
        UserAccountCardQuery param = new UserAccountCardQuery();
        if (CardType.isMaster(primary.getCardType())) {
            param.setParentAccountId(primary.getAccountId());
        } else if (CardType.isSlave(primary.getCardType())) {
            param.setAccountIds(Lists.newArrayList(primary.getParentAccountId()));
        }
        param.setExcludeUnusualState(excludeUnusualState);
        List<UserAccountCardResponseDto> association = accountQueryRpcResolver.findByQueryCondition(param);
        return new AccountWithAssociationResponseDto(primary, association);
    }

}
