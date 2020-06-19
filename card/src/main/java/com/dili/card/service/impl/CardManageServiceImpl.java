package com.dili.card.service.impl;

import com.dili.card.dto.CardRequestDto;
import com.dili.card.entity.CardAggregationWrapper;
import com.dili.card.entity.UserLogDo;
import com.dili.card.rpc.CardManageRpc;
import com.dili.card.service.ICardManageService;
import com.dili.card.service.IUserLogService;
import com.dili.card.type.OperateType;
import com.dili.ss.domain.BaseOutput;
import com.dili.ss.exception.BusinessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDateTime;


/**
 * @description： 卡片退卡换卡等操作service实现
 *
 * @author ：WangBo
 * @time ：2020年4月28日下午5:09:47
 */
@Service("cardManageService")
public class CardManageServiceImpl implements ICardManageService {

    @Resource
    private CardManageRpc cardManageRpc;
    @Resource
    private IUserLogService userLogService;

    /**
     * TODO 目前不能保证一致性 且 操作与记录存储顺序还有待商榷
     * @param cardParam
     */
    @Transactional
    @Override
    public void unLostCard(CardRequestDto cardParam) {
        BaseOutput<CardAggregationWrapper> baseOutput = cardManageRpc.unLostCard(cardParam);
        if (!baseOutput.isSuccess()) {
            throw new BusinessException(baseOutput.getCode(), baseOutput.getMessage());
        }
        CardAggregationWrapper wrapper = baseOutput.getData();
        //暂时先这样组装数据  看以后怎么优化代码
        UserLogDo userLog = new UserLogDo();
        userLogService.setSerialNo(userLog);
        userLogService.setCustomerInfo(wrapper.getUserAccount().getCustormerId(), wrapper.getUserAccount().getFirmId(), userLog);
        userLogService.setAccountCycleInfo(cardParam.getOpId(), userLog);
        userLog.setFirmId(wrapper.getUserAccount().getFirmId());
        userLog.setAccountId(wrapper.getUserAccount().getAccountId());
        userLog.setCardNo(wrapper.getUserCard().getCardNo());
        userLog.setType(OperateType.LOSS_REMOVE.getCode());
        userLog.setTotalAmount(0L);
        userLog.setOperatorId(cardParam.getOpId());
        userLog.setOperatorNo(cardParam.getOpNo());
        userLog.setOperatorName(cardParam.getOpName());
        userLog.setOperateTime(LocalDateTime.now());
        userLogService.saveUserLog(userLog);
    }
}
