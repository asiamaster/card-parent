package com.dili.card.util;

import org.springframework.beans.BeanUtils;

import com.dili.ss.constant.ResultCode;
import com.dili.ss.domain.PageOutput;
import com.github.pagehelper.Page;

/**
 * @Auther: miaoguoxin
 * @Date: 2020/6/19 14:51
 * @Description:
 */
public class PageUtils {

    public static <T> PageOutput<T> convert2PageOutput(Page<?> page, T result) {
        PageOutput<T> pageOutput = new PageOutput<>(ResultCode.OK, "success");
        pageOutput.setPageNum(page.getPageNum());
        pageOutput.setPageSize(page.getPageSize());
        pageOutput.setTotal(page.getTotal());
        pageOutput.setData(result);
        return pageOutput;
    }

    public static <T> PageOutput<T> convert2PageOutput(PageOutput<?> source, T result) {
        PageOutput<T> pageOutput = new PageOutput<>();
        BeanUtils.copyProperties(source, pageOutput);
        pageOutput.setData(result);
        return pageOutput;
    }
}
