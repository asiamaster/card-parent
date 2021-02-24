package com.dili.card.service.withdraw;

import cn.hutool.core.util.StrUtil;
import com.dili.card.dao.IAccountCycleDao;
import com.dili.card.dto.AccountCycleDto;
import com.dili.card.dto.FundRequestDto;
import com.dili.card.dto.SerialDto;
import com.dili.card.dto.UserAccountCardResponseDto;
import com.dili.card.dto.pay.TradeResponseDto;
import com.dili.card.entity.AccountCycleDo;
import com.dili.card.entity.BusinessRecordDo;
import com.dili.card.exception.CardAppBizException;
import com.dili.card.service.IMiscService;
import com.dili.card.type.DictValue;
import com.dili.card.type.FeeType;
import com.dili.card.type.FundItem;
import com.dili.card.type.OperateType;
import com.dili.card.type.TradeChannel;
import com.dili.card.type.TradeType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 现金提现
 */
@Service
public class CashWithdrawServiceImpl extends WithdrawServiceImpl {
    @Autowired
    private IAccountCycleDao accountCycleDao;
    @Autowired
    private IMiscService miscService;

    @Override
    public BusinessRecordDo createBusinessRecord(FundRequestDto fundRequestDto, UserAccountCardResponseDto accountCard) {
        return serialService.createBusinessRecord(fundRequestDto, accountCard, temp -> {
            temp.setType(OperateType.ACCOUNT_WITHDRAW.getCode());
            temp.setAmount(fundRequestDto.getAmount());
            temp.setTradeType(TradeType.WITHDRAW.getCode());
            temp.setTradeChannel(fundRequestDto.getTradeChannel());
            temp.setContractNo(fundRequestDto.getContractNo());
            temp.setConsignorId(fundRequestDto.getConsignorId());
            temp.setNotes(StrUtil.isBlank(fundRequestDto.getContractNo()) ? "现金取款" : String.format("委托现金取款，被委托人%s", fundRequestDto.getConsignorName()));
        });
    }

    @Override
    protected Integer getChannelId(FundRequestDto fundRequestDto) {
        return fundRequestDto.getTradeChannel();
    }

    @Override
    public void decreaseCashBox(Long cycleNo, Long amount, Long firmId) {
        String dictVal = miscService.getSingleDictVal(DictValue.WITHDRAW_CASH_BOX_ALLOW_CHECK.getCode(), firmId, "0");
        if ("1".equals(dictVal)){
            AccountCycleDo accountCycle = accountCycleDao.findByCycleNo(cycleNo);
            AccountCycleDto accountCycleDto = accountCycleService.buildAccountCycleWrapperDetail(accountCycle, true);
            if (accountCycleDto.getAccountCycleDetailDto().getUnDeliverAmount() < amount) {
                throw new CardAppBizException("取款金额不能超过未交现金余额");
            }
        }
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
