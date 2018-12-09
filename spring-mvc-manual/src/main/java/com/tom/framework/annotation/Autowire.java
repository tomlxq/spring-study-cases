package com.tom.framework.annotation;

import java.lang.annotation.*;

//作用在类上的
@Target(ElementType.FIELD)
//运行时启用　
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Autowire {
    String value() default "";
}
