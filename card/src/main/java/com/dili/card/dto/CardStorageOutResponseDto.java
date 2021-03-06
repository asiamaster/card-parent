package com.dili.card.dto;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.StringUtils;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 卡申领记录响应Dto
 * @Auther: miaoguoxin
 * @Date: 2020/7/1 16:50
 */
public class CardStorageOutResponseDto implements Serializable {
    /** */
	private static final long serialVersionUID = 1L;
	/**主键id*/
    private Long id;
    /**卡号列表*/
    private List<String> applyCards;
    /**领取数量*/
    private Integer amount;
    /**领取人id*/
    private Long applyUserId;
    /**领取人工号*/
    private String applyUserCode;
    /**领取人名字*/
    private String applyUserName;
    /**操作员id*/
    private Long creatorId;
    /**操作员名字*/
    private String creator;
    /**备注*/
    private String notes;
    /**申请时间*/
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime applyTime;

    /**
    *  转换数据库字符串cardNos
    * @author miaoguoxin
    * @date 2020/7/2
    */
    public void setConvertCardNo(String cardNos){
        if (StringUtils.isBlank(cardNos)){
            this.applyCards = new ArrayList<>();
        }else {
            this.applyCards = new ArrayList<>();
            applyCards.addAll(Arrays.asList(cardNos.split(",")));
        }
    }

    public String getApplyUserCode() {
        return applyUserCode;
    }

    public void setApplyUserCode(String applyUserCode) {
        this.applyUserCode = applyUserCode;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<String> getApplyCards() {
        return applyCards;
    }

    public void setApplyCards(List<String> applyCards) {
        this.applyCards = applyCards;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public Long getApplyUserId() {
        return applyUserId;
    }

    public void setApplyUserId(Long applyUserId) {
        this.applyUserId = applyUserId;
    }

    public String getApplyUserName() {
        return applyUserName;
    }

    public void setApplyUserName(String applyUserName) {
        this.applyUserName = applyUserName;
    }

    public Long getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(Long creatorId) {
        this.creatorId = creatorId;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public LocalDateTime getApplyTime() {
        return applyTime;
    }

    public void setApplyTime(LocalDateTime applyTime) {
        this.applyTime = applyTime;
    }
}
