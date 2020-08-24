package com.dili.card.controller;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSONObject;
import com.dili.card.common.handler.IControllerHandler;
import com.dili.card.dto.FundContractPrintDto;
import com.dili.card.dto.FundContractQueryDto;
import com.dili.card.dto.FundContractRequestDto;
import com.dili.card.dto.FundContractResponseDto;
import com.dili.card.schedule.ContractScheduleHandler;
import com.dili.card.service.IContractService;
import com.dili.card.validator.ConstantValidator;
import com.dili.customer.sdk.domain.Customer;
import com.dili.customer.sdk.domain.dto.CustomerQueryInput;
import com.dili.ss.domain.BaseOutput;
import com.dili.uap.sdk.domain.UserTicket;

/**
 * 合同管理
 */
@Controller
@RequestMapping(value = "/contract")
public class ContractManagementController implements IControllerHandler {
	
	private static final Logger log = LoggerFactory.getLogger(ContractManagementController.class);

    @Autowired
    private IContractService iContractService;
    @Autowired
    private ContractScheduleHandler contractScheduleHandler;

    /**
     * 列表页面
     */
    @GetMapping("/list.html")
    public String listView() {
        return "contract/list";
    }

    /**
     * 详情合同
     */
    @GetMapping("/detail.html")
    public String detail(Long id, ModelMap modelMap) {
        modelMap.put("detail", iContractService.detail(id));
        return "contract/detail";
    }
    
    /**
     * 合同预览
     */
    @GetMapping("/preview.html")
    public String preview(Long id, ModelMap modelMap) {
        modelMap.put("detail", iContractService.detail(id));
        return "contract/preview";
    }

    /**
     * 解除页面
     */
    @GetMapping("/remove.html")
    public String removeToPage(Long id, ModelMap modelMap) {
        modelMap.put("detail", iContractService.detail(id));
        return "contract/remove";
    }

    /**
     * 新增合同
     */
    @PostMapping("/save.action")
    @ResponseBody
    public BaseOutput<Boolean> save(@RequestBody @Validated FundContractRequestDto fundContractRequest) {
    	log.info("新增合同*****{}", JSONObject.toJSONString(fundContractRequest));
    	iContractService.save(fundContractRequest);
        return BaseOutput.success();
    }

    /**
     * 列表合同
     */
    @PostMapping("/page.action")
    @ResponseBody
    public Map<String, Object> page(@Validated(ConstantValidator.Page.class) FundContractQueryDto contractQueryDto) {
    	log.info("列表合同*****{}", JSONObject.toJSONString(contractQueryDto));
    	return successPage(iContractService.page(contractQueryDto));
    }


    /**
     * 列表查询合同
     */
    @PostMapping("/list.action")
    @ResponseBody
    public BaseOutput<List<FundContractResponseDto>> list(@RequestBody @Validated(ConstantValidator.Query.class) FundContractQueryDto contractQueryDto) {
    	log.info("列表查询合同*****{}", JSONObject.toJSONString(contractQueryDto));
    	return BaseOutput.successData(iContractService.list(contractQueryDto));
    }
    
    /**
     * 查询合同根据合同人
     */
    @PostMapping("/findActiveContractByAccountId.action")
    @ResponseBody
    public BaseOutput<FundContractResponseDto> findByAccountId(@RequestBody @Validated(ConstantValidator.Query.class) FundContractQueryDto contractQueryDto) {
    	log.info("查询合同*****{}", JSONObject.toJSONString(contractQueryDto));
    	return BaseOutput.successData(iContractService.findActiveContractByAccountId(contractQueryDto));
    }

    /**
     * 解除合同
     */
    @PostMapping("/remove.action")
    @ResponseBody
    public BaseOutput<Boolean> remove(@RequestBody FundContractRequestDto fundContractRequest) {
    	log.info("解除合同*****{}", JSONObject.toJSONString(fundContractRequest));
    	iContractService.remove(fundContractRequest);
        return BaseOutput.success();
    }
    
    /**
     * 更新合同状态
     */
    @PostMapping("/updateStateTask.action")
    @ResponseBody
    public BaseOutput<Boolean> updateStateTask() {
    	log.info("更新合同状态*****");
    	contractScheduleHandler.execute();
        return BaseOutput.success();
    }
    
    /**
     * 查询客户列表
     * @param keyword
     * @return
     */
    @SuppressWarnings("unchecked")
	@RequestMapping(value = "/findCustomersByKeyword.action")
    @ResponseBody
    public BaseOutput<List<Customer>> findCustomersByKeyword(String keyword) {
    	log.info("查询客户列表*****{}", keyword);
        CustomerQueryInput query = new CustomerQueryInput();
        UserTicket userTicket = getUserTicket();
        query.setMarketId(userTicket.getFirmId());
        query.setKeyword(keyword);
        return BaseOutput.success().setData(iContractService.findCustomers(query));
    }
    
    /**
     * 打印合同
     */
    @PostMapping("/print.action")
    @ResponseBody
    public BaseOutput<FundContractPrintDto> print(@RequestBody FundContractRequestDto fundContractRequest) {
    	log.info("打印合同*****{}", JSONObject.toJSONString(fundContractRequest));
        return BaseOutput.successData(iContractService.print(fundContractRequest));
    }
    
    /**
     * 签名图片上传
     */
    @PostMapping("/upload.action")
    @ResponseBody
    public BaseOutput<String> upload(@RequestPart("file") MultipartFile multipartFile) {
    	log.info("签名图片上传");
        return BaseOutput.successData(iContractService.upload(multipartFile));
    }
    
    /**
     * 合同确认
     */
    @PostMapping("/confirm.action")
    @ResponseBody
    public BaseOutput<String> confirm(@RequestBody FundContractRequestDto fundContractRequest) {
    	log.info("签名合同确认");
        return BaseOutput.successData(fundContractRequest.getContractNo());
    }
    
    /**
     * 返回上一步
     */
    @PostMapping("/back.action")
    @ResponseBody
    public BaseOutput<String> back(@RequestBody FundContractRequestDto fundContractRequest) {
    	log.info("签名合同返回上一步");
        return BaseOutput.successData(fundContractRequest.getContractNo());
    }
}
