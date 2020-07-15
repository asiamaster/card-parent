package com.dili.card.rpc;

import com.dili.card.entity.tcc.UserCardDo;
import com.dili.ss.domain.BaseOutput;
import com.dili.tcc.common.Tcc;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

/**
 * @Auther: miaoguoxin
 * @Date: 2020/7/15 11:43
 * @Description:
 */

@FeignClient(name = "account-service", contextId = "testRpc",
        path = "api/tcc", url = "http://127.0.0.1:8186")
public interface TestTccRpc {

    /**
    * tcc测试
    * @author miaoguoxin
    * @date 2020/7/15
    */
    @Tcc
    @PostMapping("testTry")
    BaseOutput<Long> testTry(UserCardDo userCardDo);

    /**
    * 测试tcc confirm
    * @author miaoguoxin
    * @date 2020/7/15
    */
    @Tcc
    @PostMapping("testConfirm")
    BaseOutput<Long> testConfirm(UserCardDo userCardDo);

    /**
    *  测试cancel阶段
    * @author miaoguoxin
    * @date 2020/7/15
    */
    @Tcc
    @PostMapping("testCancel")
    BaseOutput<String> testCancel(UserCardDo userCardDo);
}
