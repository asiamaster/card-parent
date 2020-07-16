package com.dili.tcc.core;

import com.dili.tcc.core.TccContext;

/**
 * @Auther: miaoguoxin
 * @Date: 2020/7/7 11:06
 * @Description:
 */
public class TccContextHolder {

    private static final ThreadLocal<TccContext> LOCAL = new ThreadLocal<>();

    /**
     *  静默获取方式
     * @author miaoguoxin
     * @date 2020/7/14
     */
    public static TccContext get() {
        return LOCAL.get();
    }

    public static void set(TccContext tccContext) {
        TccContext exist = LOCAL.get();
        if (exist != null){
            throw new RuntimeException("can not set tccContext,because the one has exist");
        }
        LOCAL.set(tccContext);
    }

    /**
     *  清除
     * @author miaoguoxin
     * @date 2020/7/14
     */
    public static void remove() {
        LOCAL.remove();
    }
}
