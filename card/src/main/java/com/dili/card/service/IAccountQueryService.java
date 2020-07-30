package com.dili.card.service;

import java.util.List;

import com.dili.card.dto.AccountDetailResponseDto;
import com.dili.card.dto.AccountListResponseDto;
import com.dili.card.dto.AccountSimpleResponseDto;
import com.dili.card.dto.AccountWithAssociationResponseDto;
import com.dili.card.dto.CardRequestDto;
import com.dili.card.dto.UserAccountCardQuery;
import com.dili.card.dto.UserAccountCardResponseDto;
import com.dili.ss.domain.PageOutput;

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
     * 根据卡号查询（只查询卡账户信息）
     * @author miaoguoxin
     * @date 2020/7/2
     */
    UserAccountCardResponseDto getByCardNo(String cardNo);

    /**
    * 一般的业务操作需要校验一些状态（如 挂失状态、主副卡等）
    * @author miaoguoxin
    * @date 2020/7/30
    */
    UserAccountCardResponseDto getByAccountIdForGenericOp(Long accountId);

    /**
    * 根据卡号查询 （不校验非正常状态）
    * @param
    * @return
    * @author miaoguoxin
    * @date 2020/7/30
    */
    UserAccountCardResponseDto getByCardNoWithoutValidate(String cardNo);


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
