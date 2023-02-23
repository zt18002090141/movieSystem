package com.system.security;

import cn.hutool.json.JSONUtil;
import com.system.common.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@Component
public class JwtAccessDeniedHandle implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        response.setContentType("application/json;charset=utf-8");
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        ServletOutputStream servletOutputStream=response.getOutputStream();
        log.info("系统操作权限不足");
        Result result=Result.fail("权限验证异常"+ accessDeniedException.getMessage());
        servletOutputStream.write(JSONUtil.toJsonStr(result).getBytes("UTF-8"));
        servletOutputStream.flush();
        servletOutputStream.close();
    }
}
