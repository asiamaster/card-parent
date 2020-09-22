package com.dili.card.dto;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author apache 资金合同打印dto
 */
public class FundContractPrintDto implements Serializable{
	/** */
	private static final long serialVersionUID = 1L;
	/** 卡号 */
	private String cardNo;
	/** 受托人姓名 */
	private String consigneeName;
	/** 合同开始日期 */
	private String startTime;
	/** 合同结束日期 */
	private String endTime;
	/** 当前日期 */
	private String currentTime;
	/** 委托人身份证 */
	private String consignorIdCode;
	/** 委托人手机号 */
	private String consignorMobile;
	/** 被委托人姓名 */
	private String consigneeNames;
	/** 委托人签名图片地址 */
	private String signatureImagePath;
	/** 被委托人列表 */
	private List<FundConsignorDto> consignors;

	public String getCardNo() {
		return cardNo;
	}

	public void setCardNo(String cardNo) {
		this.cardNo = cardNo;
	}

	public String getConsigneeName() {
		return consigneeName;
	}

	public void setConsigneeName(String consigneeName) {
		this.consigneeName = consigneeName;
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public String getCurrentTime() {
		return currentTime;
	}

	public void setCurrentTime(String currentTime) {
		this.currentTime = currentTime;
	}

	public String getConsignorIdCode() {
		return consignorIdCode;
	}

	public void setConsignorIdCode(String consignorIdCode) {
		this.consignorIdCode = consignorIdCode;
	}

	public String getConsigneeNames() {
		return consigneeNames;
	}

	public void setConsigneeNames(String consigneeNames) {
		this.consigneeNames = consigneeNames;
	}

	public List<FundConsignorDto> getConsignors() {
		return consignors;
	}

	public void setConsignors(List<FundConsignorDto> consignors) {
		this.consignors = consignors;
	}
	

	public String getConsignorMobile() {
		return consignorMobile;
	}

	public void setConsignorMobile(String consignorMobile) {
		this.consignorMobile = consignorMobile;
	}
	
	public static FundContractPrintDto wrapperPrintDetai(FundContractResponseDto fundContractResponseDto) {
		FundContractPrintDto fundContractPrintDto = new FundContractPrintDto();
		fundContractPrintDto.setCardNo(fundContractResponseDto.getConsignorCard());
		fundContractPrintDto.setConsigneeName(fundContractResponseDto.getConsignorName());
		fundContractPrintDto.setConsigneeNames(fundContractResponseDto.getConsigneeNames());
		fundContractPrintDto.setConsignorIdCode(fundContractResponseDto.getConsignorIdCode());
		fundContractPrintDto.setSignatureImagePath(fundContractResponseDto.getSignatureImagePath());
		LocalDateTime startTime = fundContractResponseDto.getStartTime();
		fundContractPrintDto.setStartTime( startTime.getYear() + " 年 " + startTime.getMonthValue() + " 月 " + startTime.getDayOfMonth() + " 日 ");
		LocalDateTime endTime = fundContractResponseDto.getEndTime();
		fundContractPrintDto.setEndTime(endTime.getYear() + " 年 " + endTime.getMonthValue() + " 月 " + endTime.getDayOfMonth() + " 日 ");
		LocalDateTime currentTime = fundContractResponseDto.getCreateTime();
		fundContractPrintDto.setCurrentTime(currentTime.getYear() + " 年 " + currentTime.getMonthValue() + " 月 " + currentTime.getDayOfMonth() + " 日 ");
		fundContractPrintDto.setConsignors(fundContractResponseDto.getConsignorDtos());
		fundContractPrintDto.setConsignorMobile(fundContractResponseDto.getConsignorMobile());
		return fundContractPrintDto;
	}

	public String getSignatureImagePath() {
		return signatureImagePath;
	}

	public void setSignatureImagePath(String signatureImagePath) {
		this.signatureImagePath = signatureImagePath;
	}

}
