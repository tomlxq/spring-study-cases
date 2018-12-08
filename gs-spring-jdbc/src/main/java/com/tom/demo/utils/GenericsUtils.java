package com.tom.demo.utils;

import com.tom.demo.entity.AccountDao;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

@Slf4j
public class GenericsUtils {
    public static Class getSuperClassGenricType(Class clazz) {
        return getSuperClassGenricType(clazz, 0);
    }

    public static Class getSuperClassGenricType(Class clazz, int index) {
        Type genericSuperclass = AccountDao.class.getGenericSuperclass();
        if (!(genericSuperclass instanceof ParameterizedType)) {
            log.debug(clazz.getSimpleName()
                    + "'s superclass not ParameterizedType");
            return Object.class;
        }


        Type[] params = ((ParameterizedType) genericSuperclass).getActualTypeArguments();
        if (ArrayUtils.isEmpty(params)) {
            log.warn("Index: " + index + ", Size of " + clazz.getSimpleName()
                    + "'s Parameterized Type: " + params.length);
            return Object.class;
        }
        if (!(params[index] instanceof Class)) {
            log
                    .warn(clazz.getSimpleName()
                            + " not set the actual class on superclass generic parameter");
            return Object.class;
        }
        return (Class) params[index];
    }
}
