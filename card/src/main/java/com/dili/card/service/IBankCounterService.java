package com.dili.card.service;

import com.dili.card.dto.BankCounterQuery;
import com.dili.card.dto.BankCounterRequestDto;
import com.dili.card.dto.BankCounterResponseDto;
import com.dili.ss.domain.BaseOutput;
import com.dili.ss.domain.PageOutput;

import java.util.List;

/**
 * @Auther: miaoguoxin
 * @Date: 2020/12/9 15:44
 * @Description:
 */
public interface IBankCounterService {

    /**
    *  获取分页
    * @author miaoguoxin
    * @date 2020/12/9
    */
    PageOutput<List<BankCounterResponseDto>> getPage(BankCounterQuery param);

    /**
    * 新增存款
    * @author miaoguoxin
    * @date 2020/12/10
    */
    void add(BankCounterRequestDto requestDto);
}
