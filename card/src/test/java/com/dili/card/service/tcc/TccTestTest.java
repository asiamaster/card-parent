package com.dili.card.service.tcc;

import com.dili.card.BaseTest;
import com.dili.card.entity.tcc.UserCardDo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @Auther: miaoguoxin
 * @Date: 2020/7/15 14:04
 * @Description:
 */
class TccTestTest extends BaseTest {

    @Autowired
    private TccTest tccTest;

    @Test
    void testTcc() {
        UserCardDo userCardDo = new UserCardDo();
        userCardDo.setCardNo("12334567890");
        userCardDo.setAccountId(1L);
        userCardDo.setCardNo("test1234");
        userCardDo.setCategory(1);
        userCardDo.setType(10);
        userCardDo.setLast(0);
        userCardDo.setState(1);
        userCardDo.setVersion(1);
        userCardDo.setCreatorId(73L);
        userCardDo.setCreator("王波");
        userCardDo.setFirmId(1L);
        userCardDo.setFirmName("集团");
        userCardDo.setCreateTime(LocalDateTime.now());
        userCardDo.setModifyTime(LocalDateTime.now());
        tccTest.testTcc(userCardDo);
    }
}
