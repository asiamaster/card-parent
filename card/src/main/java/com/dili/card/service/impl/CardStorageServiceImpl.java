package com.dili.card.service.impl;

import com.dili.card.dao.IApplyRecordDao;
import com.dili.card.dto.ApplyRecordQueryDto;
import com.dili.card.dto.ApplyRecordResponseDto;
import com.dili.card.entity.ApplyRecordDo;
import com.dili.card.service.ICardStorageService;
import com.dili.card.util.PageUtils;
import com.dili.ss.domain.PageOutput;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @Auther: miaoguoxin
 * @Date: 2020/7/1 16:58
 */
@Service
public class CardStorageServiceImpl implements ICardStorageService {
    @Autowired
    private IApplyRecordDao applyRecordDao;

    @Override
    public PageOutput<List<ApplyRecordResponseDto>> getPage(ApplyRecordQueryDto queryDto) {
        Page<ApplyRecordResponseDto> page = PageHelper.startPage(queryDto.getPage(), queryDto.getRows());
        List<ApplyRecordResponseDto> list = this.getByCondition(queryDto);
        return PageUtils.convert2PageOutput(page, list);
    }

    @Override
    public List<ApplyRecordResponseDto> getByCondition(ApplyRecordQueryDto queryDto) {
        queryDto.setDefSort("apply_time").setDefOrder("DESC");
        List<ApplyRecordDo> applyRecordDos = applyRecordDao.selectListByCondition(queryDto);
        return applyRecordDos.stream().map(record -> {
            ApplyRecordResponseDto recordResponseDto = new ApplyRecordResponseDto();
            BeanUtils.copyProperties(record, recordResponseDto);
            return recordResponseDto;
        }).collect(Collectors.toList());
    }

}
