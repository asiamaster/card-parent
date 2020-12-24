package com.dili.card.controller.mobile;

import com.dili.card.dto.AccountWithAssociationResponseDto;
import com.dili.card.dto.pay.CustomerBalanceResponseDto;
import com.dili.card.service.IAccountQueryService;
import com.dili.card.util.AssertUtils;
import com.dili.ss.domain.BaseOutput;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 移动端账户查询
 * 
 * @Auther: miaoguoxin
 * @Date: 2020/10/14 09:26
 */
@RestController
@RequestMapping("/api/mobile/accountQuery")
public class AccountQueryController {

	private static final Logger log = LoggerFactory.getLogger(AccountQueryController.class);

	@Autowired
	private IAccountQueryService accountQueryService;

	/**
	 * 关联卡列表
	 * 
	 * @author miaoguoxin
	 * @date 2020/10/14
	 */
	@GetMapping("/associationList.action")
	public BaseOutput<List<AccountWithAssociationResponseDto>> getAssociationList(Long customerId) {
		AssertUtils.notNull(customerId, "客户id不能为空");
		return BaseOutput.successData(accountQueryService.getMasterAssociationList(customerId));
	}

	/**
	 * @param cardNo
	 * @param firmId
	 * @return
	 */
	@GetMapping("/getAccountFundByCustomerId.action")
	public BaseOutput<CustomerBalanceResponseDto> getAccountFundByCustomerId(Long customerId, Long firmId) {
		log.info("getAccountFundByCustomerId请求参数:{}->>>>{},{}", customerId, firmId);
		AssertUtils.notNull(customerId, "卡号不能为空");
		AssertUtils.notNull(firmId, "市场ID不能为空");
		CustomerBalanceResponseDto customerBalance = accountQueryService.getAccountFundByCustomerId(customerId);
		return BaseOutput.successData(customerBalance);
	}
}
