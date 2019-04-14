package com.tom.ecommerce.user.exception;

import com.tom.ecommerce.user.constants.ResponseCodeEnum;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ValidateException extends RuntimeException {


    private static final long serialVersionUID = 7310190125507751330L;
    /**
     * 返回码
     */
    private String errorCode;
    /**
     * 信息
     */
    private String errorMessage;

    /**
     * 构造函数
     */
    public ValidateException() {
        super();
    }

    /**
     * 构造函数
     *
     * @param errorCode
     */
    public ValidateException(String errorCode) {
        super(errorCode);
        this.errorCode = ResponseCodeEnum.SYS_PARAM_NOT_RIGHT.getCode();
        this.errorMessage = ResponseCodeEnum.STATUS_NOT_RIGHT.getMsg(errorCode);
    }

    /**
     * 构造函数
     *
     * @param cause
     */
    public ValidateException(Throwable cause) {
        super(cause);
    }

    /**
     * 构造函数
     *
     * @param errorCode
     * @param cause
     */
    public ValidateException(String errorCode, Throwable cause) {
        super(cause);
        this.errorCode = errorCode;
    }

    /**
     * 构造函数
     *
     * @param errorCode
     * @param message
     */
    public ValidateException(String errorCode, String message) {
        super();
        this.errorCode = errorCode;
        this.errorMessage = message;
    }

    /**
     * 构造函数
     *
     * @param errorCode
     * @param message
     * @param cause
     */
    public ValidateException(String errorCode, String message, Throwable cause) {
        super(cause);
        this.errorCode = errorCode;
        this.errorMessage = message;
    }

}
