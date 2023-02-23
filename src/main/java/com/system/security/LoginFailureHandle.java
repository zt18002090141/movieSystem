package com.system.security;

import cn.hutool.json.JSONUtil;
import com.system.common.Result;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
//认证失败（1.验证码验证失败2.用户名密码错误）将认证失败的信息结果封装Result,转换为JSON发送给Vue前端
public class LoginFailureHandle implements AuthenticationFailureHandler {
    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        response.setContentType("application/json;charset=utf-8");
        ServletOutputStream servletOutputStream=response.getOutputStream();
        Result result=Result.fail( exception.getMessage().equals("Bad credentials")?"用户名或密码错误":exception.getMessage());
        servletOutputStream.write(JSONUtil.toJsonStr(result).getBytes("UTF-8"));
        servletOutputStream.flush();
        servletOutputStream.close();
    }
}
