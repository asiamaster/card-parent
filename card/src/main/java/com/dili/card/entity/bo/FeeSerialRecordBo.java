package com.dili.card.entity.bo;

import cn.hutool.core.collection.CollectionUtil;
import com.dili.card.dto.SerialRecordResponseDto;
import com.dili.card.type.FundItemMap;
import org.apache.commons.lang3.math.NumberUtils;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Auther: miaoguoxin
 * @Date: 2021/4/22 17:39
 * @Description: 手续费
 */
public class FeeSerialRecordBo {
    /**手续费列表*/
    private List<SerialRecordResponseDto> feeList;
    /**未过滤的流水记录列表*/
    private List<SerialRecordResponseDto> totalList;
    /**手续费金额（分）*/
    private Long totalFeeAmount;

    public FeeSerialRecordBo(List<SerialRecordResponseDto> totalList) {
        this.totalList = totalList;
        //要去除Null的记录，因为有些类型记录了假的手续费
        this.feeList = totalList.stream()
                .filter(l -> FundItemMap.isFeeFundItem(l.getFundItem()))
                .filter(l -> l.getAmount() != null)
                .collect(Collectors.toList());
        this.totalFeeAmount = this.feeList.stream()
                .mapToLong(serialRecordDo -> NumberUtils.toLong(serialRecordDo.getAmount() + ""))
                .sum();
    }

    /**
     * 扣除手续费计算期末金额
     * @author miaoguoxin
     * @date 2021/4/22
     */
    public Long calculateEndBalanceWhenDeductFee() {
        //最后一条流水就是真实期末
        if (CollectionUtil.isEmpty(this.totalList)) {
            return null;
        }
        List<SerialRecordResponseDto> filterList = this.totalList.stream().filter(l -> l.getAmount() != null).collect(Collectors.toList());
        if (CollectionUtil.isEmpty(filterList)) {
            return null;
        }
        this.totalList.sort(Comparator.comparing(SerialRecordResponseDto::getId).reversed());
        return NumberUtils.toLong(this.totalList.get(0).getEndBalance() + "");
    }

    public List<SerialRecordResponseDto> getFeeList() {
        return feeList;
    }

    public Long getTotalFeeAmount() {
        return totalFeeAmount;
    }

    public List<SerialRecordResponseDto> getTotalList() {
        return totalList;
    }
}
