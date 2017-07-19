package org.smart4j.framework.helper;

import or.smert4j.framework.annotation.Inject;
import org.smart4j.framework.util.ArrayUtil;
import org.smart4j.framework.util.PropsUtil.CollectionUtil;

import java.lang.reflect.Field;
import java.util.Map;

/**
 * Created by yz on 2017/7/19.
 */
public final class IocHelper {

    static {
        Map<Class<?>, Object> beanMap = BeanHelper.getBeanMap();
        if (CollectionUtil.isNotEmpty(beanMap)){
            for (Map.Entry<Class<?>, Object> beanEntry : beanMap.entrySet()){
                Class<?> beanClass = beanEntry.getKey();
                Field[] beanFields = beanClass.getDeclaredFields();
                if (ArrayUtil.isNotEmpty(beanFields)){
                    for (Field beanField : beanFields){
                        if (beanField.isAnnotationPresent(Inject.class)) {
                            Class<?> beanFieldClass = beanField.getType();
                            Object beanFieldInstance = beanMap.get(beanFieldClass);
                            if (beanFieldInstance != null){
                                ReflectionUtil.setField(beanFieldInstance, beanField, beanFieldInstance);
                            }
                        }
                    }
                }
            }
        }
    }


}
