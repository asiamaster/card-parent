package com.dili.card.dto.pay;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 提现返回值
 * @author xuliang
 */
public class WithdrawResponseDto {

    /** 账户ID*/
    private Long accountId;
    /** 余额*/
    private Long balance;
    /** 发生金额*/
    private Long amount;
    /** 操作时间*/
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime when;
    /** 流水明细*/
    private List<FeeItemDto> streams;

    public Long getAccountId() {
        return accountId;
    }

    public void setAccountId(Long accountId) {
        this.accountId = accountId;
    }

    public Long getBalance() {
        return balance;
    }

    public void setBalance(Long balance) {
        this.balance = balance;
    }

    public Long getAmount() {
        return amount;
    }

    public void setAmount(Long amount) {
        this.amount = amount;
    }

    public LocalDateTime getWhen() {
        return when;
    }

    public void setWhen(LocalDateTime when) {
        this.when = when;
    }

    public List<FeeItemDto> getStreams() {
        return streams;
    }

    public void setStreams(List<FeeItemDto> streams) {
        this.streams = streams;
    }
}
