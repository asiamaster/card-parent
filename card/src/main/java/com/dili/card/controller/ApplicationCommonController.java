package com.dili.card.controller;

import cn.hutool.core.util.IdUtil;
import com.dili.card.common.constant.CacheKey;
import com.dili.card.common.constant.ServiceName;
import com.dili.card.common.handler.IControllerHandler;
import com.dili.card.rpc.resolver.GenericRpcResolver;
import com.dili.card.service.IMiscService;
import com.dili.ss.domain.BaseOutput;
import com.dili.ss.redis.service.RedisUtil;
import com.dili.uap.sdk.domain.DataDictionaryValue;
import com.dili.uap.sdk.domain.UserTicket;
import com.dili.uap.sdk.rpc.DataDictionaryRpc;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @Auther: miaoguoxin
 * @Date: 2020/7/7 09:06
 * @Description:
 */
@Controller
@RequestMapping("/appCommon")
public class ApplicationCommonController implements IControllerHandler {
    private static Logger LOGGER = LoggerFactory.getLogger(ApplicationCommonController.class);
    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private IMiscService miscService;
    @Autowired
    private DataDictionaryRpc dataDictionaryRpc;

    /**
     * 获取防重复提交的token
     * @author miaoguoxin
     * @date 2020/7/7
     */
    @GetMapping("/duplicateToken.action")
    @ResponseBody
    public BaseOutput<String> getForbidDuplicateToken() {
        String id = IdUtil.fastSimpleUUID();
        String key = CacheKey.FORBID_DUPLICATE_TOKEN_PREFIX + id;
        redisUtil.increment(key, 1L);
        redisUtil.expire(key, 30, TimeUnit.MINUTES);
        LOGGER.info("获取到幂等token:{}", id);
        return BaseOutput.successData(id);
    }

    /**
     * 获取字典列表
     * @author miaoguoxin
     * @date 2020/7/30
     */
    @GetMapping("/dictList.action")
    @ResponseBody
    public BaseOutput<List<DataDictionaryValue>> getDataDictList(String code) {
        UserTicket userTicket = getUserTicket();
        BaseOutput<List<DataDictionaryValue>> listBaseOutput = dataDictionaryRpc.listDataDictionaryValueByDdCode(code);
        List<DataDictionaryValue> resolver = GenericRpcResolver.resolver(listBaseOutput, ServiceName.UAP);
        //只要已启用的
        List<DataDictionaryValue> collect = resolver.stream()
                .filter(d -> d.getState() != null && d.getState() == 1)
                .filter(d-> userTicket.getFirmId().equals(d.getFirmId()))
                .collect(Collectors.toList());
        return BaseOutput.successData(collect);
    }

    /**
     * 获取单个字典值
     * @author miaoguoxin
     * @date 2020/7/30
     */
    @GetMapping("/singleDict.action")
    @ResponseBody
    public BaseOutput<String> getSingleDataDict(String code) {
        UserTicket userTicket = getUserTicket();
        Long firmId = userTicket.getFirmId();
        return BaseOutput.successData(miscService.getSingleDictVal(code, firmId));
    }
}
