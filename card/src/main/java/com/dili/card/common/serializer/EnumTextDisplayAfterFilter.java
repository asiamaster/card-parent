package com.dili.card.common.serializer;

import com.alibaba.fastjson.serializer.AfterFilter;
import com.dili.card.common.annotation.TextDisplay;
import com.dili.ss.metadata.ValueProvider;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Auther: miaoguoxin
 * @Date: 2020/6/24 11:05
 * @Description: 用于前端展示枚举相关文本
 */
public class EnumTextDisplayAfterFilter extends AfterFilter {
    protected static Logger LOGGER = LoggerFactory.getLogger(EnumTextDisplayAfterFilter.class);
    /**表示这个包下面的类才有效*/
    private static final String NEED_SCAN_PACKAGE = "com.dili.card.dto";

    private static final String DISPLAY_SUFFIX = "Text";
    private static final Map<Class<? extends ValueProvider>, ValueProvider> CACHE = new ConcurrentHashMap<>();

    @Override
    public void writeAfter(Object object) {
        String packageName = object.getClass().getPackageName();
        if (!NEED_SCAN_PACKAGE.equalsIgnoreCase(packageName)) {
            return;
        }
        Field[] declaredFields = object.getClass().getDeclaredFields();
        for (Field declaredField : declaredFields) {
            declaredField.setAccessible(true);
            TextDisplay textDisplay = declaredField.getAnnotation(TextDisplay.class);
            if (textDisplay == null) {
                continue;
            }
            try {
                Class<? extends ValueProvider> providerClazz = textDisplay.value();
                //有就从缓存里面取，提升一点效率
                ValueProvider providerBean = this.getProvider(providerClazz);
                String displayText = providerBean.getDisplayText(declaredField.get(object), null, null);
                if (StringUtils.isNoneBlank(displayText)){
                    //构造一个新的key value
                    super.writeKeyValue(declaredField.getName() + DISPLAY_SUFFIX, displayText);
                }
            } catch (Exception e) {
                LOGGER.error("反射获取字段失败:", e);
            }
        }
    }

    private ValueProvider getProvider(Class<? extends ValueProvider> providerClazz) throws InstantiationException, IllegalAccessException, java.lang.reflect.InvocationTargetException, NoSuchMethodException {
        ValueProvider providerBean = CACHE.get(providerClazz);
        if (providerBean == null) {
            synchronized (CACHE) {
                //double check
                providerBean = CACHE.get(providerClazz);
                if (providerBean == null) {
                    providerBean = providerClazz.getConstructor().newInstance();
                    CACHE.putIfAbsent(providerClazz, providerBean);
                }
            }
        }
        return providerBean;
    }
}
