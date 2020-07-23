package com.dili.card.service.withdraw;

import cn.hutool.core.util.StrUtil;
import com.dili.card.dto.FundRequestDto;
import com.dili.card.dto.SerialDto;
import com.dili.card.dto.UserAccountCardResponseDto;
import com.dili.card.dto.pay.*;
import com.dili.card.entity.BusinessRecordDo;
import com.dili.card.exception.CardAppBizException;
import com.dili.card.service.IAccountCycleService;
import com.dili.card.service.IAccountQueryService;
import com.dili.card.service.IPayService;
import com.dili.card.service.ISerialService;
import com.dili.card.type.CardStatus;
import com.dili.card.type.TradeType;
import com.dili.ss.constant.ResultCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * 提现操作基础实现类
 */
public abstract class WithdrawServiceImpl implements IWithdrawService {
    private static final Logger LOGGER = LoggerFactory.getLogger(WithdrawServiceImpl.class);

    @Resource
    protected IPayService payService;

    @Resource
    protected IAccountQueryService accountQueryService;

    @Resource
    protected ISerialService serialService;

    @Resource
    protected IAccountCycleService accountCycleService;

    @Transactional
    @Override
    public void withdraw(FundRequestDto fundRequestDto) {
        validate(fundRequestDto);//参数验证
        UserAccountCardResponseDto accountCard = check(fundRequestDto);//验证卡状态、余额等
        BusinessRecordDo businessRecord = createBusinessRecord(fundRequestDto, accountCard);
        //构建创建交易参数
        CreateTradeRequestDto createTradeRequest = new CreateTradeRequestDto();
        createTradeRequest.setType(TradeType.WITHDRAW.getCode());
        createTradeRequest.setAccountId(accountCard.getFundAccountId());
        createTradeRequest.setAmount(fundRequestDto.getAmount());
        createTradeRequest.setSerialNo(businessRecord.getSerialNo());
        createTradeRequest.setCycleNo(String.valueOf(businessRecord.getCycleNo()));
        createTradeRequest.setDescription("");
        createTradeRequest.setBusinessId(accountCard.getAccountId());
        //创建交易
        String tradeNo = payService.createTrade(createTradeRequest);
        businessRecord.setTradeNo(tradeNo);
        //保存业务办理记录
        serialService.saveBusinessRecord(businessRecord);
        //扣减现金池
        decreaseCashBox(businessRecord.getCycleNo(), fundRequestDto.getAmount());
        //提现提交
        TradeRequestDto withdrawRequest = new TradeRequestDto();
        withdrawRequest.setTradeId(tradeNo);
        withdrawRequest.setAccountId(accountCard.getFundAccountId());
        withdrawRequest.setChannelId(fundRequestDto.getTradeChannel());
        withdrawRequest.setPassword(fundRequestDto.getTradePwd());
        withdrawRequest.setBusinessId(accountCard.getAccountId());
        withdrawRequest.setFees(createFees(fundRequestDto));
        TradeResponseDto withdrawResponse = payService.commitWithdraw(withdrawRequest);
        try {//取款成功后修改业务单状态、存储流水
            SerialDto serialDto = createAccountSerial(fundRequestDto, businessRecord, withdrawResponse);
            serialService.handleSuccess(serialDto);
        } catch (Exception e) {
            LOGGER.error("withdraw", e);
        }
    }

    /**
     * 构建账户流水
     * @param businessRecord
     * @param withdrawResponse
     * @return
     */
    protected abstract SerialDto createAccountSerial(FundRequestDto fundRequestDto, BusinessRecordDo businessRecord, TradeResponseDto withdrawResponse);

    /**
     * 构建费用列表
     * @param fundRequestDto
     * @return
     */
    protected List<FeeItemDto> createFees(FundRequestDto fundRequestDto) {
        return null;
    }

    /**
     * 构建业务办理记录数据
     * @param fundRequestDto
     * @param accountCard
     * @return
     */
    protected abstract BusinessRecordDo createBusinessRecord(FundRequestDto fundRequestDto, UserAccountCardResponseDto accountCard);

    /**
     * 扣减现金池
     * @param cycleNo
     * @param amount
     */
    protected void decreaseCashBox(Long cycleNo, Long amount) {
        return;
    }

    /**
     * 参数验证
     * @param fundRequestDto
     */
    protected void validate(FundRequestDto fundRequestDto) {
        if (fundRequestDto.getAmount() == null || fundRequestDto.getAmount() <= 0L) {
            throw new CardAppBizException("", "请正确输入提现金额");
        }
        if (StrUtil.isBlank(fundRequestDto.getTradePwd())) {
            throw new CardAppBizException("", "请输入密码");
        }
        validateSpecial(fundRequestDto);
    }

    /**
     * 参数验证
     * @param fundRequestDto
     */
    protected void validateSpecial(FundRequestDto fundRequestDto) {
        return;
    }

    /**
     * 验证是否可提款 卡状态、余额
     * @param fundRequestDto
     * @return
     */
    protected UserAccountCardResponseDto check(FundRequestDto fundRequestDto) {
        UserAccountCardResponseDto accountCard = accountQueryService.getByAccountId(fundRequestDto);
        if (!Integer.valueOf(CardStatus.NORMAL.getCode()).equals(accountCard.getCardState())) {
            throw new CardAppBizException("", String.format("该卡为%s状态,不能进行提现", CardStatus.getName(accountCard.getCardState())));
        }
        checkAvailableAmount(fundRequestDto, accountCard);
        return accountCard;
    }

    /**
     * 验证余额
     * @param fundRequestDto
     * @param accountCard
     */
    protected void checkAvailableAmount(FundRequestDto fundRequestDto, UserAccountCardResponseDto accountCard) {
        BalanceResponseDto balance = payService.queryBalance(new BalanceRequestDto(accountCard.getFundAccountId()));
        long totalAmount = fundRequestDto.getAmount();
        if (totalAmount > balance.getAvailableAmount()) {
            throw new CardAppBizException(ResultCode.DATA_ERROR, "可用余额不足");
        }
    }
}
