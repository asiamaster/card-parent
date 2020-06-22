package com.dili.card.dto;

import java.io.Serializable;

/**
 * @Auther: miaoguoxin
 * @Date: 2020/6/22 13:45
 * @Description: 客户信息响应Dto
 */
public class CustomerResponseDto implements Serializable {
    /**客户id主键*/
    private Long id;
    /**客户编码*/
    private String code;
    /**客户名称*/
    private String name;
    /**联系电话*/
    private String contactsPhone;
    /**证件地址*/
    private String certificateAddr;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContactsPhone() {
        return contactsPhone;
    }

    public void setContactsPhone(String contactsPhone) {
        this.contactsPhone = contactsPhone;
    }

    public String getCertificateAddr() {
        return certificateAddr;
    }

    public void setCertificateAddr(String certificateAddr) {
        this.certificateAddr = certificateAddr;
    }
}
