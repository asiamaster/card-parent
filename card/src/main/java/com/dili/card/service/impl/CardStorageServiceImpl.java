package com.dili.card.service.impl;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dili.card.common.constant.MarketCode;
import com.dili.card.dao.IStorageOutDao;
import com.dili.card.dao.IStorageOutDetailDao;
import com.dili.card.dto.BatchActivateCardDto;
import com.dili.card.dto.CardStorageDto;
import com.dili.card.dto.CardStorageOutQueryDto;
import com.dili.card.dto.CardStorageOutRequestDto;
import com.dili.card.dto.CardStorageOutResponseDto;
import com.dili.card.entity.CardStorageOut;
import com.dili.card.entity.StorageOutDetailDo;
import com.dili.card.exception.CardAppBizException;
import com.dili.card.rpc.CardStorageRpc;
import com.dili.card.rpc.resolver.GenericRpcResolver;
import com.dili.card.service.ICardStorageService;
import com.dili.card.type.CardStorageState;
import com.dili.card.type.CardType;
import com.dili.card.type.CustomerType;
import com.dili.card.util.PageUtils;
import com.dili.ss.constant.ResultCode;
import com.dili.ss.domain.PageOutput;
import com.dili.uap.sdk.domain.UserTicket;
import com.dili.uap.sdk.session.SessionContext;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.google.common.collect.Lists;

/**
 * @Auther: miaoguoxin
 * @Date: 2020/7/1 16:58
 */
@Service
public class CardStorageServiceImpl implements ICardStorageService {

	private static final Logger log = LoggerFactory.getLogger(CardStorageServiceImpl.class);

	@Autowired
	private IStorageOutDao storageOutDao;
	@Autowired
	private IStorageOutDetailDao storageOutDetailDao;
	@Autowired
	private CardStorageRpc cardStorageRpc;

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void saveOutRecord(CardStorageOutRequestDto requestDto) {
		CardStorageOut cardStorageOut = new CardStorageOut();
		BeanUtils.copyProperties(requestDto, cardStorageOut);
		cardStorageOut.setCreatorId(requestDto.getOpId());
		cardStorageOut.setCreator(requestDto.getOpName());
		cardStorageOut.setApplyTime(LocalDateTime.now());
		cardStorageOut.setCreateTime(LocalDateTime.now());
		cardStorageOut.setModifyTime(LocalDateTime.now());
		storageOutDao.save(cardStorageOut);
		List<StorageOutDetailDo> detailDoList = Arrays.stream(requestDto.getCardNos().split(","))
				.map(cardNo -> new StorageOutDetailDo(cardNo, cardStorageOut.getId())).collect(Collectors.toList());
		storageOutDetailDao.batchSave(detailDoList);

		List<String> cardList = Lists.newArrayList(requestDto.getCardNos().split(","));
		BatchActivateCardDto request = new BatchActivateCardDto();
		request.setCardNos(cardList);
		GenericRpcResolver.resolver(cardStorageRpc.batchActivate(request), "account-service");
	}

	@Override
	public CardStorageOutResponseDto getById(Long id) {
		CardStorageOut byId = storageOutDao.getById(id);
		if (byId == null) {
			throw new CardAppBizException(ResultCode.DATA_ERROR, "领卡记录不存在");
		}
		StorageOutDetailDo query = new StorageOutDetailDo();
		query.setStorageOutId(id);
		List<StorageOutDetailDo> detail = storageOutDetailDao.selectList(query);
		String cardNos = detail.stream().map(storageOutDetailDo -> storageOutDetailDo.getCardNo() + ",")
				.collect(Collectors.joining());
		byId.setCardNo(cardNos);
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
		return cardStorageOuts.stream().map(this::convert2ResponseDto).collect(Collectors.toList());
	}

	@Override
	public PageOutput<List<CardStorageDto>> cardStorageList(CardStorageDto queryParam) {
		PageOutput<List<CardStorageDto>> pageList = cardStorageRpc.pageList(queryParam);
		return pageList;
	}

	@Override
	public void voidCard(String cardNo, String remark) {
		CardStorageDto queryParam = new CardStorageDto();
		queryParam.setCardNo(cardNo);
		GenericRpcResolver.resolver(cardStorageRpc.voidCard(queryParam), "account-service");
	}

	@Override
	public CardStorageDto getCardStorageByCardNo(String cardNo) {
		CardStorageDto queryParam = new CardStorageDto();
		queryParam.setCardNo(cardNo);
		return GenericRpcResolver.resolver(cardStorageRpc.getCardStorageByCardNo(queryParam), "account-service");
	}

	private CardStorageOutResponseDto convert2ResponseDto(CardStorageOut record) {
		CardStorageOutResponseDto recordResponseDto = new CardStorageOutResponseDto();
		BeanUtils.copyProperties(record, recordResponseDto);
		recordResponseDto.setConvertCardNo(record.getCardNo());
		return recordResponseDto;
	}

	@Override
	public CardStorageDto checkAndGetByCardNo(String cardNo, Integer cardType, String customerType) {
		CardStorageDto cardStorage = getCardStorageByCardNo(cardNo);
		if(cardStorage == null) {
			log.warn("未找到对应的卡数据cardNo[{}]", cardNo);
			throw new CardAppBizException("未找到对应的卡数据");
		}
		if (cardStorage.getState() != CardStorageState.ACTIVE.getCode()) {
			throw new CardAppBizException("该卡状态为[" + CardStorageState.getName(cardStorage.getState()) + "],不能开卡!");
		}
		if (cardStorage.getType().intValue() != cardType) {
			throw new CardAppBizException("请使用" + CardType.getName(cardType) + "办理当前业务!");
		}
		// 副卡入库时没有卡面信息,不校验
		if (cardFaceIsMust() && null != cardStorage.getCardFace() && !CardType.isSlave(cardStorage.getType())) {
			if (!CustomerType.checkCardFace(customerType, cardStorage.getCardFace())) {
				log.warn("卡面信息和客户身份类型不符cardNo[{}]customerType[{}]cardFace[{}]", cardNo, customerType,
						cardStorage.getCardFace());
				throw new CardAppBizException("卡面信息和客户身份类型不符");
			}
		}
		return cardStorage;
	}

	@Override
	public boolean cardFaceIsMust() {
		UserTicket userTicket = SessionContext.getSessionContext().getUserTicket();
        if(userTicket == null) {
        	log.warn("卡面判断,无法获取登录用户信息,默认为非必须!");
        	return false;
        }
		if(MarketCode.SG.equalsIgnoreCase(userTicket.getFirmCode())) {
			return true;
		}
		return false;
	}
}
