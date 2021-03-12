package com.dili.card.dto;

import com.dili.card.entity.SerialRecordDo;
import com.dili.ss.domain.BaseDomain;

import java.util.List;

/**
 * 流水相关dto
 * @author xuliang
 */
public class SerialDto extends BaseDomain {

    /** */
	private static final long serialVersionUID = 122934787180274582L;
	/** 流水号*/
    private String serialNo;
    /** 可用期初余额*/
    private Long startBalance;
    /** 可用期末余额*/
    private Long endBalance;
    /** 总余额(包含冻结) */
    private Long totalBalance;
    /** 流水列表 */
    private List<SerialRecordDo> serialRecordList;
    /**额外记录*/
    private String attach;

    public String getAttach() {
        return attach;
    }

    public void setAttach(String attach) {
        this.attach = attach;
    }

    public String getSerialNo() {
        return serialNo;
    }

    public void setSerialNo(String serialNo) {
        this.serialNo = serialNo;
    }

    public Long getStartBalance() {
        return startBalance;
    }

    public void setStartBalance(Long startBalance) {
        this.startBalance = startBalance;
    }

    public Long getEndBalance() {
        return endBalance;
    }

    public void setEndBalance(Long endBalance) {
        this.endBalance = endBalance;
    }

    public Long getTotalBalance() {
        return totalBalance;
    }

    public void setTotalBalance(Long totalBalance) {
        this.totalBalance = totalBalance;
    }

    public List<SerialRecordDo> getSerialRecordList() {
        return serialRecordList;
    }

    public void setSerialRecordList(List<SerialRecordDo> serialRecordList) {
        this.serialRecordList = serialRecordList;
    }
}
