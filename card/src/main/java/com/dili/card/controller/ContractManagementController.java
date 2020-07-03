package com.dili.card.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.dili.card.dto.FundContractQueryDto;
import com.dili.card.dto.FundContractRequestDto;
import com.dili.card.dto.FundContractResponseDto;
import com.dili.card.service.IContractService;
import com.dili.card.validator.ConstantValidator;
import com.dili.ss.domain.BaseOutput;
import com.dili.ss.domain.PageOutput;

/**
 * 合同管理
 */
@Controller
@RequestMapping(value = "/contract")
public class ContractManagementController {
	@Autowired
	private IContractService iContractService;
	
    /**
     * 列表页面
     */
    @GetMapping("/list.html")
    public String listView() {
        return "contract/list";
    }
	
	/**
	 * 新增合同
	 */
	@PostMapping("/save.action")
	@ResponseBody
	public BaseOutput<Boolean> save(@RequestBody @Validated FundContractRequestDto fundContractRequest) {
		iContractService.save(fundContractRequest);
		return BaseOutput.success();
	}
	
	/**
	 * 列表合同
	 */
	@PostMapping("/page.action")
	@ResponseBody
	public PageOutput<List<FundContractResponseDto>> page(@RequestBody  @Validated(ConstantValidator.Page.class) FundContractQueryDto contractQueryDto) {
		return iContractService.page(contractQueryDto);
	}
	
	/**
	 * 详情合同
	 */
	@GetMapping("/detail.html")
	public String detail(@PathVariable Long id, ModelMap modelMap) {
		modelMap.put("detail", iContractService.detail(id));
		return "contract/detail";
	}
	
	/**
	 * 解除合同
	 */
	@PostMapping("/remove.action")
	@ResponseBody
	public BaseOutput<Boolean> remove(@RequestBody FundContractRequestDto fundContractRequest) {
		iContractService.remove(fundContractRequest);
		return BaseOutput.success();
	}
}
