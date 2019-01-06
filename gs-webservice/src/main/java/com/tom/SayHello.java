package com.tom;

import javax.jws.WebMethod;
import javax.jws.WebService;

/**
 * ＳＥＩ和ＳＥ的实现类
 */
@WebService
public interface SayHello {
    /**
     * ＳＥＩ中的方法
     *
     * @param name
     */
    @WebMethod
    String sayHello(String name);
}
