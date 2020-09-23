package com.dili.card.service.withdraw;

import com.dili.card.dto.FundRequestDto;
import com.dili.card.dto.SerialDto;
import com.dili.card.dto.UserAccountCardResponseDto;
import com.dili.card.dto.pay.BalanceRequestDto;
import com.dili.card.dto.pay.BalanceResponseDto;
import com.dili.card.dto.pay.FeeItemDto;
import com.dili.card.dto.pay.TradeResponseDto;
import com.dili.card.entity.BusinessRecordDo;
import com.dili.card.exception.CardAppBizException;
import com.dili.card.type.*;
import com.dili.card.util.CurrencyUtils;
import com.dili.ss.constant.ResultCode;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 网银提现
 */
@Service
public class EBankWithdrawServiceImpl extends WithdrawServiceImpl {


    @Override
    public void validateSpecial(FundRequestDto fundRequestDto) {
        fundRequestDto.setServiceCost(fundRequestDto.getServiceCost() == null ? 0L : fundRequestDto.getServiceCost());
        if (fundRequestDto.getServiceCost() < 0L) {
            throw new CardAppBizException("", "请正确输入手续费");
        }
    }

    @Override
    public void checkAvailableAmount(FundRequestDto fundRequestDto, UserAccountCardResponseDto accountCard) {
        BalanceResponseDto balance = payService.queryBalance(new BalanceRequestDto(accountCard.getFundAccountId()));
        long totalAmount = fundRequestDto.getAmount() + fundRequestDto.getServiceCost();
        if (totalAmount > balance.getAvailableAmount()) {
            throw new CardAppBizException(ResultCode.DATA_ERROR, "可用余额不足");
        }
    }

    @Override
    public BusinessRecordDo createBusinessRecord(FundRequestDto fundRequestDto, UserAccountCardResponseDto accountCard) {
        return serialService.createBusinessRecord(fundRequestDto, accountCard, temp -> {
            temp.setType(OperateType.ACCOUNT_WITHDRAW.getCode());
            temp.setAmount(fundRequestDto.getAmount());
            temp.setTradeType(TradeType.WITHDRAW.getCode());
            temp.setTradeChannel(fundRequestDto.getTradeChannel());
            temp.setServiceCost(fundRequestDto.getServiceCost());
            temp.setNotes(String.format("网银取款，手续费%s元", CurrencyUtils.toYuanWithStripTrailingZeros(fundRequestDto.getServiceCost())));
        });
    }

    @Override
    protected List<FeeItemDto> createFees(FundRequestDto fundRequestDto) {
        if (fundRequestDto.getServiceCost() == 0L) {
            return null;
        }
        List<FeeItemDto> fees = new ArrayList<>();
        FeeItemDto feeItem = new FeeItemDto();
        feeItem.setAmount(fundRequestDto.getServiceCost());
        feeItem.setType(FeeType.SERVICE.getCode());
        feeItem.setTypeName(FeeType.SERVICE.getName());
        fees.add(feeItem);
        return fees;
    }

    @Override
    protected SerialDto createAccountSerial(FundRequestDto fundRequestDto, BusinessRecordDo businessRecord, TradeResponseDto withdrawResponse) {
        if (fundRequestDto.getServiceCost() == 0L) {//特殊处理为0时记录
            FeeItemDto feeItem = new FeeItemDto();
            feeItem.setType(FeeType.SERVICE.getCode());
            withdrawResponse.getStreams().add(feeItem);
        }
        return serialService.createAccountSerialWithFund(businessRecord, withdrawResponse, (serialRecord, feeType) -> {
            if (Integer.valueOf(FeeType.ACCOUNT.getCode()).equals(feeType)) {
                serialRecord.setFundItem(FundItem.EBANK_WITHDRAW.getCode());
                serialRecord.setFundItemName(FundItem.EBANK_WITHDRAW.getName());
            }
            if (Integer.valueOf(FeeType.SERVICE.getCode()).equals(feeType)) {
                serialRecord.setFundItem(FundItem.EBANK_SERVICE.getCode());
                serialRecord.setFundItemName(FundItem.EBANK_SERVICE.getName());
            }
        });
    }

    @Override
    public Integer support() {
        return TradeChannel.E_BANK.getCode();
    }
}
