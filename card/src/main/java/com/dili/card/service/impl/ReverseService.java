package com.dili.card.service.impl;

import cn.hutool.core.util.NumberUtil;
import com.dili.card.dao.IReverseRecordDao;
import com.dili.card.dto.BusinessRecordResponseDto;
import com.dili.card.dto.ReverseDetailResponseDto;
import com.dili.card.dto.ReverseRecordQueryDto;
import com.dili.card.dto.ReverseRecordResponseDto;
import com.dili.card.dto.ReverseRequestDto;
import com.dili.card.dto.SerialQueryDto;
import com.dili.card.dto.UserAccountCardResponseDto;
import com.dili.card.dto.UserAccountSingleQueryDto;
import com.dili.card.entity.BusinessRecordDo;
import com.dili.card.entity.ReverseRecord;
import com.dili.card.entity.SerialRecordDo;
import com.dili.card.exception.CardAppBizException;
import com.dili.card.rpc.resolver.AccountQueryRpcResolver;
import com.dili.card.rpc.resolver.SerialRecordRpcResolver;
import com.dili.card.service.IAccountCycleService;
import com.dili.card.service.IReverseService;
import com.dili.card.service.ISerialService;
import com.dili.card.type.OperateType;
import com.dili.card.type.ReverseFundItemMap;
import com.dili.card.type.TradeType;
import com.dili.card.util.PageUtils;
import com.dili.ss.domain.PageOutput;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import io.seata.spring.annotation.GlobalTransactional;
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
        if (!TradeType.canReverseType(businessRecord.getTradeType())) {
            throw new CardAppBizException("只允许充值和提款业务 冲正");
        }

        BusinessRecordResponseDto bizSerial = new BusinessRecordResponseDto();
        BeanUtils.copyProperties(businessRecord, bizSerial);

        //查询操作记录
        SerialQueryDto queryDto = new SerialQueryDto();
        queryDto.setSerialNo(serialNo);
        List<SerialRecordDo> serialRecordDos = serialRecordRpcResolver.getList(queryDto);
        List<SerialRecordDo> feeSerials = new ArrayList<>();
        for (SerialRecordDo serialRecordDo : serialRecordDos) {
            if (ReverseFundItemMap.isFeeFundItem(serialRecordDo.getFundItem())) {
                feeSerials.add(serialRecordDo);
            }
        }

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
        UserAccountCardResponseDto userAccount = detail.getAccountInfo();
        //校验金额
        this.validateAmount(requestDto.getAmount(), requestDto.getFee(), bizSerial, detail.getFeeSerials());
        ReverseRecord reverseRecord = reverseRecordDao.findByBizSerialNo(requestDto.getBizSerialNo(), requestDto.getFirmId());
        if (reverseRecord != null) {
            throw new CardAppBizException("只允许做一次冲正操作");
        }
        if (!accountCycleService.isActiveByCycleNo(bizSerial.getCycleNo())) {
            throw new CardAppBizException("该业务单已平账");
        }

        Long reverseAmount = calcReverseAmount(requestDto.getAmount(), requestDto.getFee());
        BusinessRecordDo businessRecord = serialService.createBusinessRecord(requestDto, userAccount, record -> {
            record.setType(OperateType.FUND_REVERSE.getCode());
            record.setAmount(reverseAmount);
            record.setTradeType(bizSerial.getTradeType());
            record.setTradeChannel(bizSerial.getTradeChannel());
        });
        //TODO 待支付系统增加冲正接口
        //保存业务办理记录
        businessRecord.setTradeNo("tradeNo");
        serialService.saveBusinessRecord(businessRecord);

        ReverseRecord newReverseRecord = ReverseRecord.create(businessRecord.getSerialNo(), reverseAmount);
        this.setOperator(newReverseRecord, requestDto);
        this.copyFromOriginalSerial(newReverseRecord, bizSerial);
        reverseRecordDao.save(newReverseRecord);

        return newReverseRecord.getReverseId();
//        SerialDto serialDto = serialService.createAccountSerialWithFund(businessRecord, tradeResponseDto, (serialRecord, feeType) -> {
//        });
//        serialService.handleSuccess(serialDto);
    }

    private List<ReverseRecordResponseDto> convert2ListDto(List<ReverseRecord> list) {
        return list.stream().map(reverseRecord -> {
            ReverseRecordResponseDto dto = new ReverseRecordResponseDto();
            dto.setAmount(reverseRecord.getAmount());
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

    /**
     * 校验输入金额
     * @author miaoguoxin
     * @date 2020/11/25
     */
    private void validateAmount(Long amount, Long fee, BusinessRecordResponseDto bizRecord, List<SerialRecordDo> feeRecords) {
        if (Math.abs(amount) > bizRecord.getAmount()) {
            throw new CardAppBizException("冲正金额不能超过操作金额");
        }
        long totalFee = feeRecords.stream().mapToLong(SerialRecordDo::getAmount).sum();
        if (Math.abs(fee) > totalFee) {
            throw new CardAppBizException("收益账户变动金额不能超过费用金额");
        }
        if (bizRecord.getTradeType() == TradeType.DEPOSIT.getCode()) {
            if (amount >= 0) {
                throw new CardAppBizException("充值冲正金额只允许为负");
            }
        }
        if (bizRecord.getTradeType() == TradeType.WITHDRAW.getCode()) {
            if (amount <= 0) {
                throw new CardAppBizException("提款冲正金额只允许为正");
            }
        }
    }

    /**
     * 计算冲正金额
     * @author miaoguoxin
     * @date 2020/11/25
     */
    private static Long calcReverseAmount(Long amount, Long fee) {
        return NumberUtil.sub(amount, fee).longValue();
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
}
