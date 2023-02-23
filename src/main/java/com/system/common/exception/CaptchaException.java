package com.system.common.exception;


import org.springframework.security.core.AuthenticationException;
//自定义异常类：表示验证码失败的异常
public class CaptchaException extends AuthenticationException {

    public CaptchaException(String detail) {
        super(detail);
    }

}
