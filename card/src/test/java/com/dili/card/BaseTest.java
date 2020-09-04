package com.dili.card;

import com.alibaba.fastjson.JSON;
import com.dili.ss.redis.service.RedisUtil;
import com.dili.uap.sdk.session.SessionConstants;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.Set;

/**
 * @Auther: miaoguoxin
 * @Date: 2020/6/22 16:10
 * @Description:
 */
@SpringBootTest
@AutoConfigureMockMvc
public class BaseTest {
    //spy方法需要使用doReturn方法才不会调用实际方法。
    protected static Logger LOGGER = LoggerFactory.getLogger(BaseTest.class);
    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Test
    public void testRedis(){
        Set<String> members = redisTemplate.opsForSet().members(SessionConstants.USER_MENU_URL_KEY + "93");
        System.out.println(JSON.toJSONString(members));
    }

}
