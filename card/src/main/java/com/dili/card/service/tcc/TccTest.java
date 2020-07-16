package com.dili.card.service.tcc;

import com.dili.card.entity.tcc.UserCardDo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @Auther: miaoguoxin
 * @Date: 2020/7/15 10:35
 * @Description:
 */
@Component
public class TccTest {
    @Autowired
    private TccTestService tccTestService;

    /**
    * 测试
    * @author miaoguoxin
    * @date 2020/7/15
    */
    public void testTcc(UserCardDo userCardDo){
        Long s = tccTestService.doTcc(userCardDo);
    }
}
