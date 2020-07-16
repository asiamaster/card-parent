package com.dili.card.common.tcc;

import com.dili.ss.domain.BaseOutput;
import com.dili.tcc.core.TccResultInterceptor;
import org.springframework.stereotype.Component;

/**
 * @Auther: miaoguoxin
 * @Date: 2020/7/15 11:41
 * @Description:
 */
@Component
public class OutputTccResultInterceptor implements TccResultInterceptor {

    @Override
    public boolean doExtra(Object result) {
        if (result instanceof BaseOutput) {
            BaseOutput<?> output = (BaseOutput<?>) result;
            return output.isSuccess();
        }
        return true;
    }
}
