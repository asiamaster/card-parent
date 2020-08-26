package com.dili.card.controller;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.dili.card.common.handler.IControllerHandler;
import com.dili.card.dto.BusinessRecordResponseDto;
import com.dili.card.dto.SerialDto;
import com.dili.card.dto.SerialQueryDto;
import com.dili.card.entity.BusinessRecordDo;
import com.dili.card.entity.SerialRecordDo;
import com.dili.card.rpc.resolver.SerialRecordRpcResolver;
import com.dili.card.service.ISerialService;
import com.dili.card.util.DateUtil;
import com.dili.ss.constant.ResultCode;
import com.dili.ss.domain.BaseOutput;
import com.dili.ss.domain.PageOutput;
import com.dili.uap.sdk.domain.UserTicket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 业务记录  以及 操作流水  相关controller
 */
@Controller
@RequestMapping(value = "/serial")
public class SerialController implements IControllerHandler {
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
     * @param serialQueryDto
     * @return
     */
    @RequestMapping(value = "/account/listPage.action")
    @ResponseBody
    public Map<String, Object> listPage(SerialQueryDto serialQueryDto) {
    	LOGGER.info("分页加载操作流水*****{}", JSONObject.toJSONString(serialQueryDto));
        Map<String, Object> result = new HashMap<>();
        checkAndSetDefaultTimeRange(serialQueryDto);
        UserTicket userTicket = getUserTicket();
        serialQueryDto.setFirmId(userTicket.getFirmId());
        serialQueryDto.setSerialSort("operate_time desc, id desc");
        PageOutput<List<SerialRecordDo>> pageOutput = serialRecordRpcResolver.listPage(serialQueryDto);
        if (pageOutput.isSuccess()) {
        	result.put("code", ResultCode.OK);
            result.put("rows", pageOutput.getData());
            result.put("total", pageOutput.getTotal());
        }
        return result;
    }

    /**
     * 检查并设置默认时间 当时间范围为空时，默认时间范围 当前时间年-1 天+1 - 当前时间
     * @param serialQueryDto
     */
    private void checkAndSetDefaultTimeRange(SerialQueryDto serialQueryDto) {
        if (!StrUtil.isBlank(serialQueryDto.getOperateTimeStart()) || !StrUtil.isBlank(serialQueryDto.getOperateTimeEnd())) {
            return;
        }
        LocalDateTime localDateTime = DateUtil.nowDateTime();
        serialQueryDto.setOperateTimeEnd(DateUtil.formatDateTime(localDateTime, "yyyy-MM-dd HH:mm:ss"));
        serialQueryDto.setOperateTimeStart(DateUtil.formatDateTime(localDateTime.minus(1, ChronoUnit.YEARS).plus(1, ChronoUnit.DAYS), "yyyy-MM-dd HH:mm:ss"));
    }

    /**
     * 获取操作员当前账期内用于补打的操作记录列表 默认20条，按操作时间倒序排列
     * @param serialQueryDto
     * @return
     */
    @RequestMapping(value = "/business/cycleReprintList.action")
    @ResponseBody
    public BaseOutput<List<BusinessRecordDo>> cycleReprintList(@RequestBody SerialQueryDto serialQueryDto) {
    	LOGGER.info("获取补打的操作记录*****{}", JSONObject.toJSONString(serialQueryDto));
    	UserTicket userTicket = getUserTicket();
        serialQueryDto.setOperatorId(userTicket.getId());
        serialQueryDto.setFirmId(userTicket.getFirmId());
        List<BusinessRecordDo> itemList = serialService.cycleReprintList(serialQueryDto);
        return BaseOutput.successData(itemList);
    }

    /**
     * 查询客户今日充值记录
     * @param serialQueryDto
     * @return
     */
    @RequestMapping(value = "/business/todayChargeList.action")
    @ResponseBody
    public BaseOutput<List<BusinessRecordDo>> todayChargeList(@RequestBody SerialQueryDto serialQueryDto) {
    	LOGGER.info("查询客户今日充值记录*****{}", JSONObject.toJSONString(serialQueryDto));
    	if (serialQueryDto.getAccountId() == null) {
            return BaseOutput.failure("账户ID为空");
        }
        UserTicket userTicket = getUserTicket();
        serialQueryDto.setFirmId(userTicket.getFirmId());
        List<BusinessRecordDo> itemList = serialService.todayChargeList(serialQueryDto);
        return BaseOutput.successData(itemList);
    }


    /**
     * 办理成功后修改业务单状态 用于修复数据
     * @param serialDto
     * @return
     */
    @RequestMapping(value = "/business/handleSuccess/{flag}")
    @ResponseBody
    public BaseOutput<?> handleSuccess(@RequestBody SerialDto serialDto, @PathVariable boolean flag) {
    	LOGGER.info("办理成功后修改业务单状态*****{}", JSONObject.toJSONString(serialDto));
    	serialService.handleSuccess(serialDto, flag);
        return BaseOutput.success();
    }


    /**
     * 办理失败后修改业务单状态 用于修复数据
     * @param serialDto
     * @return
     */
    @RequestMapping(value = "/business/handleFailure")
    @ResponseBody
    public BaseOutput<?> handleFailure(@RequestBody SerialDto serialDto) {
    	LOGGER.info("办理失败后修改业务单状态*****{}", JSONObject.toJSONString(serialDto));
        serialService.handleFailure(serialDto);
        return BaseOutput.success();
    }


    /**
     * 保存账户流水 用于修复数据
     * @param serialDto
     * @return
     */
    @RequestMapping(value = "/account/saveSerial")
    @ResponseBody
    public BaseOutput<?> saveSerial(@RequestBody SerialDto serialDto) {
    	LOGGER.info("保存账户流水*****{}", JSONObject.toJSONString(serialDto));
        serialService.saveSerialRecord(serialDto);
        return BaseOutput.success();
    }
    /**
    * 业务日志分页
    * @author miaoguoxin
    * @date 2020/7/1
    */
    @PostMapping("/business/page.action")
    @ResponseBody
    public Map<String, Object> businessPage(SerialQueryDto queryDto){
    	LOGGER.info("业务日志分页*****{}", JSONObject.toJSONString(queryDto));
        PageOutput<List<BusinessRecordResponseDto>> lists = serialService.queryPage(queryDto);
        return successPage(lists);
    }
}
