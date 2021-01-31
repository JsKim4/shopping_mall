package me.kjs.mall.common.util;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

@Component
public class BeanUtil implements ApplicationContextAware {
    private static ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        BeanUtil.applicationContext = applicationContext;
    }

    public static <T> T getBean(Class<T> cls) {
        return applicationContext.getBean(cls);
    }
}
