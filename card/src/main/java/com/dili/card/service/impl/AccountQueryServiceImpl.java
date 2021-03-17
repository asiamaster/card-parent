package com.dili.card.service.impl;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSONObject;
import com.dili.card.common.constant.Constant;
import com.dili.card.common.constant.ServiceName;
import com.dili.card.dto.*;
import com.dili.card.dto.pay.AccountAllPermission;
import com.dili.card.dto.pay.AccountPermissionResponseDto;
import com.dili.card.dto.pay.BalanceResponseDto;
import com.dili.card.dto.pay.CustomerBalanceResponseDto;
import com.dili.card.exception.CardAppBizException;
import com.dili.card.rpc.AccountQueryRpc;
import com.dili.card.rpc.CardStorageRpc;
import com.dili.card.rpc.PayRpc;
import com.dili.card.rpc.resolver.AccountQueryRpcResolver;
import com.dili.card.rpc.resolver.CustomerRpcResolver;
import com.dili.card.rpc.resolver.GenericRpcResolver;
import com.dili.card.rpc.resolver.PayRpcResolver;
import com.dili.card.service.IAccountQueryService;
import com.dili.card.service.ICustomerService;
import com.dili.card.type.CardStatus;
import com.dili.card.type.CardType;
import com.dili.card.type.DisableState;
import com.dili.card.util.PageUtils;
import com.dili.card.validator.AccountValidator;
import com.dili.ss.constant.ResultCode;
import com.dili.ss.domain.BaseOutput;
import com.dili.ss.domain.PageOutput;
import com.google.common.collect.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @Auther: miaoguoxin
 * @Date: 2020/6/22 14:00
 * @Description:
 */
@Service("accountQueryService")
public class AccountQueryServiceImpl implements IAccountQueryService {

	private static final Logger log = LoggerFactory.getLogger(AccountQueryServiceImpl.class);

    @Autowired
    private CustomerRpcResolver customerRpcResolver;
    @Autowired
    private ICustomerService customerService;
    @Autowired
    private PayRpc payRpc;
    @Autowired
    private PayRpcResolver payRpcResolver;
    @Autowired
    private AccountQueryRpcResolver accountQueryRpcResolver;
    @Autowired
    private AccountQueryRpc accountQueryRpc;
    @Autowired
    private CardStorageRpc cardStorageRpc;

    @Override
    public PageOutput<List<AccountListResponseDto>> getPage(UserAccountCardQuery param) {
        //查询所有卡状态，包含已禁用账户
        param.setExcludeUnusualState(0);
        PageOutput<List<UserAccountCardResponseDto>> page = accountQueryRpcResolver.findPageByCondition(param);
        List<UserAccountCardResponseDto> data = page.getData();
        if (CollectionUtils.isEmpty(data)) {
            return PageUtils.convert2PageOutput(page, new ArrayList<>());
        }
        List<String> cardNos = data.stream().map(UserAccountCardResponseDto::getCardNo)
                .collect(Collectors.toList());
        //查询卡面信息
        Map<String, CardStorageDto> stockReduceDtoMap = this.getCardFaceMap(cardNos);

        List<AccountListResponseDto> result = this.addCustomer2AccountList(data);
        for (AccountListResponseDto responseDto : result) {
            CardStorageDto dto = stockReduceDtoMap.getOrDefault(responseDto.getCardNo(), new CardStorageDto());
            responseDto.setCardFace(dto.getCardFace());
        }
        // 设置客户子类型  TODO 待优化，开发环境耗时0.3秒左右
        log.info("耗时测试a");
        List<Long> cidList = result.stream().map(AccountListResponseDto::getCustomerId).collect(Collectors.toList());
        Map<Long, String> subTypeNames = customerService.getSubTypeNames(cidList, param.getFirmId());
        result.forEach(account -> account.setCustomerSubTypeName(subTypeNames.get(account.getCustomerId())));
        log.info("耗时测试b");
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
        //客户信息已经在account-service中做了冗余，不进行查询也可以
        CustomerResponseDto customer = customerRpcResolver.findCustomerByIdWithConvert(primary.getCustomerId(), primary.getFirmId());

        BalanceResponseDto fund;
        try {
            fund = payRpcResolver.findBalanceByFundAccountId(primary.getFundAccountId());
        } catch (Exception e) {
            //支付异常状态不管，有可能是（资金账户被注销）
            fund = new BalanceResponseDto();
            fund.setAvailableAmount(0L);
            fund.setBalance(0L);
            fund.setFrozenAmount(0L);
        }

        detail.setAccountFund(fund);
        detail.setCustomer(customer);
        detail.setCardAssociation(cardAssociation);
        return detail;
    }

    @Override
    public AccountDetailResponseDto getDetailEx(Long cardPkId, Long accountPkId) {
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
        //客户信息已经在account-service中做了冗余，不进行查询也可以
        CustomerResponseDto customer = customerRpcResolver.findCustomerByIdWithConvert(primary.getCustomerId(), primary.getFirmId());

        BalanceResponseDto fund;
        try {
            fund = payRpcResolver.findBalanceByFundAccountIdEx(primary.getFundAccountId());
        } catch (Exception e) {
            //支付异常状态不管，有可能是（资金账户被注销）
            fund = new BalanceResponseDto();
            fund.setAvailableAmount(0L);
            fund.setBalance(0L);
            fund.setFrozenAmount(0L);
        }
        
        String subTypeNames = customerService.getSubTypeNames(customer.getId(), primary.getFirmId());
        customer.setCustomerSubType(subTypeNames);
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
        if (CardStatus.RETURNED.getCode() == single.getCardState()) {
            throw new CardAppBizException(ResultCode.DATA_ERROR, "该卡已退卡，不能进行此操作");
        }
        if (CardStatus.LOSS.getCode() != single.getCardState()) {
            throw new CardAppBizException(ResultCode.DATA_ERROR,String.format("该卡为%s状态，不能进行此操作", CardStatus.getName(single.getCardState())));
        }
        if (DisableState.DISABLED.getCode().equals(single.getDisabledState())) {
            throw new CardAppBizException(ResultCode.DATA_ERROR, String.format("该账户为%s状态，不能进行此操作", DisableState.DISABLED.getName()));
        }
        return single;
    }

    @Override
    public UserAccountCardResponseDto getForUnLockCard(UserAccountSingleQueryDto query) {
        UserAccountCardResponseDto single = accountQueryRpcResolver.findSingleWithoutValidate(query);
        if (CardStatus.RETURNED.getCode() == single.getCardState()) {
            throw new CardAppBizException(ResultCode.DATA_ERROR, "该卡已退卡，不能进行此操作");
        }
        if (CardStatus.LOCKED.getCode() != single.getCardState()) {
            throw new CardAppBizException(ResultCode.DATA_ERROR, String.format("该卡为%s状态，不能进行此操作", CardStatus.getName(single.getCardState())));
        }
        if (DisableState.DISABLED.getCode().equals(single.getDisabledState())) {
            throw new CardAppBizException(ResultCode.DATA_ERROR, String.format("该账户为%s状态，不能进行此操作", DisableState.DISABLED.getName()));
        }
        return single;
    }

    @Override
    public List<AccountWithAssociationResponseDto> getMasterAssociationList(Long customerId) {
        UserAccountCardQuery masterParams = new UserAccountCardQuery();
        masterParams.setCardType(CardType.MASTER.getCode());
        masterParams.setCustomerIds(Lists.newArrayList(customerId));
        //按照现在需求，一个customerId只有一张主卡，但是为了需求有变，这里设计成list
        //以防customerId对应多个主卡的情况
        List<UserAccountCardResponseDto> masterList = GenericRpcResolver.resolver(accountQueryRpc.findListV2(masterParams), ServiceName.ACCOUNT);

        List<AccountWithAssociationResponseDto> result = new ArrayList<>();
        UserAccountCardQuery slaveParams = new UserAccountCardQuery();
        slaveParams.setCardType(CardType.SLAVE.getCode());
        for (UserAccountCardResponseDto master : masterList) {
            slaveParams.setParentAccountId(master.getAccountId());
            List<UserAccountCardResponseDto> slaveList = GenericRpcResolver.resolver(accountQueryRpc.findListV2(slaveParams), ServiceName.ACCOUNT);
            BalanceResponseDto fund = payRpcResolver.findBalanceByFundAccountId(master.getFundAccountId());
            AccountWithAssociationResponseDto responseDto = new AccountWithAssociationResponseDto(master, slaveList, fund);
            result.add(responseDto);
        }
        return result;
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

    private Map<String, CardStorageDto> getCardFaceMap(List<String> cardNos) {
        CardRepoQueryParam queryParam = new CardRepoQueryParam();
        queryParam.setCardNos(cardNos);
        List<CardStorageDto> cardStorageDtos = GenericRpcResolver.resolver(cardStorageRpc.listByCardNo(queryParam), ServiceName.ACCOUNT);
        return cardStorageDtos.stream().collect(Collectors.toMap(CardStorageDto::getCardNo,
                Function.identity(), (key1, key2) -> key2));
    }
	@Override
	public CustomerBalanceResponseDto getAccountFundByCustomerId(Long customerId) {
		UserAccountCardQuery masterParams = new UserAccountCardQuery();
        masterParams.setCardType(CardType.MASTER.getCode());
        masterParams.setCustomerIds(Lists.newArrayList(customerId));
        //按照现在需求，一个customerId只有一张主卡，但是为了需求有变，这里设计成list
        //以防customerId对应多个主卡的情况
        List<UserAccountCardResponseDto> masterList = GenericRpcResolver.resolver(accountQueryRpc.findListV2(masterParams), ServiceName.ACCOUNT);
        if(CollectionUtils.isEmpty(masterList)) {
        	throw new CardAppBizException("您没有开通过账户");
        }

        // 查询客户资产情况
        FundAccountDto fundAccountDto = new FundAccountDto();
        fundAccountDto.setCustomerId(customerId);
        CustomerBalanceResponseDto customerBalance = GenericRpcResolver.resolver(payRpc.getAccountFundByCustomerId(fundAccountDto),ServiceName.PAY);
        if(CollectionUtils.isEmpty(masterList)) {
        	log.warn("未获取到支付接口明细数据");
        }else {
	        customerBalance.getFundAccounts().forEach(accountItem -> {
	        	for(UserAccountCardResponseDto userAccount: masterList) {
	        		if(accountItem.getAccountId().longValue() == userAccount.getFundAccountId()) {
	        			accountItem.setCardNo(userAccount.getCardNo());
	        			accountItem.setCardExist(userAccount.getCardExist());
	        			break;
	        		}
	        	}
	        });
        }

		return customerBalance;
	}

    @Override
    public BaseOutput presetPermissionByCard(String cardNo) {
        if (StrUtil.isBlank(cardNo)) {
            return BaseOutput.failure("参数丢失");
        }
        UserAccountCardResponseDto cardInfo = getByCardNo(cardNo);
        if (Objects.isNull(cardInfo)) {
            return BaseOutput.failure("卡账户数据不存在");
        }
        if (CardType.MASTER.getCode() != cardInfo.getCardType().intValue()) {
            return BaseOutput.failure("此功能只对主卡账户开放");
        }
        if (!DisableState.ENABLED.getCode().equals(cardInfo.getDisabledState())) {
            return BaseOutput.failure("卡账户已冻结，不支持办理此业务");
        }
        JSONObject params = new JSONObject();
        params.put("accountId", cardInfo.getFundAccountId());
        AccountPermissionResponseDto responseDto = GenericRpcResolver.resolver(payRpc.loadPermission(params), ServiceName.PAY);
        Set<Integer> exitsPermission = responseDto.getPermission();
        List<AccountAllPermission> allPermission = responseDto.getAllPermission();
        List<JSONObject> allPermissionObject = Lists.newArrayList();
        allPermission.forEach(t -> {
            JSONObject jsonObject = JSONObject.parseObject(JSONObject.toJSONString(t));
            jsonObject.put("selected", exitsPermission.contains(Integer.valueOf(Objects.toString(t.getCode(), "-1"))));
            allPermissionObject.add(jsonObject);
        });
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("accountId", cardInfo.getAccountId());
        jsonObject.put("customerName", cardInfo.getCustomerName());
        jsonObject.put("customerCertificateNumber", cardInfo.getCustomerCertificateNumber());
        jsonObject.put("permission", allPermissionObject);
        jsonObject.put("withdraw", responseDto.getWithdraw());
        jsonObject.put("trade", responseDto.getTrade());
        return BaseOutput.successData(jsonObject);
    }

    @Override
	public UserAccountCardResponseDto getForResetLoginPassword(UserAccountSingleQueryDto query) {
		UserAccountCardResponseDto single = accountQueryRpcResolver.findSingleWithoutValidate(query);
		
		if (CardStatus.RETURNED.getCode() == single.getCardState()) {
            throw new CardAppBizException(ResultCode.DATA_ERROR, "该卡已退卡，不能进行此操作");
        }
        if (CardStatus.LOSS.getCode() == single.getCardState()) {
            throw new CardAppBizException(ResultCode.DATA_ERROR, "该卡已挂失，不能进行此操作");
        }
        if (DisableState.DISABLED.getCode().equals(single.getDisabledState())) {
            throw new CardAppBizException(ResultCode.DATA_ERROR, String.format("该账户为%s状态，不能进行此操作", DisableState.DISABLED.getName()));
        }
        return single;
	}
}
