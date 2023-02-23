package com.system.security;


import com.system.common.exception.CaptchaException;
import com.system.common.lang.Const;
import com.system.util.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

// 验证码认证的过滤器
@Component
@Slf4j
public class CaptchaFilter extends OncePerRequestFilter {

//  定义登录路径 /login
    private final String loginURL="/login";
    @Autowired
    RedisUtil redisUtil;
//  声明一个登录失败处理器
    @Autowired
    LoginFailureHandle loginFailureHandle;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String url=request.getRequestURI();
        if(url.equals(loginURL) && request.getMethod().equals("POST")){
            log.info("获得login请求:"+url);
            try{
                validate(request);
            }catch (CaptchaException e){
                log.info(e.getMessage());
//              执行登录失败的处理器 LoginFailureHandle (输出登录失败的信息到vue)
                loginFailureHandle.onAuthenticationFailure(request,response,e);
            }
        }
//      跳转到后续的过滤器
        filterChain.doFilter(request,response);
    }
//  声明一个验证方法
    private void validate(HttpServletRequest request) throws CaptchaException {
      String key =  request.getParameter("key");
      String code = request.getParameter("captchaCode");

      if(StringUtils.isBlank(key) || StringUtils.isBlank(code)){
//        如果key或者code是空值，则抛出异常
          throw  new CaptchaException("验证码不为空！");
      }
//    redis 存CAPTCHA_KEY Map(uuid,value)
      if(!code.equals(redisUtil.hget(Const.CAPTCHA_KEY,key))){
          throw new CaptchaException("验证码输入不正确");
      }
//    redis 中验证码是一次性的，无论验证码对错，都删除redis数据库中的code
      redisUtil.hdel(Const.CAPTCHA_KEY,key);
    }
}
