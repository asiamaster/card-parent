package com.dili.card.service.impl;

import com.dili.card.common.constant.ServiceName;
import com.dili.card.dao.IReverseRecordDao;
import com.dili.card.dto.BusinessRecordResponseDto;
import com.dili.card.dto.ReverseDetailResponseDto;
import com.dili.card.dto.ReverseRecordQueryDto;
import com.dili.card.dto.ReverseRecordResponseDto;
import com.dili.card.dto.ReverseRequestDto;
import com.dili.card.dto.SerialDto;
import com.dili.card.dto.SerialQueryDto;
import com.dili.card.dto.SerialRecordResponseDto;
import com.dili.card.dto.UserAccountCardResponseDto;
import com.dili.card.dto.UserAccountSingleQueryDto;
import com.dili.card.dto.pay.PayReverseRequestDto;
import com.dili.card.dto.pay.TradeResponseDto;
import com.dili.card.entity.BusinessRecordDo;
import com.dili.card.entity.ReverseRecord;
import com.dili.card.entity.SerialRecordDo;
import com.dili.card.entity.bo.ReverseAmountBo;
import com.dili.card.exception.CardAppBizException;
import com.dili.card.rpc.PayRpc;
import com.dili.card.rpc.resolver.AccountQueryRpcResolver;
import com.dili.card.rpc.resolver.GenericRpcResolver;
import com.dili.card.rpc.resolver.SerialRecordRpcResolver;
import com.dili.card.service.IAccountCycleService;
import com.dili.card.service.IReverseService;
import com.dili.card.service.ISerialService;
import com.dili.card.type.FeeType;
import com.dili.card.type.FundItem;
import com.dili.card.type.OperateType;
import com.dili.card.type.ReverseFundItemMap;
import com.dili.card.util.PageUtils;
import com.dili.ss.domain.PageOutput;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import io.seata.spring.annotation.GlobalTransactional;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Auther: miaoguoxin
 * @Date: 2020/11/24 14:31
 * @Description:
 */
@Service
public class ReverseService implements IReverseService {
    @Autowired
    private IAccountCycleService accountCycleService;
    @Autowired
    private ISerialService serialService;
    @Autowired
    private IReverseRecordDao reverseRecordDao;
    @Autowired
    private PayRpc payRpc;
    @Autowired
    private AccountQueryRpcResolver accountQueryRpcResolver;
    @Autowired
    private SerialRecordRpcResolver serialRecordRpcResolver;

    @Override
    public PageOutput<List<ReverseRecordResponseDto>> getPage(ReverseRecordQueryDto queryDto) {
        Page<ReverseRecord> page = PageHelper.startPage(queryDto.getPage(), queryDto.getRows());
        List<ReverseRecord> list = reverseRecordDao.findByCondition(queryDto);
        return PageUtils.convert2PageOutput(page, convert2ListDto(list));
    }

    @Override
    public ReverseDetailResponseDto getDetail(String serialNo, Long firmId) {
        //业务记录
        BusinessRecordDo businessRecord = serialService.findBusinessRecordBySerialNo(serialNo);
        if (businessRecord == null) {
            throw new CardAppBizException("操作记录不存在");
        }
        if (!OperateType.canReverseType(businessRecord.getType())) {
            throw new CardAppBizException("只允许充值和提款业务 冲正");
        }
        ReverseRecord reverseRecord = reverseRecordDao.findByBizSerialNo(serialNo, firmId);
        if (reverseRecord != null) {
            throw new CardAppBizException("只允许做一次冲正操作");
        }
        BusinessRecordResponseDto bizSerial = new BusinessRecordResponseDto();
        BeanUtils.copyProperties(businessRecord, bizSerial);

        //查询操作记录
        SerialQueryDto queryDto = new SerialQueryDto();
        queryDto.setSerialNo(serialNo);
        List<SerialRecordDo> serialRecordDos = serialRecordRpcResolver.getList(queryDto);
        //手续费
        List<SerialRecordResponseDto> feeSerials = this.getFeeSerialRecords(serialRecordDos);

        //查询卡账户
        UserAccountSingleQueryDto accountQueryDto = new UserAccountSingleQueryDto();
        accountQueryDto.setAccountId(bizSerial.getAccountId());
        accountQueryDto.setCardNo(bizSerial.getCardNo());
        UserAccountCardResponseDto accountInfo = accountQueryRpcResolver.findSingle(accountQueryDto);

        ReverseDetailResponseDto result = new ReverseDetailResponseDto();
        result.setAccountInfo(accountInfo);
        result.setBizSerial(bizSerial);
        result.setFeeSerials(feeSerials);
        return result;
    }


    @Override
    @GlobalTransactional(rollbackFor = Exception.class)
    @Transactional(rollbackFor = Exception.class)
    public Long add(ReverseRequestDto requestDto) {
        ReverseDetailResponseDto detail = this.getDetail(requestDto.getBizSerialNo(), requestDto.getFirmId());
        BusinessRecordResponseDto bizSerial = detail.getBizSerial();
        List<SerialRecordResponseDto> feeSerials = detail.getFeeSerials();
        UserAccountCardResponseDto userAccount = detail.getAccountInfo();
        ReverseRecord reverseRecord = reverseRecordDao.findByBizSerialNo(requestDto.getBizSerialNo(), requestDto.getFirmId());
        if (reverseRecord != null) {
            throw new CardAppBizException("只允许做一次冲正操作");
        }
        if (!accountCycleService.isActiveByCycleNo(bizSerial.getCycleNo(), requestDto.getFirmId())) {
            throw new CardAppBizException("该业务单已平账，不能进行冲正操作");
        }
        //计算冲正金额
        ReverseAmountBo reverseAmountBo = new ReverseAmountBo(
                requestDto.getAmount(),
                bizSerial.getAmount(),
                requestDto.getFee(),
                this.getTotalFee(feeSerials),
                bizSerial.getTradeType());
        Long reverseAmount = reverseAmountBo.calcReverseAmount();
        Long inAccChangeAmount = reverseAmountBo.calcInAccChangeAmount();
        //发起冲正
        PayReverseRequestDto tradeRequest = PayReverseRequestDto.createReverse(
                userAccount, bizSerial.getTradeNo(), reverseAmount
        );
        if (inAccChangeAmount != 0) {
            tradeRequest.addFee(inAccChangeAmount, FundItem.REVERSE_FEE);
        }
        TradeResponseDto tradeResponse = GenericRpcResolver.resolver(payRpc.reverse(tradeRequest), ServiceName.PAY);
        //保存业务办理记录
        BusinessRecordDo businessRecord = this.saveBizRecord(requestDto, bizSerial, userAccount, reverseAmount, tradeResponse);
        //保存冲正记录
        ReverseRecord newReverseRecord = ReverseRecord.create(businessRecord, reverseAmount, inAccChangeAmount);
        this.setOperator(newReverseRecord, requestDto);
        this.copyFromOriginalSerial(newReverseRecord, bizSerial);
        reverseRecordDao.save(newReverseRecord);
        return newReverseRecord.getReverseId();
    }

    private BusinessRecordDo saveBizRecord(ReverseRequestDto requestDto, BusinessRecordResponseDto bizSerial, UserAccountCardResponseDto userAccount, Long reverseAmount, TradeResponseDto tradeResponse) {
        BusinessRecordDo businessRecord = serialService.createBusinessRecord(requestDto, userAccount, record -> {
            record.setType(OperateType.FUND_REVERSE.getCode());
            record.setAmount(Math.abs(reverseAmount));
            record.setTradeType(bizSerial.getTradeType());
            record.setTradeChannel(bizSerial.getTradeChannel());
            record.setTradeNo(tradeResponse.getTradeId());
        });
        serialService.saveBusinessRecord(businessRecord);
        SerialDto serialDto = serialService.createAccountSerialWithFund(businessRecord, tradeResponse, (serialRecord, feeType) -> {
            if (Integer.valueOf(FeeType.ACCOUNT.getCode()).equals(feeType)) {
                serialRecord.setFundItem(FundItem.REVERSE.getCode());
                serialRecord.setFundItemName(FundItem.REVERSE.getName());
            }
            if (Integer.valueOf(FeeType.SERVICE.getCode()).equals(feeType)) {
                serialRecord.setFundItem(FundItem.REVERSE_FEE.getCode());
                serialRecord.setFundItemName(FundItem.REVERSE_FEE.getName());
            }
        });
        serialService.handleSuccess(serialDto);
        return businessRecord;
    }

    private List<ReverseRecordResponseDto> convert2ListDto(List<ReverseRecord> list) {
        return list.stream().map(reverseRecord -> {
            ReverseRecordResponseDto dto = new ReverseRecordResponseDto();
            dto.setSerialNo(reverseRecord.getSerialNo());
            dto.setAmount(reverseRecord.getAmount());
            dto.setInAccChangeAmount(reverseRecord.getInAccChangeAmount());
            dto.setBizSerialNo(reverseRecord.getBizSerialNo());
            dto.setBizTradeType(reverseRecord.getBizTradeType());
            dto.setCreateTime(reverseRecord.getCreateTime());
            dto.setReverseId(reverseRecord.getReverseId());
            dto.setOpId(reverseRecord.getOperatorId());
            dto.setOpName(reverseRecord.getOperatorName());
            dto.setOpNo(reverseRecord.getOperatorNo());
            dto.setFirmId(reverseRecord.getFirmId());
            dto.setFirmName(reverseRecord.getFirmName());
            return dto;
        }).collect(Collectors.toList());
    }


    private void setOperator(ReverseRecord newReverseRecord, ReverseRequestDto requestDto) {
        newReverseRecord.setFirmId(requestDto.getFirmId());
        newReverseRecord.setFirmName(requestDto.getFirmName());
        newReverseRecord.setOperatorId(requestDto.getOpId());
        newReverseRecord.setOperatorNo(requestDto.getOpNo());
        newReverseRecord.setOperatorName(requestDto.getOpName());
    }

    private void copyFromOriginalSerial(ReverseRecord newReverseRecord, BusinessRecordResponseDto bizSerial) {
        newReverseRecord.setBizSerialNo(bizSerial.getSerialNo());
        newReverseRecord.setBizTradeType(bizSerial.getTradeType());
    }

    private Long getTotalFee(List<SerialRecordResponseDto> feeRecords) {
        return feeRecords.stream()
                .mapToLong(serialRecordDo -> NumberUtils.toLong(serialRecordDo.getAmount() + ""))
                .sum();
    }

    private List<SerialRecordResponseDto> getFeeSerialRecords(List<SerialRecordDo> serialRecordDos) {
        List<SerialRecordResponseDto> feeSerials = new ArrayList<>();
        for (SerialRecordDo serialRecordDo : serialRecordDos) {
            if (ReverseFundItemMap.isFeeFundItem(serialRecordDo.getFundItem())) {
                SerialRecordResponseDto dto = new SerialRecordResponseDto();
                BeanUtils.copyProperties(serialRecordDo, dto);
                feeSerials.add(dto);
            }
        }
        return feeSerials;
    }
}
