package com.dili.card.service;

import com.alibaba.fastjson.JSON;
import com.dili.card.BaseTest;
import com.dili.card.dto.CardStorageOutQueryDto;
import com.dili.card.dto.CardStorageOutRequestDto;
import com.dili.card.dto.CardStorageOutResponseDto;
import com.dili.ss.domain.PageOutput;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @Auther: miaoguoxin
 * @Date: 2020/7/3 10:03
 * @Description:
 */
class CardStorageServiceTest extends BaseTest {
    @SpyBean
    private ICardStorageService cardStorageService;

    @Test
    @Transactional
    @Rollback
    void testSaveOutRecord() {
        CardStorageOutRequestDto requestDto = new CardStorageOutRequestDto();
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
    }

    @Test
    void testGetPage() {
        CardStorageOutQueryDto recordRequestDto = new CardStorageOutQueryDto();
        recordRequestDto.setFirmId(1L);
        recordRequestDto.setPage(1);
        recordRequestDto.setRows(10);
        PageOutput<List<CardStorageOutResponseDto>> page = cardStorageService.getPage(recordRequestDto);
        LOGGER.info("获取到结果:{}", JSON.toJSONString(page));
    }
}
