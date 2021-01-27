package com.dili.card.service;

import com.dili.card.dto.*;
import com.dili.card.dto.pay.CustomerBalanceResponseDto;
import com.dili.ss.domain.BaseOutput;
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
     * 查询详情
     * @author miaoguoxin
     * @date 2020/6/28
     */
    AccountDetailResponseDto getDetail(Long cardPkId, Long accountPkId);

    /**
    * 查询详情(扩展)
    * @author miaoguoxin
    * @date 2020/10/19
    */
    AccountDetailResponseDto getDetailEx(Long cardPkId, Long accountPkId);

    /**
     * 根据卡号查询（只查询卡账户信息）
     * @author miaoguoxin
     * @date 2020/7/2
     */
    UserAccountCardResponseDto getByCardNo(String cardNo);


    /**
     * 根据卡号查询 （不校验非正常状态）
     * @author miaoguoxin
     * @date 2020/7/30
     */
    UserAccountCardResponseDto getByCardNoWithoutValidate(String cardNo);

    /**
     * 根据账号id查询 （不校验非正常状态）
     * @author miaoguoxin
     * @date 2020/7/30
     */
    UserAccountCardResponseDto getByAccountIdWithoutValidate(Long accountId);


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
    AccountWithAssociationResponseDto getAssociationByAccountId(Long accountId, int excludeUnusualState);

    /**
     * 查询账户信息（包含余额）
     * @author miaoguoxin
     * @date 2020/7/7
     */
    AccountSimpleResponseDto getByCardNoWithBalance(String cardNo);


    /**
     * 解挂操作查询
     * @author miaoguoxin
     * @date 2020/8/6
     */
    UserAccountCardResponseDto getForUnLostCard(UserAccountSingleQueryDto query);

    /**
     * 解锁操作查询
     * @author miaoguoxin
     * @date 2020/8/6
     */
    UserAccountCardResponseDto getForUnLockCard(UserAccountSingleQueryDto query);
    
    /**
     * 重置密码查询操作,允许锁定状态，排除退卡、挂失、账户禁用
     * @author miaoguoxin
     * @date 2020/8/6
     */
    UserAccountCardResponseDto getForResetLoginPassword(UserAccountSingleQueryDto query);

    /**
     * 查询主卡的关联卡列表
     * @author miaoguoxin
     * @date 2020/10/14
     */
    List<AccountWithAssociationResponseDto> getMasterAssociationList(Long customerId);
    
    
    /**
     * 根据客户ID获取客户下所有账户，及资金情况(包括可用资金、冻结资金)
     */
    CustomerBalanceResponseDto getAccountFundByCustomerId(Long customerId);

    /**
     * 预设置卡账户权限
     * @param cardNo 卡号
     * @return
     */
    BaseOutput presetPermissionByCard(String cardNo);

}

