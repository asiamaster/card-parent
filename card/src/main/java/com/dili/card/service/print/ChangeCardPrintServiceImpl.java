package com.dili.card.service.print;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.dili.card.common.constant.Constant;
import com.dili.card.dto.PrintDto;
import com.dili.card.entity.BusinessRecordDo;
import com.dili.card.type.OperateType;
import com.dili.card.type.PrintTemplate;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

/**
 * 换卡打印
 * @Auther: miaoguoxin
 * @Date: 2020/9/7 09:24
 */
@Service
public class ChangeCardPrintServiceImpl extends PrintServiceImpl{
    @Override
    public String getPrintTemplate(BusinessRecordDo recordDo) {
        return PrintTemplate.CHANGE_CARD.getType();
    }

    @Override
    public void createSpecial(PrintDto printDto, BusinessRecordDo recordDo, boolean reprint) {
        String attach = recordDo.getAttach();
        if (!StringUtils.isNoneBlank(attach)) {
            return;
        }
        JSONObject attachObj = JSON.parseObject(attach);
        String oldCardNo = attachObj.getString(Constant.OLD_CARD_NO_PARAM);
        printDto.setOldCardNo(oldCardNo);
    }

    @Override
    public Integer support() {
        return OperateType.CHANGE.getCode();
    }
}
