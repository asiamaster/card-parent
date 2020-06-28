package com.dili.card.controller;

import cn.hutool.core.util.StrUtil;
import com.dili.card.common.holder.IUserTicketHolder;
import com.dili.card.dto.SerialDto;
import com.dili.card.entity.BusinessRecordDo;
import com.dili.card.entity.SerialRecordDo;
import com.dili.card.rpc.resolver.SerialRecordRpcResolver;
import com.dili.card.service.ISerialService;
import com.dili.ss.domain.BaseOutput;
import com.dili.ss.domain.EasyuiPageOutput;
import com.dili.ss.domain.PageOutput;
import com.dili.uap.sdk.domain.UserTicket;
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
public class SerialController implements IUserTicketHolder {
    private static final Logger LOGGER = LoggerFactory.getLogger(SerialController.class);

    @Resource
    private SerialRecordRpcResolver serialRecordRpcResolver;
    @Resource
    private ISerialService serialService;
    /**
     * 跳转到操作流水页面
     * @return
     */
    @RequestMapping(value = "/account/forwardList.html")
    public String forwardList() {
        return "serial/index";
    }

    /**
     * 分页加载操作流水
     * @param serialDto
     * @return
     */
    @RequestMapping(value = "/account/listPage.action")
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
     * 获取操作员当前账期内用于补打的操作记录列表 默认20条，按操作时间倒序排列
     * @param serialDto
     * @return
     */
    @RequestMapping(value = "/business/cycleReprintList.action")
    @ResponseBody
    public BaseOutput<List<BusinessRecordDo>> cycleReprintList(SerialDto serialDto) {
        try {
            UserTicket userTicket = getUserTicket();
            serialDto.setOperatorId(userTicket.getId());
            serialDto.setFirmId(userTicket.getFirmId());
            List<BusinessRecordDo> itemList = serialService.cycleReprintList(serialDto);
            return BaseOutput.success().setData(itemList);
        } catch (Exception e) {
            LOGGER.error("reprintList", e);
            return BaseOutput.failure();
        }
    }

    /**
     * 查询客户今日充值记录
     * @param serialDto
     * @return
     */
    @RequestMapping(value = "/business/todayChargeList.action")
    @ResponseBody
    public BaseOutput<List<BusinessRecordDo>> todayChargeList(SerialDto serialDto) {
        try {
            if (serialDto.getAccountId() == null) {
                return BaseOutput.failure("账户ID为空");
            }
            List<BusinessRecordDo> itemList = serialService.todayChargeList(serialDto);
            return BaseOutput.success().setData(itemList);
        } catch (Exception e) {
            LOGGER.error("todayChargeList", e);
            return BaseOutput.failure();
        }
    }
}
