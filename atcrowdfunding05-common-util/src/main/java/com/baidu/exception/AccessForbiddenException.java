package com.baidu.exception;

/**
 * 表示用户没有登录就访问保护资源时的异常
 * @Author Administrator
 * @create 2020/7/16 0016 10:52
 */
public class AccessForbiddenException extends RuntimeException{
    private static final long serialVersionUID = 1L;

    public AccessForbiddenException() {
    }

    public AccessForbiddenException(String message) {
        super(message);
    }

    public AccessForbiddenException(String message, Throwable cause) {
        super(message, cause);
    }

    public AccessForbiddenException(Throwable cause) {
        super(cause);
    }

    public AccessForbiddenException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
