package com.tom.web.interceptor;

/**
 * User: TOM
 * Date: 2015/1/17
 * email: beauty9235@gmail.com
 * Time: 15:27
 */

import java.lang.annotation.*;

@Target(ElementType.METHOD)   //创建作用于Method级别的Annotation类，用于传入功能ID
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface FireAuthority {
    /**
     * accessRightId 权限检查点
     * @return
     */
    String accessRightId();
    /**
     * resultType 返回页面类型,用于展示错误时的返回的错误视图模型
     * @return
     */
    ResultTypeEnum resultType() default ResultTypeEnum.page;
}
