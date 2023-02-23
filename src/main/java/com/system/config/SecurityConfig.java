package com.system.config;
import com.system.security.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

//SpringSecurity核心配置类，所有定义过滤器、处理器都是在这里配置执行。
@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)   //方法级别的权限认证
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    //用来加密密码的对象
    @Bean
    BCryptPasswordEncoder bCryptPasswordEncoder(){
        return new BCryptPasswordEncoder();
    }
    @Bean
    JwtAuthenticationFilter jwtAuthenticationFilter() throws Exception{
        JwtAuthenticationFilter jwtAuthenticationFilter = new JwtAuthenticationFilter(authenticationManager());
        return jwtAuthenticationFilter;
    }

    //声明需要配置过滤器和处理器对象
    @Autowired
    CaptchaFilter captchaFilter;
    @Autowired
    LoginSuccessHandle loginSuccessHandler;
    @Autowired
    LoginFailureHandle loginFailureHandler;
    @Autowired
    UserDetailsServiceImpl userDetailsService;
    @Autowired
    JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;   //jwt验证失败的处理器
    @Autowired
    JwtAccessDeniedHandle jwtAccessDeniedHandler;  //权限不够异常的处理器
    @Autowired
    LogoutSuccessHandler logoutSuccessHandler;  //登出成功执行的处理器

    //白名单
    public static final String[] URL_WHITE_LIST = {
            "/login",
            "/logout",
            "/captcha",
            "/upload/**"
    };

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        //关闭跨域和CSRF防护
        http.cors().and().csrf().disable()
                //登录配置
                .formLogin()
                .failureHandler(loginFailureHandler) //配置登录失败的处理器对象
                .successHandler(loginSuccessHandler) //配置登录成功的处理器对象

                //登出操作
                .and()
                .logout()
                .logoutSuccessHandler(logoutSuccessHandler)

                //配置拦截规则
                .and()
                .authorizeRequests()  //对所有的请求URL进行权限验证，
                .antMatchers(URL_WHITE_LIST)  //请求放行的规则设置，采用白名单
                .permitAll()  //对所有人应用这些规则
                .anyRequest() //表示匹配任意URL请求
                .authenticated()   //表示所有匹配到的URL需要被认证才能访问

                //禁用session
                /*
                SessionCreationPolicy.STATELESS ————永远不会创建Session
                SessionCreationPolicy.ALWAYS————总是创建Session
                SessionCreationPolicy.IF_REQUIRED————SC只会在需要时创建一个session
                SessionCreationPolicy.NEVER————不会创建session，但是如果session已经存在，那么可以使用
                **/
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)

                //配置异常的处理器
                .and()
                .exceptionHandling()
                .authenticationEntryPoint(jwtAuthenticationEntryPoint) //SC捕获AuthenticationException，使用对应的jwtAuthenticationEntryPoint的commence()方法处理。
                .accessDeniedHandler(jwtAccessDeniedHandler)

                //配置自定义的过滤器
                .and()
                .addFilter(jwtAuthenticationFilter())  //配置验证Jwt的过滤器
                .addFilterBefore(captchaFilter, UsernamePasswordAuthenticationFilter.class);
        //captchaFilter 验证码过滤器必须在 UsernamePasswordAuthenticationFilter SC验证登录密码之前执行
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService);
    }
}
