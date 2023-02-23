package com.system.security;


import cn.hutool.core.util.StrUtil;
import com.system.entity.User;
import com.system.service.UserService;
import com.system.util.JwtUtils;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextChangedEvent;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.TreeSet;

@Slf4j
public class JwtAuthenticationFilter extends BasicAuthenticationFilter {
    @Autowired
    JwtUtils jwtUtils;

    @Autowired
   UserDetailsServiceImpl userDetailsService;
    @Autowired
    UserService userService;
    public JwtAuthenticationFilter(AuthenticationManager authenticationManager) {
        super(authenticationManager);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException{
        log.info("jwt验证");
        String jwt=request.getHeader(jwtUtils.getHeader());
        if(StrUtil.isBlankOrUndefined(jwt)){
            chain.doFilter(request,response);
            return;
        }
        Claims claims=jwtUtils.getClaimByToken(jwt);
        if (claims==null){
            throw new JwtException("token已经过期");
        }
        String username=claims.getSubject();
        log.info("用户:{}--正在登录",username);

        User user=userService.getByUsername(username);
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken= new UsernamePasswordAuthenticationToken(username,null,userDetailsService.getUserAuthority(user.getId()));
        SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
        chain.doFilter(request,response);
    }
}
