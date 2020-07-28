package com.dili.card.service.impl;

import com.dili.card.dao.IStorageOutDao;
import com.dili.card.dto.CardStorageDto;
import com.dili.card.dto.CardStorageOutQueryDto;
import com.dili.card.dto.CardStorageOutRequestDto;
import com.dili.card.dto.CardStorageOutResponseDto;
import com.dili.card.entity.CardStorageOut;
import com.dili.card.exception.CardAppBizException;
import com.dili.card.rpc.CardStorageRpc;
import com.dili.card.rpc.resolver.GenericRpcResolver;
import com.dili.card.service.ICardStorageService;
import com.dili.card.util.PageUtils;
import com.dili.ss.constant.ResultCode;
import com.dili.ss.domain.PageOutput;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Auther: miaoguoxin
 * @Date: 2020/7/1 16:58
 */
@Service
public class CardStorageServiceImpl implements ICardStorageService {
    @Autowired
    private IStorageOutDao storageOutDao;
    @Autowired
    private CardStorageRpc cardStorageRpc;

    @Override
    public void saveOutRecord(CardStorageOutRequestDto requestDto) {
        CardStorageOut cardStorageOut = new CardStorageOut();
        BeanUtils.copyProperties(requestDto, cardStorageOut);
        cardStorageOut.setCardNo(requestDto.getCardNos());
        cardStorageOut.setCreatorId(requestDto.getOpId());
        cardStorageOut.setCreator(requestDto.getOpName());
        cardStorageOut.setApplyTime(LocalDateTime.now());
        cardStorageOut.setCreateTime(LocalDateTime.now());
        cardStorageOut.setModifyTime(LocalDateTime.now());
        storageOutDao.save(cardStorageOut);

    }

    @Override
    public CardStorageOutResponseDto getById(Long id) {
        CardStorageOut byId = storageOutDao.getById(id);
        if (byId == null) {
            throw new CardAppBizException(ResultCode.DATA_ERROR, "领卡记录不存在");
        }
        return this.convert2ResponseDto(byId);
    }

    @Override
    public PageOutput<List<CardStorageOutResponseDto>> getPage(CardStorageOutQueryDto queryDto) {
        Page<CardStorageOutResponseDto> page = PageHelper.startPage(queryDto.getPage(), queryDto.getRows());
        List<CardStorageOutResponseDto> list = this.getByCondition(queryDto);
        return PageUtils.convert2PageOutput(page, list);
    }

    @Override
    public List<CardStorageOutResponseDto> getByCondition(CardStorageOutQueryDto queryDto) {
        queryDto.setDefSort("apply_time").setDefOrder("DESC");
        List<CardStorageOut> cardStorageOuts = storageOutDao.selectListByCondition(queryDto);
        return cardStorageOuts.stream()
                .map(this::convert2ResponseDto)
                .collect(Collectors.toList());
    }

    @Override
	public PageOutput<List<CardStorageDto>> cardStorageList(CardStorageDto queryParam) {
		return cardStorageRpc.pageList(queryParam);
	}

	@Override
	public void voidCard(String cardNo, String remark) {
		CardStorageDto queryParam = new CardStorageDto();
		queryParam.setCardNo(cardNo);
		GenericRpcResolver.resolver(cardStorageRpc.voidCard(queryParam), "account-service");
	}



    private CardStorageOutResponseDto convert2ResponseDto(CardStorageOut record) {
        CardStorageOutResponseDto recordResponseDto = new CardStorageOutResponseDto();
        BeanUtils.copyProperties(record, recordResponseDto);
        recordResponseDto.setConvertCardNo(record.getCardNo());
        return recordResponseDto;
    }

	@Override
	public CardStorageDto getCardStorageByCardNo(String cardNo) {
		CardStorageDto queryParam=new CardStorageDto();
		queryParam.setCardNo(cardNo);
		CardStorageDto cardStorage = GenericRpcResolver.resolver(cardStorageRpc.getCardStorageByCardNo(queryParam), "account-service");
		return cardStorage;
	}

}
