package com.dili.card.util;

import cn.hutool.core.map.MapUtil;
import cn.hutool.core.math.MathUtil;
import cn.hutool.core.math.Money;
import com.dili.ss.constant.ResultCode;
import com.dili.ss.domain.PageOutput;
import com.github.pagehelper.Page;
import org.springframework.beans.BeanUtils;

import java.util.Collection;

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
        pageOutput.setTotal((int) page.getTotal());
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
