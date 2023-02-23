package com.system.security;


import cn.hutool.json.JSONUtil;
import com.system.common.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@Slf4j
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        log.info("jwt认证失败，未登录");
        response.setContentType("application/json;charset=utf=8");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        ServletOutputStream servletOutputStream=response.getOutputStream();
        Result result=Result.fail("请先登录");
//      将result对象转换为JSON并返沪Vue前端
        servletOutputStream.write(JSONUtil.toJsonStr(result).getBytes("UTF-8"));
        servletOutputStream.flush();
        servletOutputStream.close();
    }
}
