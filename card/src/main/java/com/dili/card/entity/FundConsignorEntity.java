package com.dili.card.entity;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 受委托人
 * @author bob
 */
public class FundConsignorEntity implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	/**  */
	private Long id; 
	/** 合同编号 */
	private String contractNo; 
	/** 受托人姓名 */
	private String consigneeName; 
	/** 受委托人签名图片地址 */
	private String signatureImagePath; 
	/** 受托人证件号 */
	private String consigneeIdCode; 
	/** 受托人手机号 */
	private String consigneeIdMobile; 
	/** 创建时间 */
	private LocalDateTime createTime; 
	/** 商户编码 */
	private Long firmId; 
	/** 商户名称 */
	private String firmName; 
    /**
     * FundConsignorEntity constructor
     */
	public FundConsignorEntity() {
		super();
	}

    /**
     * setter for 
     */
	public void setId(Long id) {
		this.id = id;
	}

    /**
     * getter for 
     */
	public Long getId() {
		return id;
	}

    /**
     * setter for 合同编号
     */
	public void setContractNo(String contractNo) {
		this.contractNo = contractNo;
	}

    /**
     * getter for 合同编号
     */
	public String getContractNo() {
		return contractNo;
	}

    /**
     * setter for 受托人姓名
     */
	public void setConsigneeName(String consigneeName) {
		this.consigneeName = consigneeName;
	}

    /**
     * getter for 受托人姓名
     */
	public String getConsigneeName() {
		return consigneeName;
	}

    /**
     * setter for 受委托人签名图片地址
     */
	public void setSignatureImagePath(String signatureImagePath) {
		this.signatureImagePath = signatureImagePath;
	}

    /**
     * getter for 受委托人签名图片地址
     */
	public String getSignatureImagePath() {
		return signatureImagePath;
	}

    /**
     * setter for 受托人证件号
     */
	public void setConsigneeIdCode(String consigneeIdCode) {
		this.consigneeIdCode = consigneeIdCode;
	}

    /**
     * getter for 受托人证件号
     */
	public String getConsigneeIdCode() {
		return consigneeIdCode;
	}

    /**
     * setter for 受托人手机号
     */
	public void setConsigneeIdMobile(String consigneeIdMobile) {
		this.consigneeIdMobile = consigneeIdMobile;
	}

    /**
     * getter for 受托人手机号
     */
	public String getConsigneeIdMobile() {
		return consigneeIdMobile;
	}

    /**
     * setter for 创建时间
     */
	public void setCreateTime(LocalDateTime createTime) {
		this.createTime = createTime;
	}

    /**
     * getter for 创建时间
     */
	public LocalDateTime getCreateTime() {
		return createTime;
	}

    /**
     * setter for 商户编码
     */
	public void setFirmId(Long firmId) {
		this.firmId = firmId;
	}

    /**
     * getter for 商户编码
     */
	public Long getFirmId() {
		return firmId;
	}

    /**
     * setter for 商户名称
     */
	public void setFirmName(String firmName) {
		this.firmName = firmName;
	}

    /**
     * getter for 商户名称
     */
	public String getFirmName() {
		return firmName;
	}

    /**
     * FundConsignorEntity.toString()
     */
    @Override
    public String toString() {
        return "FundConsignorEntity{" +
               "id='" + id + '\'' +
               ", contractNo='" + contractNo + '\'' +
               ", consigneeName='" + consigneeName + '\'' +
               ", signatureImagePath='" + signatureImagePath + '\'' +
               ", consigneeIdCode='" + consigneeIdCode + '\'' +
               ", consigneeIdMobile='" + consigneeIdMobile + '\'' +
               ", createTime='" + createTime + '\'' +
               ", firmId='" + firmId + '\'' +
               ", firmName='" + firmName + '\'' +
               '}';
    }

}
