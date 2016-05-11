package com.tom.web.interceptor;

import java.lang.annotation.*;

/**
 * User: TOM
 * Date: 2015/9/29
 * email: beauty9235@gmail.com
 * Time: 15:29
 */
@Target(ElementType.METHOD)   //创建作用于Method级别的Annotation类，用于传入功能ID
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ClientFireAuthority {
    boolean isLogged();
    /**
     * resultType 返回页面类型,用于展示错误时的返回的错误视图模型
     * @return
     */
    ResultTypeEnum resultType() default ResultTypeEnum.page;
}
