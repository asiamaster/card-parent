package com.dili.card.rpc;

import java.util.List;

import com.dili.card.dto.CardRepoQueryParam;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

import com.dili.card.dto.BatchActivateCardDto;
import com.dili.card.dto.BatchCardAddStorageDto;
import com.dili.card.dto.CardStorageActivateDto;
import com.dili.card.dto.CardStorageDto;
import com.dili.ss.domain.BaseOutput;
import com.dili.ss.domain.PageOutput;

/**
 * @description： 卡片仓库服务接口
 *
 * @author ：WangBo
 * @time ：2020年7月17日上午10:22:18
 */
@FeignClient(name = "account-service", contextId = "cardStorgeRpc", path = "/api/account/cardStorage"
		, url = "${accountService.url:}" )
public interface CardStorageRpc {

	/**
	 * 仓库中卡片列表，包括状态及入库信息
	 *
	 * @return
	 */
	@PostMapping("/pageList")
	PageOutput<List<CardStorageDto>> pageList(CardStorageDto queryParam);

	/**
	*  根据卡号批量查询列表
	* @author miaoguoxin
	* @date 2020/12/3
	*/
	@PostMapping("/batchQueryByCardNo")
	BaseOutput<List<CardStorageDto>> listByCardNo(CardRepoQueryParam queryParam);

	/**
	 * 入库
	 */
	@PostMapping("/add")
	BaseOutput<?> addCard(CardStorageDto addParam);

	/**
	 * 批量入库
	 */
	@PostMapping("/batchAdd")
	BaseOutput<?> batchAddCard(BatchCardAddStorageDto batchInfo);

	/**
	 * 作库
	 */
	@PostMapping("/void")
	BaseOutput<?> voidCard(CardStorageDto param);

	/**
	 * 查询单条数据
	 */
	@PostMapping("/getCardStorageByCardNo")
	BaseOutput<CardStorageDto> getCardStorageByCardNo(CardStorageDto param);

	/**
	 * 批量激活,卡片库存状态从未使用到激活
	 *
	 * @author miaoguoxin
	 * @date 2020/7/29
	 */
	@PostMapping("/batchActivate")
	BaseOutput<?> batchActivate(BatchActivateCardDto dto);

	/**
	 * 激活，卡片库存状态从在用到激活
	 *
	 * @param dto 提供卡号即可
	 */
	@PostMapping("/activateCardByInUse")
	BaseOutput<?> activateCardByInUse(CardStorageActivateDto dto);

	/**
	 * 卡片库存数据删除，根据号段入库ID，如果有非激活状态的卡片则删除失败
	 */
	@PostMapping("/delByStorageInId")
	BaseOutput<?> delByStorageInId(CardStorageDto param);
}
