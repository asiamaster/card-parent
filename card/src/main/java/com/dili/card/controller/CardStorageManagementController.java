package com.dili.card.controller;

import com.dili.card.common.handler.IControllerHandler;
import com.dili.card.dto.ApplyRecordQueryDto;
import com.dili.card.dto.ApplyRecordRequestDto;
import com.dili.card.dto.ApplyRecordResponseDto;
import com.dili.card.service.ICardStorageService;
import com.dili.card.validator.ConstantValidator;
import com.dili.ss.domain.BaseOutput;
import com.dili.ss.domain.PageOutput;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.ui.ModelMap;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * 卡仓库管理
 * @Auther: miaoguoxin
 * @Date: 2020/7/1 16:11
 */
@Controller
@RequestMapping("cardStorage")
public class CardStorageManagementController implements IControllerHandler {

    @Autowired
    private ICardStorageService cardStorageService;

    /**
     * 跳转出库页面
     * @author miaoguoxin
     * @date 2020/7/1
     */
    @GetMapping("outList.html")
    public String outListView() {
        return "cardstorage/outList";
    }

    /**
     * 跳转到出库详情
     * @author miaoguoxin
     * @date 2020/7/2
     */
    @GetMapping("outDetail.html/{id}")
    public String outDetailView(@PathVariable Long id, ModelMap modelMap) {
        modelMap.put("detail", cardStorageService.getById(id));
        return "cardstorage/outDetail";
    }

    /**
    *  跳转到添加页面
    * @author miaoguoxin
    * @date 2020/7/2
    */
    @GetMapping("outAdd.html")
    public String outAddView(){
        return "cardstorage/outAdd";
    }

    /**
     * 列表分页
     * @author miaoguoxin
     * @date 2020/7/1
     */
    @PostMapping("outPage.action")
    @ResponseBody
    public PageOutput<List<ApplyRecordResponseDto>> getPage(@RequestBody @Validated(ConstantValidator.Page.class)
                                                                    ApplyRecordQueryDto queryDto) {
        this.buildOperatorInfo(queryDto);
        return cardStorageService.getPage(queryDto);
    }

    /**
    * 添加出库记录
    * @author miaoguoxin
    * @date 2020/7/3
    */
    @PostMapping("addOut.action")
    public BaseOutput<?> addOutRecord(@RequestBody ApplyRecordRequestDto requestDto){
        this.buildOperatorInfo(requestDto);
        cardStorageService.saveOutRecord(requestDto);
        return BaseOutput.success();
    }

    @Autowired
    private PlatformTransactionManager transactionManager;
    /**
    *
    * @author miaoguoxin
    * @date 2020/7/3
    */
    @PostMapping("test.action")
    @ResponseBody
    public BaseOutput<?> test(){
        DefaultTransactionDefinition def = new DefaultTransactionDefinition();
        def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW); // 事物隔离级别，开启新事务，这样会比较安全些。
        TransactionStatus status = transactionManager.getTransaction(def);
        try {
            ApplyRecordRequestDto requestDto = new ApplyRecordRequestDto();
            requestDto.setAmount(2);
            requestDto.setCardNos("1233,44444");
            requestDto.setOpId(1L);
            requestDto.setOpName("test");
            requestDto.setFirmId(1L);
            requestDto.setFirmName("测试市场");
            requestDto.setApplyUserCode("1234");
            requestDto.setApplyUserId(1L);
            requestDto.setApplyUserName("ttttt");
            cardStorageService.saveOutRecord(requestDto);
            transactionManager.commit(status);
            return BaseOutput.success();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(status.isRollbackOnly());
            transactionManager.rollback(status);
            return BaseOutput.failure();
        }
    }
}
