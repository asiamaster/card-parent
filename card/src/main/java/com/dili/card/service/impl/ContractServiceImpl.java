package com.dili.card.service.impl;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import com.dili.card.common.constant.Constant;
import com.dili.card.common.constant.ServiceName;
import com.dili.card.dao.IFundConsignorDao;
import com.dili.card.dao.IFundContractDao;
import com.dili.card.dto.FundConsignorDto;
import com.dili.card.dto.FundContractPrintDto;
import com.dili.card.dto.FundContractQueryDto;
import com.dili.card.dto.FundContractRequestDto;
import com.dili.card.dto.FundContractResponseDto;
import com.dili.card.dto.UserAccountCardResponseDto;
import com.dili.card.dto.UserAccountSingleQueryDto;
import com.dili.card.entity.FundConsignorDo;
import com.dili.card.entity.FundContractDo;
import com.dili.card.exception.CardAppBizException;
import com.dili.card.exception.ErrorCode;
import com.dili.card.rpc.resolver.AccountQueryRpcResolver;
import com.dili.card.rpc.resolver.CustomerRpcResolver;
import com.dili.card.rpc.resolver.DataDictionaryRpcResovler;
import com.dili.card.rpc.resolver.GenericRpcResolver;
import com.dili.card.rpc.resolver.UidRpcResovler;
import com.dili.card.service.IAccountQueryService;
import com.dili.card.service.IContractService;
import com.dili.card.service.IFileUpDownloadService;
import com.dili.card.type.BizNoType;
import com.dili.card.type.CardStatus;
import com.dili.card.type.ContractState;
import com.dili.card.type.CustomerState;
import com.dili.card.type.DisableState;
import com.dili.card.util.PageUtils;
import com.dili.customer.sdk.domain.Customer;
import com.dili.customer.sdk.domain.dto.CustomerQueryInput;
import com.dili.customer.sdk.rpc.CustomerRpc;
import com.dili.ss.constant.ResultCode;
import com.dili.ss.domain.PageOutput;
import com.dili.ss.util.DateUtils;
import com.dili.uap.sdk.domain.UserTicket;
import com.dili.uap.sdk.session.SessionContext;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;

import cn.hutool.core.codec.Base64;

@Service
public class ContractServiceImpl implements IContractService {

	@Autowired
	private IFundConsignorDao fundConsignorDao;
	@Autowired
	private IFundContractDao contractDao;
	@Autowired
	private UidRpcResovler uidRpcResovler;
	@Autowired
	private CustomerRpcResolver customerRpcResolver;
	@Autowired
	private AccountQueryRpcResolver accountQueryRpcResolver;
	@Autowired
	private IAccountQueryService accountQueryService;
	@Autowired
	private DataDictionaryRpcResovler dataDictionaryRpcResovler;
	@Autowired
	private CustomerRpc customerRpc;
	@Autowired
	private IFileUpDownloadService fileUpDownloadService;

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void save(FundContractRequestDto fundContractRequest) {
		
		// 校验委托人和被委托人身份证手机号校验
		this.validateIdCodeAndMobile(fundContractRequest);
		
		// 构建合同主体
		FundContractDo fundContract = this.buildContractEntity(fundContractRequest);
		
		// 构建被委托人主体
		List<FundConsignorDo> consignors = this.buildConsignorEntities(fundContractRequest);
		
		// 保存委托人数据
		contractDao.save(fundContract);
		
		// 保存被委托人数据
		fundConsignorDao.saveBatch(consignors);
	}

	@Override
	public List<FundContractResponseDto> list(FundContractQueryDto contractQueryDto) {
		
		// 构建查询条件
		this.buildQueryContractConditon(contractQueryDto);
		
		// 查询条件
		List<FundContractDo> fundContracts = contractDao.findEntityByCondition(contractQueryDto);
		
		// 数据转换
		return this.buildPageResponseContracts(fundContracts);
	}

	@Override
	public PageOutput<List<FundContractResponseDto>> page(FundContractQueryDto contractQueryDto) {
		
		Page<?> page = PageHelper.startPage(contractQueryDto.getPage(), contractQueryDto.getRows());
		
		List<FundContractResponseDto> contractResponses = this.list(contractQueryDto);
		
		return PageUtils.convert2PageOutput(page, contractResponses);
	}

	@Override
	public void remove(FundContractRequestDto fundContractRequest) {
		
		FundContractDo fundContract = contractDao.getById(fundContractRequest.getId());
		
		if (fundContract == null) {
			throw new CardAppBizException(ResultCode.DATA_ERROR, "该合同号不存在");
		}
		if (ContractState.REMOVED.getCode() == fundContract.getState()) {
			return;
		}
		
		FundContractDo updateFundContract = new FundContractDo();
		updateFundContract.setId(fundContractRequest.getId());
		updateFundContract.setState(ContractState.REMOVED.getCode());
		UserTicket userTicket = SessionContext.getSessionContext().getUserTicket();
		updateFundContract.setTerminater(userTicket.getRealName());
		updateFundContract.setTerminateNotes(fundContractRequest.getNotes());
		
		contractDao.update(updateFundContract);
	}

	@Override
	public FundContractResponseDto detail(Long id) {
		
		FundContractDo fundContract = contractDao.getById(id);
		
		if (fundContract == null) {
			throw new CardAppBizException(ResultCode.DATA_ERROR, "该合同号不存在");
		}
		
		Customer customer = customerRpcResolver.getWithNotNull(fundContract.getConsignorCustomerId(),
				fundContract.getFirmId());
		
		return this.buildContractResponse(fundContract, customer);
	}
	
	@Override
	public FundContractResponseDto preview(Long id) {
		
		FundContractResponseDto fundContract = this.detail(id);
		LocalDateTime createTime = fundContract.getCreateTime();
		fundContract.setPreviewTime( createTime.getYear() + " 年 " + createTime.getMonthValue() + " 月 " + createTime.getDayOfMonth() + " 日 ");
		buildImage(fundContract);
		return fundContract;
	}


	@Override
	@Transactional(rollbackFor = Exception.class)
	public void closeOverdueContract() {
		
		contractDao.closeOverdueContract();
		
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void activeOverdueContract() {
		
		contractDao.activeOverdueContract();
		
	}

	@Override
	public FundContractPrintDto print(Long id) {
		
		FundContractResponseDto fundContract = this.detail(id);
		fundContract.setSignatureImagePathByte(fileUpDownloadService.download(fundContract.getSignatureImagePath()));
		for (FundConsignorDto fundConsignorDto : fundContract.getConsignorDtos()) {
			fundConsignorDto.setSignatureImagePathByte(fileUpDownloadService.download(fundConsignorDto.getSignatureImagePath()));
		}
		return FundContractPrintDto.wrapperPrintDetai(fundContract);
	}

	@Override
	public FundContractResponseDto findActiveContractByAccountId(FundContractQueryDto contractQueryDto) {
		
		List<FundContractResponseDto> fundContractResponseDtos = this.list(contractQueryDto);
		
		if (CollectionUtils.isEmpty(fundContractResponseDtos)) {
			return null;
		}
		
		FundContractResponseDto fundContractResponseDto = fundContractResponseDtos.get(0);
		
		for (int i = 1; i < fundContractResponseDtos.size(); i++) {
			fundContractResponseDto.getConsignorDtos().addAll(fundContractResponseDtos.get(i).getConsignorDtos());
		}
		
		return fundContractResponseDto;
	}

	@Override
	public Customer findCustomers(CustomerQueryInput query) {
		
		Customer customer = GenericRpcResolver.resolver(customerRpc.getByCertificateNumber(query.getKeyword(), query.getMarketId()), ServiceName.CUSTOMER);
		
		if (customer == null) {
			throw new CardAppBizException(ResultCode.DATA_ERROR, "无相应客户信息");
		};
		
		if (!customer.getState().equals(CustomerState.VALID.getCode())) {
			throw new CardAppBizException(ResultCode.PARAMS_ERROR,
					"客户已" + CustomerState.getStateName(customer.getState()));
		}
		
		return customer;
	}

	@Override
	public String upload(MultipartFile multipartFile) {
		return fileUpDownloadService.upload(multipartFile);
	}

	/**
	 * 校验委托人和被委托人身份证校验
	 */
	private void validateIdCodeAndMobile(FundContractRequestDto fundContractRequest) {
		if (StringUtils.isBlank(fundContractRequest.getSignatureImagePath())) {
			throw new CardAppBizException(ResultCode.DATA_ERROR, "委托人未签名");
		}
		List<String> consigneeCustomerIdCodes = new ArrayList<String>();
		for (FundConsignorDto fundConsignorDto : fundContractRequest.getConsignors()) {
			if (fundContractRequest.getConsignorCustomerIDCode()
					.equalsIgnoreCase(fundConsignorDto.getConsigneeIdCode())) {
				throw new CardAppBizException(ResultCode.DATA_ERROR, "被委托人证件号与委托人证件号不能相同");
			}
			if (consigneeCustomerIdCodes.contains(fundConsignorDto.getConsigneeIdCode())) {
				throw new CardAppBizException(ResultCode.DATA_ERROR, "被委托人证件号不能相同");
			}
			if (StringUtils.isBlank(fundConsignorDto.getSignatureImagePath())) {
				throw new CardAppBizException(ResultCode.DATA_ERROR, "被委托人未完成签名");
			}
			consigneeCustomerIdCodes.add(fundConsignorDto.getConsigneeIdCode());
		}
	}

	/**
	 * 构建查询条件
	 */
	private void buildQueryContractConditon(FundContractQueryDto contractQueryDto) {
		UserTicket userTicket = SessionContext.getSessionContext().getUserTicket();
		contractQueryDto.setFirmId(userTicket.getFirmId());
		if (!StringUtils.isBlank(contractQueryDto.getCardNo())) {
			// 构建卡数据
			UserAccountCardResponseDto userAccountCard = null;
			try {
				userAccountCard = accountQueryService.getByCardNoWithoutValidate(contractQueryDto.getCardNo());
			} catch (CardAppBizException e) {
				//卡号不存在不需要报错 自己处理就好
				if (!ErrorCode.CARD_NO_NOT_EXIST.equals(e.getCode())) {
					throw e;
				}
			}catch (Exception e) {
				throw e;
			}
			// 如果卡为退卡状态不能卡出卡信息
			Long accountId = userAccountCard == null || userAccountCard.getCardState() == CardStatus.RETURNED.getCode() ? -1: userAccountCard.getAccountId();
			contractQueryDto.setConsignorAccountId(accountId);
		}
		// 过期时间构建
		if (contractQueryDto.getDays() != null && contractQueryDto.getDays() >= 0) {
			contractQueryDto.setExpirationTime(
					DateUtils.format(DateUtils.addDays(new Date(), contractQueryDto.getDays()), "yyyy-MM-dd"));
		}
	}

	/**
	 * 合同页面信息列表
	 */
	private List<FundContractResponseDto> buildPageResponseContracts(List<FundContractDo> fundContracts) {
		List<FundContractResponseDto> contractResponseDtos = new ArrayList<FundContractResponseDto>();
		if (CollectionUtils.isEmpty(fundContracts)) {
			return contractResponseDtos;
		}
		// 客户信息构建
		List<Long> customerIds = fundContracts.stream().map(c -> c.getConsignorCustomerId())
				.collect(Collectors.toList());
		Map<Long, Customer> customerMap = customerRpcResolver.findCustomerMapByCustomerIds(customerIds,
				fundContracts.get(0).getFirmId());
		// 合同信息构建
		for (FundContractDo fundContractDo : fundContracts) {
			contractResponseDtos.add(this.buildContractResponse(fundContractDo, customerMap.get(fundContractDo.getConsignorCustomerId())));
		}
		return contractResponseDtos;
	}

	/**
	 * 构建页面相应数据
	 */
	private FundContractResponseDto buildContractResponse(FundContractDo fundContractDo, Customer customer) {
		FundContractResponseDto contractResponseDto = new FundContractResponseDto();
		// 构建合同核心数据
		contractResponseDto.setId(fundContractDo.getId());
		contractResponseDto.setContractNo(fundContractDo.getContractNo());
		contractResponseDto.setCreator(fundContractDo.getCreator());
		contractResponseDto.setCreateTime(fundContractDo.getCreateTime());
		contractResponseDto.setStartTime(fundContractDo.getStartTime());
		contractResponseDto.setEndTime(fundContractDo.getEndTime());
		contractResponseDto.setTerminater(fundContractDo.getTerminater());
		contractResponseDto.setTerminateNotes(fundContractDo.getTerminateNotes());
		contractResponseDto.setTerminateTime(fundContractDo.getTerminateTime());
		contractResponseDto.setState(fundContractDo.getState());
		contractResponseDto.setSignatureImagePath(fundContractDo.getSignatureImagePath());
		contractResponseDto.setConsignorName(fundContractDo.getConsignorCustomerName());
		contractResponseDto.setConsignorCode(fundContractDo.getConsignorCustomerCode());
		
		contractResponseDto.setReadyExpire(false);
		long expireDay = 0L;
		String readyExpireDay = dataDictionaryRpcResovler.findByDataDictionaryValue(Constant.CONTRACT_EXPIRE_DAYS);
		if (StringUtils.isBlank(readyExpireDay) || !StringUtils.isNumeric(readyExpireDay)) {
			expireDay = Constant.READY_EXPIRE_DAY;
		}
		expireDay = Long.valueOf(readyExpireDay);
		LocalDate plusDaysResult = LocalDate.now().plusDays(expireDay);
		if (fundContractDo.getState().equals(ContractState.ENTUST.getCode())) {
			if (Timestamp.valueOf(fundContractDo.getEndTime()).getTime() < Timestamp
					.valueOf(plusDaysResult.atStartOfDay()).getTime()) {
				contractResponseDto.setReadyExpire(true);
			}

		}
		List<FundConsignorDo> consignors = fundConsignorDao.findConsignorsByContractNo(fundContractDo.getContractNo());
		// 列表被委托人信息
		List<FundConsignorDto> consignorDtos = new ArrayList<FundConsignorDto>();
		StringBuilder mobiles = new StringBuilder();
		StringBuilder names = new StringBuilder();
		for (FundConsignorDo fundConsignorDo : consignors) {
			mobiles.append(fundConsignorDo.getConsigneeIdMobile() + "、");
			names.append(fundConsignorDo.getConsigneeName() + "、");
			consignorDtos.add(this.buildConsignorForPage(fundConsignorDo, contractResponseDto.getReadyExpire()));
		}
		contractResponseDto.setConsigneeNames(names.substring(0, names.lastIndexOf("、")));
		contractResponseDto.setConsigneeMobiles(mobiles.substring(0, mobiles.lastIndexOf("、")));
		contractResponseDto.setConsignorDtos(consignorDtos);
		// 构建卡数据
		contractResponseDto.setConsignorCard(fundContractDo.getConsignorCardNo());
		// 获取客户信息
		contractResponseDto.setConsignorMobile(customer.getContactsPhone());
		contractResponseDto.setConsignorIdCode(customer.getCertificateNumber());
		return contractResponseDto;
	}

	/**
	 * 构建被委托人信息列表
	 */
	private List<FundConsignorDo> buildConsignorEntities(FundContractRequestDto fundContractRequest) {
		List<FundConsignorDo> consignorDos = new ArrayList<FundConsignorDo>();
		for (FundConsignorDto consignorRequestDto : fundContractRequest.getConsignors()) {
			consignorDos.add(this.buildConsignorForDataBase(consignorRequestDto, fundContractRequest.getContractNo()));
		}
		return consignorDos;
	}

	/**
	 * 构建被委托人信息for data base
	 */
	private FundConsignorDo buildConsignorForDataBase(FundConsignorDto consignorRequestDto, String contractNo) {
		FundConsignorDo fundConsignorDo = new FundConsignorDo();
		// 构建合同被委托人核心数据
		fundConsignorDo.setConsigneeIdCode(consignorRequestDto.getConsigneeIdCode().trim());
		fundConsignorDo.setConsigneeIdMobile(consignorRequestDto.getConsigneeIdMobile().trim());
		fundConsignorDo.setConsigneeName(consignorRequestDto.getConsigneeName().trim());
		fundConsignorDo.setSignatureImagePath(consignorRequestDto.getSignatureImagePath().trim());
		// 构建商户信息
		UserTicket userTicket = SessionContext.getSessionContext().getUserTicket();
		fundConsignorDo.setFirmId(userTicket.getFirmId());
		fundConsignorDo.setFirmName(userTicket.getFirmName());
		fundConsignorDo.setContractNo(contractNo);
		return fundConsignorDo;
	}

	/**
	 * 构建被委托人信息for page
	 */
	private FundConsignorDto buildConsignorForPage(FundConsignorDo fundConsignorDo, boolean readyExpire) {
		FundConsignorDto fundConsignorDto = new FundConsignorDto();
		fundConsignorDto.setConsigneeName(fundConsignorDo.getConsigneeName());
		fundConsignorDto.setConsigneeIdMobile(fundConsignorDo.getConsigneeIdMobile());
		fundConsignorDto.setConsigneeIdCode(fundConsignorDo.getConsigneeIdCode());
		fundConsignorDto.setContractNo(fundConsignorDo.getContractNo());
		fundConsignorDto.setId(fundConsignorDo.getId());
		fundConsignorDto.setReadyExpire(readyExpire);
		fundConsignorDto.setSignatureImagePath(fundConsignorDo.getSignatureImagePath());
		return fundConsignorDto;
	}

	/**
	 * 构建合同主体详情
	 */
	private FundContractDo buildContractEntity(FundContractRequestDto fundContractRequest) {
		
		FundContractDo fundContractDo = new FundContractDo();
		UserTicket userTicket = SessionContext.getSessionContext().getUserTicket();

		// 获取客户信息
		Customer customer = customerRpcResolver.getWithNotNull(fundContractRequest.getCustomerId(),
				(userTicket.getFirmId()));
		if (customer == null) {
			throw new CardAppBizException(ResultCode.DATA_ERROR, "系统没有客户信息");
		}
		fundContractDo.setConsignorCustomerCode(customer.getCode());
		fundContractDo.setConsignorCustomerId(customer.getId());
		
		//保存签名图片地址
		fundContractDo.setSignatureImagePath(fundContractRequest.getSignatureImagePath());
		
		//校验持卡人的卡状态
		validateAccountId(fundContractRequest, customer);
		
		
		// 构建合同委托人核心数据
		fundContractDo.setConsignorAccountId(fundContractRequest.getConsignorAccountId());
		if (Timestamp.valueOf(fundContractRequest.getEndTime()).getTime() < Timestamp
				.valueOf(LocalDate.now().atStartOfDay()).getTime()) {
			throw new CardAppBizException(ResultCode.DATA_ERROR, "合同结束时间不小于今天");
		}
		if (Timestamp.valueOf(fundContractRequest.getEndTime()).getTime() < Timestamp
				.valueOf(fundContractRequest.getStartTime()).getTime()) {
			throw new CardAppBizException(ResultCode.DATA_ERROR, "合同结束时间不小于开始时间");
		}
		fundContractDo.setStartTime(fundContractRequest.getStartTime());
		fundContractDo.setEndTime(fundContractRequest.getEndTime());
		fundContractDo.setNotes(fundContractRequest.getNotes());
		fundContractDo.setConsignorCardNo(fundContractRequest.getCardNo());
		fundContractDo.setConsignorCustomerName(fundContractRequest.getConsignorCustomerName());
		
		// 构建商户信息
		fundContractDo.setCreatorId(userTicket.getId());
		fundContractDo.setCreator(userTicket.getRealName());
		fundContractDo.setFirmId(userTicket.getFirmId());
		fundContractDo.setFirmName(userTicket.getFirmName());
		
		fundContractDo.setState(ContractState.UNSTARTED.getCode());
		if (Timestamp.valueOf(fundContractRequest.getStartTime()).getTime() <= Timestamp.valueOf(LocalDateTime.now())
				.getTime()) {
			fundContractDo.setState(ContractState.ENTUST.getCode());
		}
		
		// 获取业务编号
		String contractNo = uidRpcResovler.bizNumber(BizNoType.CONTRACT_NO.getCode());
		fundContractDo.setContractNo(contractNo);
		fundContractRequest.setContractNo(contractNo);
		
		return fundContractDo;
	}

	/**
	 * 校验持卡人的卡状态
	 */
	private void validateAccountId(FundContractRequestDto fundContractRequest, Customer customer) {
		UserAccountSingleQueryDto query = new UserAccountSingleQueryDto();
		query.setAccountId(fundContractRequest.getConsignorAccountId());
		UserAccountCardResponseDto userAccountCardResponseDto = accountQueryRpcResolver
				.findSingleWithoutValidate(query);
		if (userAccountCardResponseDto == null) {
			throw new CardAppBizException(ResultCode.DATA_ERROR, "卡号不存在");
		}
		if (CardStatus.RETURNED.getCode() == userAccountCardResponseDto.getCardState()) {
			throw new CardAppBizException(ResultCode.DATA_ERROR, "该卡为退还状态，不能进行此操作");
		}
		if (DisableState.DISABLED.getCode().equals(userAccountCardResponseDto.getDisabledState())) {
			throw new CardAppBizException(ResultCode.DATA_ERROR, "该卡账户为禁用状态，不能进行此操作");
		}
		if (CardStatus.LOSS.getCode() == userAccountCardResponseDto.getCardState()) {
			throw new CardAppBizException(ResultCode.DATA_ERROR, "该卡为挂失状态，不能进行此操作");
		}
		if (!customer.getId().equals(userAccountCardResponseDto.getCustomerId())) {
			throw new CardAppBizException(ResultCode.DATA_ERROR, "卡主和持卡人不一致");
		}
		if (userAccountCardResponseDto.getCardType() != 10) {
			throw new CardAppBizException(ResultCode.DATA_ERROR, "卡必须是主卡");
		}
		
	}
	
	/**
	 * 包装图片
	 */
	private void buildImage(FundContractResponseDto fundContract) {
		fundContract.setSignatureImagePath(Base64.encode(fileUpDownloadService.download(fundContract.getSignatureImagePath())));
		for (FundConsignorDto fundConsignorDto : fundContract.getConsignorDtos()) {
			fundConsignorDto.setSignatureImagePath(Base64.encode(fileUpDownloadService.download(fundConsignorDto.getSignatureImagePath())));
		}
	}
}
