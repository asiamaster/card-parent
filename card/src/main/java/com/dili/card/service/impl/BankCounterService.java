package com.dili.card.service.impl;

import java.util.List;
import java.util.stream.Collectors;

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
