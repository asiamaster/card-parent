package com.dili.card.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.dili.card.common.constant.Constant;
import com.dili.card.common.constant.ServiceName;
import com.dili.card.dao.IReverseRecordDao;
import com.dili.card.dto.BusinessRecordResponseDto;
import com.dili.card.dto.ReverseDetailResponseDto;
import com.dili.card.dto.ReverseRecordQueryDto;
import com.dili.card.dto.ReverseRecordResponseDto;
import com.dili.card.dto.ReverseRequestDto;
import com.dili.card.dto.SerialDto;
import com.dili.card.dto.SerialRecordResponseDto;
import com.dili.card.dto.UserAccountCardResponseDto;
import com.dili.card.dto.UserAccountSingleQueryDto;
import com.dili.card.dto.pay.PayReverseRequestDto;
import com.dili.card.dto.pay.TradeResponseDto;
import com.dili.card.entity.BusinessRecordDo;
import com.dili.card.entity.ReverseRecord;
import com.dili.card.entity.SerialRecordDo;
import com.dili.card.entity.bo.FeeSerialRecordBo;
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
import com.dili.card.type.FundItemMap;
import com.dili.card.type.OperateType;
import com.dili.card.type.TradeChannel;
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
        if (TradeChannel.BANK.getCode() == businessRecord.getTradeChannel()) {
            throw new CardAppBizException("圈提不允许 冲正");
        }
        ReverseRecord reverseRecord = reverseRecordDao.findByBizSerialNo(serialNo, firmId);
        if (reverseRecord != null) {
            throw new CardAppBizException("只允许做一次冲正操作");
        }
        BusinessRecordResponseDto bizSerial = new BusinessRecordResponseDto();
        BeanUtils.copyProperties(businessRecord, bizSerial);
        //查询手续费记录
        FeeSerialRecordBo feeSerialRecordBo = serialService.getFeeSerialListBySerialNo(serialNo);
        //期末要求计算手续费
        bizSerial.setEndBalance(feeSerialRecordBo.calculateEndBalanceWhenDeductFee());
        //查询卡账户
        UserAccountSingleQueryDto accountQueryDto = new UserAccountSingleQueryDto();
        accountQueryDto.setAccountId(bizSerial.getAccountId());
        accountQueryDto.setCardNo(bizSerial.getCardNo());
        UserAccountCardResponseDto accountInfo = accountQueryRpcResolver.findSingle(accountQueryDto);

        ReverseDetailResponseDto result = new ReverseDetailResponseDto();
        result.setAccountInfo(accountInfo);
        result.setBizSerial(bizSerial);
        result.setFeeSerials(feeSerialRecordBo.getFeeList());
        result.setTotalSerials(feeSerialRecordBo.getTotalList());
        return result;
    }


    @Override
    @GlobalTransactional(rollbackFor = Exception.class)
    @Transactional(rollbackFor = Exception.class)
    public Long add(ReverseRequestDto requestDto) {
        ReverseDetailResponseDto detail = this.getDetail(requestDto.getBizSerialNo(), requestDto.getFirmId());
        BusinessRecordResponseDto bizSerial = detail.getBizSerial();
        List<SerialRecordResponseDto> feeSerials = detail.getFeeSerials();
        List<SerialRecordResponseDto> totalSerials = detail.getTotalSerials();
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
        FundItem serviceFeeItem = this.getServiceFeeItem(totalSerials);
        if (inAccChangeAmount != 0) {
            tradeRequest.addFee(inAccChangeAmount, serviceFeeItem);
        }
        TradeResponseDto tradeResponse = GenericRpcResolver.resolver(payRpc.reverse(tradeRequest), ServiceName.PAY);
        //保存业务办理记录
        BusinessRecordDo businessRecord = this.saveBizRecord(requestDto, bizSerial, userAccount, reverseAmount, tradeResponse, totalSerials);
        //保存冲正记录
        ReverseRecord newReverseRecord = ReverseRecord.create(businessRecord, reverseAmount, inAccChangeAmount);
        this.setOperator(newReverseRecord, requestDto);
        this.copyFromOriginalSerial(newReverseRecord, bizSerial);
        reverseRecordDao.save(newReverseRecord);
        return newReverseRecord.getReverseId();
    }

    private BusinessRecordDo saveBizRecord(ReverseRequestDto requestDto, BusinessRecordResponseDto bizSerial, UserAccountCardResponseDto userAccount, Long reverseAmount, TradeResponseDto tradeResponse, List<SerialRecordResponseDto> feeSerials) {
        String note = String.format("冲正操作，原业务流水号[%s]", bizSerial.getSerialNo());
        BusinessRecordDo businessRecord = serialService.createBusinessRecord(requestDto, userAccount, record -> {
            record.setType(OperateType.FUND_REVERSE.getCode());
            record.setAmount(Math.abs(reverseAmount));
            record.setTradeType(bizSerial.getTradeType());
            record.setTradeChannel(bizSerial.getTradeChannel());
            record.setTradeNo(tradeResponse.getTradeId());
            JSONObject attachObj = new JSONObject();
            attachObj.put(Constant.BUSINESS_RECORD_NO, bizSerial.getSerialNo());
            record.setAttach(attachObj.toJSONString());
            record.setNotes(note);
        });
        serialService.saveBusinessRecord(businessRecord);
        SerialDto serialDto = serialService.createAccountSerialWithFund(businessRecord, tradeResponse, (serialRecord, feeType) -> {
            FundItem fundItem;
            if (Integer.valueOf(FeeType.ACCOUNT.getCode()).equals(feeType)) {
                fundItem = this.getAccountFeeItem(feeSerials);
            } else {
                fundItem = this.getServiceFeeItem(feeSerials);
            }
            if (fundItem != null) {
                serialRecord.setFundItem(fundItem.getCode());
                serialRecord.setFundItemName(fundItem.getName());
            }
            serialRecord.setNotes(note);
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
            dto.setBizType(reverseRecord.getBizType());
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
        newReverseRecord.setBizType(bizSerial.getType());
        newReverseRecord.setCycleNo(bizSerial.getCycleNo());
    }

    private Long getTotalFee(List<SerialRecordResponseDto> feeRecords) {
        return feeRecords.stream()
                .mapToLong(serialRecordDo -> NumberUtils.toLong(serialRecordDo.getAmount() + ""))
                .sum();
    }

    private FundItem getAccountFeeItem(List<SerialRecordResponseDto> recordResponseDtos) {
        for (SerialRecordResponseDto serialRecordDo : recordResponseDtos) {
            if (!FundItemMap.isFeeFundItem(serialRecordDo.getFundItem())) {
                return FundItem.getByCode(serialRecordDo.getFundItem());
            }
        }
        return null;
    }

    private FundItem getServiceFeeItem(List<SerialRecordResponseDto> recordResponseDtos) {
        for (SerialRecordResponseDto serialRecordDo : recordResponseDtos) {
            if (FundItemMap.isFeeFundItem(serialRecordDo.getFundItem())) {
                return FundItem.getByCode(serialRecordDo.getFundItem());
            }
        }
        return null;
    }

    private List<SerialRecordResponseDto> getTotalSerialRecords(List<SerialRecordDo> serialRecordDos) {
        return serialRecordDos.stream().map(s -> {
            SerialRecordResponseDto dto = new SerialRecordResponseDto();
            BeanUtils.copyProperties(s, dto);
            return dto;
        }).collect(Collectors.toList());
    }

    private List<SerialRecordResponseDto> getFeeSerialRecords(List<SerialRecordDo> serialRecordDos) {
        List<SerialRecordResponseDto> feeSerials = new ArrayList<>();
        for (SerialRecordDo serialRecordDo : serialRecordDos) {
            if (FundItemMap.isFeeFundItem(serialRecordDo.getFundItem())) {
                SerialRecordResponseDto dto = new SerialRecordResponseDto();
                BeanUtils.copyProperties(serialRecordDo, dto);
                feeSerials.add(dto);
            }
        }
        return feeSerials;
    }
}
