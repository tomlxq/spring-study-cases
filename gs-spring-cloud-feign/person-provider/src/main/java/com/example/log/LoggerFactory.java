package com.example.log;

import java.util.HashMap;
import java.util.Map;

/**
 * 功能描述
 *
 * @author TomLuo
 * @date 2019/7/5
 */

public class LoggerFactory {

    private static Object initLock = new Object();
    private static Map<String, CASLogger> loggerMap = new HashMap<String, CASLogger>();

    /**
     * 功能描述: 获取Logger
     * 
     * @param name
     *            String
     * @return CASLogger CASLogger Date: 2018-8-17
     */
    public static CASLogger getLogger(String name) {
        CASLogger casLogger = null;
        synchronized (initLock) {
            casLogger = loggerMap.get(name);
            if (casLogger == null) {
                casLogger = new CASLogger(name);
                loggerMap.put(name, casLogger);
            }
        }
        return casLogger;
    }

    /**
     * 功能描述: 获取Logger
     * 
     * @param clazz
     *            String
     * @return CASLogger CASLogger Date: 2018-8-17
     */
    public static CASLogger getLogger(Class<?> clazz) {
        String name = clazz.getName();
        CASLogger casLogger = null;
        synchronized (initLock) {
            casLogger = loggerMap.get(name);
            if (casLogger == null) {
                casLogger = new CASLogger(name);
                loggerMap.put(name, casLogger);
            }
        }
        return casLogger;
    }
}