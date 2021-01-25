package com.dili.card.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.dili.card.common.constant.ServiceName;
import com.dili.card.dto.BindBankCardDto;
import com.dili.card.dto.FirmWithdrawInitResponseDto;
import com.dili.card.dto.pay.BalanceResponseDto;
import com.dili.card.dto.pay.MerAccountResponseDto;
import com.dili.card.rpc.PayRpc;
import com.dili.card.rpc.resolver.GenericRpcResolver;
import com.dili.card.rpc.resolver.PayRpcResolver;
import com.dili.card.service.IBindBankCardService;
import com.dili.card.service.IFirmWithdrawService;
import com.dili.uap.sdk.domain.Firm;
import com.dili.uap.sdk.rpc.FirmRpc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Auther: miaoguoxin
 * @Date: 2021/1/22 16:00
 * @Description:
 */
@Service
public class FirmWithdrawService implements IFirmWithdrawService {
    @Autowired
    private IBindBankCardService bindBankCardService;
    @Autowired
    private PayRpcResolver payRpcResolver;
    @Autowired
    private FirmRpc firmRpc;
    @Autowired
    private PayRpc payRpc;

    @Override
    public FirmWithdrawInitResponseDto init(Long firmId) {
        // 查询市场信息
        Firm firm = GenericRpcResolver.resolver(firmRpc.getById(firmId),
                ServiceName.UAP);
        // 查询商户信息
        JSONObject params = new JSONObject();
        params.put("mchId", firmId);
        MerAccountResponseDto merInfo = GenericRpcResolver.resolver(payRpc.getMerAccount(params),
                ServiceName.PAY);
        //查询商户绑定银行卡,fundAccountId和accountId目前相同
        BindBankCardDto bindBankCardDto = new BindBankCardDto();
        bindBankCardDto.setAccountId(merInfo.getVouchAccount());
        bindBankCardDto.setFirmId(firmId);
        List<BindBankCardDto> bindBankCardDtoList = bindBankCardService.getList(bindBankCardDto);
        //市场余额信息
        BalanceResponseDto balanceInfo = payRpcResolver.findBalanceByFundAccountId(merInfo.getVouchAccount());
        FirmWithdrawInitResponseDto result = new FirmWithdrawInitResponseDto();
        result.setMerInfo(merInfo);
        result.setFirm(firm);
        result.setBalanceInfo(balanceInfo);
        result.setBindBankCardList(bindBankCardDtoList);
        return result;
    }
}
