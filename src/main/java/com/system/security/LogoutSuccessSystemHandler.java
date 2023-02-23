package com.system.security;

import cn.hutool.json.JSONUtil;
import com.system.common.Result;
import com.system.util.JwtUtils;
import com.system.util.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class LogoutSuccessSystemHandler implements LogoutSuccessHandler {
   @Autowired
    JwtUtils jwtUtils;
    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
       if(authentication!=null){
           new SecurityContextLogoutHandler().logout(request,response,authentication);
       }

        response.setContentType("application/json;charset=utf-8");
       // response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setHeader(jwtUtils.getHeader(),"");
        ServletOutputStream servletOutputStream=response.getOutputStream();
        Result result=Result.success("");
        servletOutputStream.write(JSONUtil.toJsonStr(result).getBytes("utf-8"));
        servletOutputStream.flush();
        servletOutputStream.close();
    }
}
