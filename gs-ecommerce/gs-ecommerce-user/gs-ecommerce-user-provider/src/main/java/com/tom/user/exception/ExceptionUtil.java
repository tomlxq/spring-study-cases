
package com.tom.user.exception;


import com.tom.user.constants.ResponseCodeEnum;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ExceptionUtil {

    //private static Logger logger = LoggerFactory.getLogger(ExceptionUtil.class);

    /**
     * 将下层抛出的异常转换为resp返回码
     *
     * @param e Exception
     * @return
     */
    public static Exception handlerException4biz(Exception e) {
        Exception ex = null;
        if (!(e instanceof Exception)) {
            return null;
        }
        if (e instanceof ValidateException) {
            ex = new ServiceException(((ValidateException) e).getErrorCode(), ((ValidateException) e).getErrorMessage());
        } else if (e instanceof Exception) {
            ex = new ServiceException(ResponseCodeEnum.SYSTEM_BUSY.getCode(),
                    ResponseCodeEnum.SYSTEM_BUSY.getMsg());
        }
        LOGGER.error("ExceptionUtil.handlerException4biz,Exception=" + e.getMessage(), e);
        return ex;
    }
}
