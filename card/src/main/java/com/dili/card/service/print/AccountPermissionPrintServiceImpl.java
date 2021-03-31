package com.dili.card.service.print;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.dili.card.common.constant.Constant;
import com.dili.card.common.constant.ServiceName;
import com.dili.card.dto.PrintDto;
import com.dili.card.dto.pay.AccountAllPermission;
import com.dili.card.dto.pay.AccountPermissionResponseDto;
import com.dili.card.entity.BusinessRecordDo;
import com.dili.card.exception.CardAppBizException;
import com.dili.card.rpc.PayRpc;
import com.dili.card.rpc.resolver.GenericRpcResolver;
import com.dili.card.type.CardType;
import com.dili.card.type.OperateType;
import com.dili.card.type.PrintTemplate;
import com.dili.card.util.CurrencyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @Auther: miaoguoxin
 * @Date: 2021/3/31 16:09
 * @Description:
 */
@Service
public class AccountPermissionPrintServiceImpl extends PrintServiceImpl {
    @Autowired
    private PayRpc payRpc;

    @Override
    public String getPrintTemplate(BusinessRecordDo recordDo) {
        return PrintTemplate.ACCOUNT_PERMISSION.getType();
    }

    @Override
    public void createSpecial(PrintDto printDto, BusinessRecordDo recordDo, boolean reprint) {
        String attach = recordDo.getAttach();
        if (StrUtil.isBlank(attach)) {
            throw new CardAppBizException("交易记录数据异常");
        }

        JSONObject attachObj = JSON.parseObject(attach);
        Integer cardType = attachObj.getInteger(Constant.BUSINESS_RECORD_ATTACH_CARDTYPE);
        printDto.setCardType(CardType.getName(cardType));
        printDto.setCustomerCertificateNumber(attachObj.getString(Constant.BUSINESS_RECORD_ATTACH_CERT_NO));

        JSONObject params = new JSONObject();
        params.put("accountId", attachObj.getLong(Constant.BUSINESS_RECORD_ATTACH_FUND_ACCOUNTID));
        AccountPermissionResponseDto responseDto = GenericRpcResolver.resolver(payRpc.loadPermission(params), ServiceName.PAY);
        Set<Integer> selectedCode = responseDto.getPermission();
        List<AccountAllPermission> allPermission = responseDto.getAllPermission();
        String permissions = allPermission.stream()
                .filter(p -> selectedCode.contains(p.getCode()))
                .map(AccountAllPermission::getName)
                .collect(Collectors.joining("、"));

        printDto.setPermissionNames(permissions);
        Long dailyAmountFen = responseDto.getTrade().getDailyAmount();
        Long withdrawAmountFen = responseDto.getWithdraw().getDailyAmount();
        printDto.setTradeDailyAmount(CurrencyUtils.cent2TenNoSymbol(dailyAmountFen));
        printDto.setWithdrawDailyAmount(CurrencyUtils.cent2TenNoSymbol(withdrawAmountFen));
        printDto.setWithdrawDailyTimes(responseDto.getWithdraw().getDailyTimes());
        printDto.setTradeDailyTimes(responseDto.getTrade().getDailyTimes());
    }

    @Override
    public Integer support() {
        return OperateType.PERMISSION_SET.getCode();
    }
}
