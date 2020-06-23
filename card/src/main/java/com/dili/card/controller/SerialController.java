package com.dili.card.controller;

import cn.hutool.core.util.StrUtil;
import com.dili.card.dto.SerialDto;
import com.dili.card.entity.SerialRecordDo;
import com.dili.card.rpc.resolver.SerialRecordRpcResolver;
import com.dili.ss.domain.EasyuiPageOutput;
import com.dili.ss.domain.PageOutput;
import com.dili.ss.dto.DTOUtils;
import com.dili.uap.sdk.domain.UserTicket;
import com.dili.uap.sdk.session.SessionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * 业务记录  以及 操作流水  相关controller
 */
@Controller
@RequestMapping(value = "/serial")
public class SerialController {
    private static final Logger LOGGER = LoggerFactory.getLogger(SerialController.class);

    @Resource
    private SerialRecordRpcResolver serialRecordRpcResolver;
    /**
     * 跳转到操作流水页面
     * @return
     */
    @RequestMapping(value = "/forwardList.html")
    public String forwardList() {
        return "serial/index";
    }

    /**
     * 分页加载操作流水
     * @param serialDto
     * @return
     */
    @RequestMapping(value = "/listPage.action")
    @ResponseBody
    public String listPage(SerialDto serialDto) {
        try {
            UserTicket userTicket = getUserTicket();
            serialDto.setFirmId(userTicket.getFirmId());
            if (StrUtil.isBlank(serialDto.getSort())) {
                serialDto.setSort("operate_time");
            }
            if (StrUtil.isBlank(serialDto.getOrder())) {
                serialDto.setSort("desc");
            }
            PageOutput<List<SerialRecordDo>> pageOutput = serialRecordRpcResolver.listPage(serialDto);
            if (pageOutput.isSuccess()) {
                return new EasyuiPageOutput(pageOutput.getTotal(), pageOutput.getData()).toString();
            }
        } catch (Exception e) {
            LOGGER.error("listPage", e);
        }
        return new EasyuiPageOutput(0, new ArrayList(0)).toString();
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
