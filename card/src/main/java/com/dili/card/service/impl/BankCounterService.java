package com.dili.card.service.impl;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.StrUtil;
import com.dili.card.dto.BankCounterPrintResponseDto;
import com.dili.card.exception.CardAppBizException;
import com.dili.card.type.PrintTemplate;
import com.dili.card.util.CurrencyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dili.card.dao.IBankCounterDao;
import com.dili.card.dto.BankCounterQuery;
import com.dili.card.dto.BankCounterRequestDto;
import com.dili.card.dto.BankCounterResponseDto;
import com.dili.card.entity.BankCounterDo;
import com.dili.card.service.IBankCounterService;
import com.dili.card.util.PageUtils;
import com.dili.ss.domain.PageOutput;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;

/**
 * @Auther: miaoguoxin
 * @Date: 2020/12/9 16:00
 * @Description:
 */
@Service
public class BankCounterService implements IBankCounterService {
    @Autowired
    private IBankCounterDao bankCounterDao;

    @Override
    public PageOutput<List<BankCounterResponseDto>> getPage(BankCounterQuery param) {
        Page<?> page = PageHelper.startPage(param.getPage(), param.getRows());
        List<BankCounterDo> list = bankCounterDao.findByCondition(param);
        return PageUtils.convert2PageOutput(page, this.convert2RespList(list));
    }

    @Override
    public void add(BankCounterRequestDto requestDto) {
        BankCounterDo bankCounterDo = BankCounterDo.create(requestDto);
        bankCounterDao.save(bankCounterDo);
    }

    @Override
    public BankCounterPrintResponseDto getPrintData(Long id) {
        BankCounterDo bankCounterDo = bankCounterDao.getById(id);
        if (bankCounterDo == null){
            throw new CardAppBizException("存取款记录不存在");
        }
        return copyPrintData(bankCounterDo);
    }

    private BankCounterPrintResponseDto copyPrintData(BankCounterDo bankCounterDo) {
        BankCounterPrintResponseDto responseDto = new BankCounterPrintResponseDto();
        responseDto.setReceiveAccountNo("123");
        responseDto.setReceiveCompany("xxx公司");
        responseDto.setAction(bankCounterDo.getAction());
        responseDto.setAmountText(formatPrintAmount(String.valueOf(bankCounterDo.getAmount())));
        responseDto.setApplyTime(bankCounterDo.getApplyTime());
        responseDto.setAmountCN(Convert.digitToChinese(new BigDecimal(responseDto.getAmountText())));
        responseDto.setCreatedTime(bankCounterDo.getCreatedTime());
        responseDto.setDescription(bankCounterDo.getDescription());
        responseDto.setOperatorId(bankCounterDo.getOperatorId());
        responseDto.setOperatorName(bankCounterDo.getOperatorName());
        responseDto.setSerialNo(bankCounterDo.getSerialNo());
        responseDto.setPrintTemplate(PrintTemplate.BANK_COUNTER.getType());
        return responseDto;
    }

    private static String formatPrintAmount(String amountText){
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < amountText.length(); i++) {
            char c = amountText.charAt(i);
            if (i != amountText.length()-1){
                sb.append(c).append(" ");
            }else {
                sb.append(c);
            }
        }
        return sb.toString();
    }

    private List<BankCounterResponseDto> convert2RespList(List<BankCounterDo> bankCounterDos) {
        return bankCounterDos.stream().map(bankCounterDo -> {
            BankCounterResponseDto dto = new BankCounterResponseDto();
            dto.setId(bankCounterDo.getId());
            dto.setAction(bankCounterDo.getAction());
            dto.setAmount(bankCounterDo.getAmount());
            dto.setApplyTime(bankCounterDo.getApplyTime());
            dto.setCreatedTime(bankCounterDo.getCreatedTime());
            dto.setDescription(bankCounterDo.getDescription());
            dto.setOperatorId(bankCounterDo.getOperatorId());
            dto.setOperatorName(bankCounterDo.getOperatorName());
            dto.setSerialNo(bankCounterDo.getSerialNo());
            return dto;
        }).collect(Collectors.toList());

    }
}
