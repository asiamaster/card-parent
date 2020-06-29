package com.dili.card.controller;

import com.dili.card.common.handler.IControllerHandler;
import com.dili.ss.domain.BaseOutput;
import com.dili.ss.dto.DTOUtils;
import com.dili.uap.sdk.domain.User;
import com.dili.uap.sdk.domain.UserTicket;
import com.dili.uap.sdk.domain.dto.UserQuery;
import com.dili.uap.sdk.rpc.UserRpc;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.List;

/**
 * 系统用户controller
 */
@Controller
@RequestMapping(value = "/user")
public class UserController implements IControllerHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);

    @Resource
    private UserRpc userRpc;

    /**
     * 查询客户列表
     * @param keyword
     * @return
     */
    @RequestMapping(value = "/listByKeyword.action")
    @ResponseBody
    public BaseOutput<List<User>> listByKeyword(String keyword) {
        try {
            UserQuery query = DTOUtils.newDTO(UserQuery.class);
            UserTicket userTicket = getUserTicket();
            query.setFirmCode(userTicket.getFirmCode());
            query.setKeyword(keyword);
            return userRpc.listByExample(query);
        } catch (Exception e) {
            LOGGER.error("list", e);
            return BaseOutput.failure();
        }
    }
}
