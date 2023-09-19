package com.example.Administrator.common;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

//relationship between JWTInterceptor and InterceptorConfig
@Configuration
public class InterceptorConfig extends WebMvcConfigurationSupport {
    protected void addInterceptors(InterceptorRegistry registry) {
        // /** means block all url paths
        // configure jwt interceptor rules - addInterceptor(jwtInterceptor())
        registry.addInterceptor(jwtInterceptor()).addPathPatterns("/**")
//        so far, all path has been blocked, so we need configure something becoming block all paths except login
        .excludePathPatterns("/login","/Register");
        super.addInterceptors(registry);
    }

    @Bean
    public JwtInterceptor jwtInterceptor() {
        return new JwtInterceptor();
    }
}
