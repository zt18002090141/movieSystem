package com.system.config;
import com.google.code.kaptcha.impl.DefaultKaptcha;
import com.google.code.kaptcha.util.Config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Properties;

@Configuration
public class KaptchaConfig {
    //用来产生验证码的方法
    @Bean
    public DefaultKaptcha producer(){
        Properties properties = new Properties();
        properties.put("kaptcha.border","no");
        properties.put("kaptcha.textproducer.font.color","38,113,70");
        properties.put("kaptcha.textproducer.char.space","4");
        properties.put("kaptcha.textproducer.font.size","30");
        properties.put("kaptcha.image.height","40");
        properties.put("kaptcha.image.width","120");
        Config config = new Config(properties);
        DefaultKaptcha defaultKaptcha = new DefaultKaptcha();
        defaultKaptcha.setConfig( config);
        return defaultKaptcha;
    }
}
