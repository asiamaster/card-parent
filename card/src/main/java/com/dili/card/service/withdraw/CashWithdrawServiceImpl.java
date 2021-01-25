package com.dili.card.service.withdraw;

import cn.hutool.core.util.StrUtil;
import com.dili.card.dto.FundRequestDto;
import com.dili.card.dto.SerialDto;
import com.dili.card.dto.UserAccountCardResponseDto;
import com.dili.card.dto.pay.TradeResponseDto;
import com.dili.card.entity.BusinessRecordDo;
import com.dili.card.type.*;
import org.springframework.stereotype.Service;

/**
 * 现金提现
 */
@Service
public class CashWithdrawServiceImpl extends WithdrawServiceImpl {

    @Override
    public BusinessRecordDo createBusinessRecord(FundRequestDto fundRequestDto, UserAccountCardResponseDto accountCard) {
        return serialService.createBusinessRecord(fundRequestDto, accountCard, temp -> {
            temp.setType(OperateType.ACCOUNT_WITHDRAW.getCode());
            temp.setAmount(fundRequestDto.getAmount());
            temp.setTradeType(TradeType.WITHDRAW.getCode());
            temp.setTradeChannel(fundRequestDto.getTradeChannel());
            temp.setContractNo(fundRequestDto.getContractNo());
            temp.setConsignorId(fundRequestDto.getConsignorId());
            temp.setNotes(StrUtil.isBlank(fundRequestDto.getContractNo()) ?  "现金取款": String.format("委托现金取款，被委托人%s", fundRequestDto.getConsignorName()));
        });
    }

    @Override
    protected Integer getChannelId(FundRequestDto fundRequestDto) {
        return fundRequestDto.getTradeChannel();
    }

    @Override
    public void decreaseCashBox(Long cycleNo, Long amount) {
        accountCycleService.decreaseeCashBox(cycleNo, amount);
    }

    @Override
    public SerialDto createAccountSerial(FundRequestDto fundRequestDto, BusinessRecordDo businessRecord, TradeResponseDto withdrawResponse) {
        return serialService.createAccountSerialWithFund(businessRecord, withdrawResponse, (serialRecord, feeType) -> {
            if (Integer.valueOf(FeeType.ACCOUNT.getCode()).equals(feeType)) {
                serialRecord.setFundItem(FundItem.CASH_WITHDRAW.getCode());
                serialRecord.setFundItemName(FundItem.CASH_WITHDRAW.getName());
            }
        });
    }

    @Override
    public Integer support() {
        return TradeChannel.CASH.getCode();
    }
}
