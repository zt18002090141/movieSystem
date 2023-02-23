package com.system.security;

import cn.hutool.json.JSONUtil;
import com.system.common.Result;
import com.system.service.UserService;
import com.system.util.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

//认证成功的处理器：登录成功之后，需要返回登录成功的用户相关信息到Vue前端
@Component
public class LoginSuccessHandle implements AuthenticationSuccessHandler {

    @Autowired
    private JwtUtils jwtUtils;
    @Autowired
    private UserService userService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
//      设置返回客户端的 data 的类型
        response.setContentType("application/json;charset=utf-8");
        ServletOutputStream servletOutputStream= response.getOutputStream();
//      先获得Jwt(token)
//      Authentication 是SpringSecurity封装用户登录信息，getName()获得登录输入的用户名
        String username=authentication.getName();
        String jwt=jwtUtils.generateToken(username);
//      响应头中设置返回的token
        response.setHeader(jwtUtils.getHeader(),jwt);
//      返回的是查询出的user对象
        Result result=Result.success(null);
//      将result对象转换为JSON并返沪Vue前端
        servletOutputStream.write(JSONUtil.toJsonStr(result).getBytes("UTF-8"));
        servletOutputStream.flush();
        servletOutputStream.close();

    }
}
