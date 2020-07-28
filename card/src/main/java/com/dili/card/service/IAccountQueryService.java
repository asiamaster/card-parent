package com.dili.card.service;

import com.dili.card.dto.AccountCustomerDto;
import com.dili.card.dto.AccountDetailResponseDto;
import com.dili.card.dto.AccountListResponseDto;
import com.dili.card.dto.AccountSimpleResponseDto;
import com.dili.card.dto.AccountWithAssociationResponseDto;
import com.dili.card.dto.CardRequestDto;
import com.dili.card.dto.UserAccountCardQuery;
import com.dili.card.dto.UserAccountCardResponseDto;
import com.dili.ss.domain.PageOutput;

import java.util.List;

/**
 * @Auther: miaoguoxin
 * @Date: 2020/6/22 13:59
 * @Description:
 */
public interface IAccountQueryService {

    /**
     * 分页多条件查询
     * @author miaoguoxin
     * @date 2020/6/22
     */
    PageOutput<List<AccountListResponseDto>> getPage(UserAccountCardQuery param);

    /**
     * 条件查询
     * @author miaoguoxin
     * @date 2020/7/28
     */
    List<UserAccountCardResponseDto> getList(UserAccountCardQuery param);

    /**
     * 根据卡号查询详情
     * @author miaoguoxin
     * @date 2020/6/28
     */
    AccountDetailResponseDto getDetailByCardNo(String cardNo);

    /**
     * 根据卡号查询客户信息及账户信息
     * @param cardNo
     * @return
     */
    AccountCustomerDto getAccountCustomerByCardNo(String cardNo);

    /**
     * 根据卡号查询（只查询卡账户信息）
     * @author miaoguoxin
     * @date 2020/7/2
     */
    UserAccountCardResponseDto getByCardNo(String cardNo);

    /**
     * 根据卡号查询（只查询卡账户信息）
     * @author miaoguoxin
     * @param validateLevel 账户校验等级{@link com.dili.card.validator.AccountValidator}
     * @date 2020/7/2
     */
    UserAccountCardResponseDto getByCardNo(String cardNo, Integer validateLevel);

    /**
     *  根据accountId，会进行合法性校验
     * @author miaoguoxin
     * @date 2020/7/6
     */
    UserAccountCardResponseDto getByAccountId(CardRequestDto requestDto);

    /**
     *  根据accountId查询
     * @author miaoguoxin
     * @date 2020/7/6
     */
    UserAccountCardResponseDto getByAccountId(Long accountId);

    /**
     * 根据accountId获取包含关联卡
     * @author miaoguoxin
     * @date 2020/7/28
     */
    AccountWithAssociationResponseDto getAssociationByAccountId(Long accountId);


    /**
     * 查询账户信息（包含余额）
     * @author miaoguoxin
     * @date 2020/7/7
     */
    AccountSimpleResponseDto getByCardNoWithBalance(String cardNo);

}
