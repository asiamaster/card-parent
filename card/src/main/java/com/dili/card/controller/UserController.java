package com.dili.card.controller;

import com.dili.ss.domain.BaseOutput;
import com.dili.ss.dto.DTOUtils;
import com.dili.uap.sdk.domain.User;
import com.dili.uap.sdk.domain.UserTicket;
import com.dili.uap.sdk.domain.dto.UserQuery;
import com.dili.uap.sdk.rpc.UserRpc;
import com.dili.uap.sdk.session.SessionContext;
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
public class UserController {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);

    @Resource
    private UserRpc userRpc;

    /**
     * 查询客户列表
     * @param query
     * @return
     */
    @RequestMapping(value = "/list.action")
    @ResponseBody
    public BaseOutput<List<User>> list(UserQuery query) {
        try {
            UserTicket userTicket = getUserTicket();
            query.setFirmCode(userTicket.getFirmCode());
            return userRpc.list(query);
        } catch (Exception e) {
            LOGGER.error("list", e);
            return BaseOutput.failure();
        }
    }

    /**
     * 获取登录用户信息 如为null则new一个，以免空指针
     * @return
     */
    private UserTicket getUserTicket() {
        UserTicket userTicket = SessionContext.getSessionContext().getUserTicket();
        return userTicket != null ? userTicket : DTOUtils.newInstance(UserTicket.class);
    }
}
