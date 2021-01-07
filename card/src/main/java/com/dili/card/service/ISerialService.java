package com.dili.card.service;

import com.dili.card.dto.BusinessRecordResponseDto;
import com.dili.card.dto.CardRequestDto;
import com.dili.card.dto.SerialDto;
import com.dili.card.dto.SerialQueryDto;
import com.dili.card.dto.UserAccountCardResponseDto;
import com.dili.card.dto.pay.TradeResponseDto;
import com.dili.card.entity.BusinessRecordDo;
import com.dili.card.entity.SerialRecordDo;
import com.dili.card.service.serial.IAccountSerialFilter;
import com.dili.card.service.serial.IBusinessRecordFilter;
import com.dili.ss.domain.PageOutput;

import java.util.List;

/**
 * 操作流水记录service接口
 * @author xuliang
 */
public interface ISerialService {

    /**
     * 保存业务办理记录
     * @param businessRecord
     */
    void saveBusinessRecord(BusinessRecordDo businessRecord);

    /**
     * 拷贝公共字段
     * @param serialRecord
     * @param businessRecord
     */
    void copyCommonFields(SerialRecordDo serialRecord, BusinessRecordDo businessRecord);

    /**
     * 失败处理 改状态
     * @param serialDto
     */
    void handleFailure(SerialDto serialDto);

    /**
     * 成功处理 改状态、记操作流水
     * @param serialDto
     */
    void handleSuccess(SerialDto serialDto);

    /**
     * 成功处理 改状态、记操作流水
     * @param serialDto
     * * @param saveSerial 是否保存账户流水标记
     */
    void handleSuccess(SerialDto serialDto, boolean saveSerial);

    /**
     * 保存账户流水
     * @param serialDto
     */
    void saveSerialRecord(SerialDto serialDto);

    /**
     * 获取操作员当前账期内用于补打的操作记录
     * @param serialQueryDto
     * @return
     */
    List<BusinessRecordDo> cycleReprintList(SerialQueryDto serialQueryDto);

    /**
     * 获取客户今日充值记录列表
     * @param serialQueryDto
     * @return
     */
    List<BusinessRecordDo> todayChargeList(SerialQueryDto serialQueryDto);

    /**
     * 根据条件查询业务办理记录
     * @param serialQueryDto
     * @return
     */
    List<BusinessRecordDo> queryBusinessRecord(SerialQueryDto serialQueryDto);

    /**
     * 分页查询
     * @author miaoguoxin
     * @date 2020/7/1
     */
    PageOutput<List<BusinessRecordResponseDto>> queryPage(SerialQueryDto serialQueryDto);

    /**
     * 构建业务记录数据并提供回调处理
     * @param cardRequestDto
     * @param accountCard
     * @param filter
     * @return
     */
    BusinessRecordDo createBusinessRecord(CardRequestDto cardRequestDto, UserAccountCardResponseDto accountCard, IBusinessRecordFilter filter);

    /**
     * 构建业务记录数据并提供回调处理
     * @param cardRequestDto
     * @param accountCard
     * @param filter
     * @return
     */
    BusinessRecordDo createBusinessRecord(CardRequestDto cardRequestDto, UserAccountCardResponseDto accountCard, IBusinessRecordFilter filter, Long cycleNo);
    
    /**
     * 构建流水记录并提供回调处理 没有资金操作的
     * @param businessRecord
     * @param filter
     * @return
     */
    SerialDto createAccountSerial(BusinessRecordDo businessRecord, IAccountSerialFilter filter);

    /**
     * 构建流水记录并提供回调处理 有资金操作
     * @param businessRecord
     * @param tradeResponseDto
     * @param filter
     * @return
     */
    SerialDto createAccountSerialWithFund(BusinessRecordDo businessRecord, TradeResponseDto tradeResponseDto, IAccountSerialFilter filter);

    /**
     * 构建流水记录并提供回调处理 有资金操作
     * @param businessRecord
     * @param tradeResponseDto
     * @param filter
     * @param isFrozen 标记是否是冻结/解冻资金操作
     * @return
     */
    SerialDto createAccountSerialWithFund(BusinessRecordDo businessRecord, TradeResponseDto tradeResponseDto, IAccountSerialFilter filter, boolean isFrozen);

    /**
     * 根据操作流水查询办理记录
     * @param serialNo
     * @return
     */
    BusinessRecordDo findBusinessRecordBySerialNo(String serialNo);
}
