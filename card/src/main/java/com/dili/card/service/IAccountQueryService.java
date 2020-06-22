package com.dili.card.service;

import com.dili.card.dto.AccountListResponseDto;
import com.dili.card.dto.UserAccountCardQuery;
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
    * @param
    * @return
    * @author miaoguoxin
    * @date 2020/6/22
    */
    PageOutput<List<AccountListResponseDto>> getPage(UserAccountCardQuery param);
}
