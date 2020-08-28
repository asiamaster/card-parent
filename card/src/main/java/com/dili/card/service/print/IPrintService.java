package com.dili.card.service.print;

import com.dili.card.dto.PrintDto;
import com.dili.card.entity.BusinessRecordDo;

import java.util.Map;

/**
 * 打印相关接口
 */
public interface IPrintService {

    /**
     * 构建打印数据
     * @return
     */
    Map<String, Object> create(BusinessRecordDo recordDo, boolean reprint);

    /**
     * 获取票据模板名称
     * @return
     */
    String getPrintTemplate(BusinessRecordDo recordDo);

    /**
     * 方便各业务自己修改数据
     * @param printDto
     * @param recordDo
     * @param reprint
     */
    void createSpecial(PrintDto printDto, BusinessRecordDo recordDo, boolean reprint);

    /**
     * 支持的操作类型
     * @return
     */
    Integer support();
}
