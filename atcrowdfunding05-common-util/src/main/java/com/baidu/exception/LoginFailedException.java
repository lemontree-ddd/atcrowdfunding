package com.baidu.exception;

/**
 * 登录失败后抛出的异常
 * @Author Administrator
 * @create 2020/7/15 0015 18:09
 */
public class LoginFailedException extends RuntimeException{

    private static final long serialVersionUID = 1L;


    public LoginFailedException() {
    }

    public LoginFailedException(String message) {
        super(message);
    }

    public LoginFailedException(String message, Throwable cause) {
        super(message, cause);
    }

    public LoginFailedException(Throwable cause) {
        super(cause);
    }

    public LoginFailedException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
