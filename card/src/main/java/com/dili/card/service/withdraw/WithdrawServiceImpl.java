package com.dili.card.service.withdraw;

import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.core.util.StrUtil;
import com.dili.card.common.constant.Constant;
import com.dili.card.dto.FundRequestDto;
import com.dili.card.dto.SerialDto;
import com.dili.card.dto.UserAccountCardResponseDto;
import com.dili.card.dto.pay.BalanceRequestDto;
import com.dili.card.dto.pay.BalanceResponseDto;
import com.dili.card.dto.pay.CreateTradeRequestDto;
import com.dili.card.dto.pay.FeeItemDto;
import com.dili.card.dto.pay.TradeRequestDto;
import com.dili.card.dto.pay.TradeResponseDto;
import com.dili.card.entity.BusinessRecordDo;
import com.dili.card.entity.bo.MessageBo;
import com.dili.card.exception.CardAppBizException;
import com.dili.card.service.IAccountCycleService;
import com.dili.card.service.IAccountQueryService;
import com.dili.card.service.IPayService;
import com.dili.card.service.ISerialService;
import com.dili.card.type.CardStatus;
import com.dili.card.type.CardType;
import com.dili.card.type.PaySubject;
import com.dili.card.type.TradeChannel;
import com.dili.card.type.TradeType;
import com.dili.card.type.UsePermissionType;
import com.dili.ss.constant.ResultCode;
import io.seata.spring.annotation.GlobalTransactional;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * 提现操作基础实现类
 */
public abstract class WithdrawServiceImpl implements IWithdrawService {

    @Resource
    protected IPayService payService;

    @Resource
    protected IAccountQueryService accountQueryService;

    @Resource
    protected ISerialService serialService;

    @Resource
    protected IAccountCycleService accountCycleService;

   // @GlobalTransactional(rollbackFor = Exception.class)
    @Transactional(rollbackFor = Exception.class)
    @Override
    public MessageBo<String> withdraw(FundRequestDto fundRequestDto) {
        //参数验证
        this.validate(fundRequestDto);
        //验证卡状态、余额等
        UserAccountCardResponseDto accountCard = this.check(fundRequestDto);
        BusinessRecordDo businessRecord = createBusinessRecord(fundRequestDto, accountCard);
        //构建创建交易参数
        CreateTradeRequestDto createTradeRequest = CreateTradeRequestDto.createTrade(TradeType.WITHDRAW.getCode(), accountCard.getAccountId(), accountCard.getFundAccountId(), fundRequestDto.getAmount(), businessRecord.getSerialNo(), String.valueOf(businessRecord.getCycleNo()));
        createTradeRequest.setDescription(PaySubject.WITHDRAW.getName());
        //银行圈提有点特殊
        if (TradeChannel.BANK.getCode() == fundRequestDto.getTradeChannel()) {
            createTradeRequest.setType(TradeType.BANK_WITHDRAW.getCode());
        }
        //创建交易
        String tradeNo = payService.createTrade(createTradeRequest);
        businessRecord.setTradeNo(tradeNo);
        //先异步保存一条记录，防止被事务回滚
        ThreadUtil.execute(() -> {
            //保存业务办理记录
            serialService.saveBusinessRecord(businessRecord);
        });
        //扣减现金池
        this.decreaseCashBox(businessRecord.getCycleNo(), fundRequestDto.getAmount());
        //提现提交
        TradeRequestDto withdrawRequest = TradeRequestDto.createTrade(accountCard, tradeNo, this.getChannelId(fundRequestDto), fundRequestDto.getTradePwd());
        withdrawRequest.setFees(this.createFees(fundRequestDto));
        withdrawRequest.setChannelAccount(fundRequestDto.getChannelAccount());
        TradeResponseDto withdrawResponse = payService.commitWithdraw(withdrawRequest);
        return this.handleSerialAfterCommitWithdraw(fundRequestDto, businessRecord, withdrawResponse);
    }

    /**
     * 提交支付以后操作流水
     * @author miaoguoxin
     * @date 2021/1/14
     */
    protected MessageBo<String> handleSerialAfterCommitWithdraw(FundRequestDto fundRequestDto, BusinessRecordDo businessRecord, TradeResponseDto withdrawResponse) {
        //取款成功后修改业务单状态、存储流水
        SerialDto serialDto = this.createAccountSerial(fundRequestDto, businessRecord, withdrawResponse);
        serialService.handleSuccess(serialDto);
        return MessageBo.success(businessRecord.getSerialNo());
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
     * 交易渠道（和支付系统有偏差）
     * @author miaoguoxin
     * @date 2021/1/14
     */
    protected abstract Integer getChannelId(FundRequestDto fundRequestDto);

    /**
     * 扣减现金池
     * @param cycleNo
     * @param amount
     */
    protected void decreaseCashBox(Long cycleNo, Long amount) {

    }

    /**
     * 参数验证
     * @param fundRequestDto
     */
    protected void validate(FundRequestDto fundRequestDto) {
        if (fundRequestDto.getAmount() == null || fundRequestDto.getAmount() < Constant.MIN_AMOUNT || fundRequestDto.getAmount() > Constant.MAX_AMOUNT) {
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

    }

    /**
     * 验证是否可提款 卡状态、余额
     * @param fundRequestDto
     * @return
     */
    protected UserAccountCardResponseDto check(FundRequestDto fundRequestDto) {
        UserAccountCardResponseDto accountCard = accountQueryService.getByAccountId(fundRequestDto);
        if (!Integer.valueOf(CardType.MASTER.getCode()).equals(accountCard.getCardType())) {
            throw new CardAppBizException("", "该卡非主卡,不能进行提现");
        }
        if (!accountCard.getPermissionList().contains(String.valueOf(UsePermissionType.WITHDRAW.getCode()))) {
            throw new CardAppBizException("", "该卡无提现权限,不能进行提现");
        }
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
