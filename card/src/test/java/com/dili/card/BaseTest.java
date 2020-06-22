package com.dili.card;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

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
}
