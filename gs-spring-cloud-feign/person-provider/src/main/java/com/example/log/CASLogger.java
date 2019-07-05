package com.example.log;

import org.slf4j.Logger;

/**
 * 功能描述
 *
 * @author TomLuo
 * @date 2019/7/5
 */
public class CASLogger {

    /** LOGINDEVICE */
    private static final String LOGINDEVICE = "loginDevice";
    /** slf4j */
    private Logger log;

    /**
     * 调用类名
     * 
     * @param name
     *            String
     */
    public CASLogger(String name) {
        log = org.slf4j.LoggerFactory.getLogger(name);

    }

    /**
     * 此处用于重写需要用的的log方法
     */

}
