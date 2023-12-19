package com.config;

import org.springframework.web.filter.CorsFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

/**
 * @Description: TODO
 * @author: BlackWarm
 * @date: 2023年 09月 19日  16:21
 */
@Configuration
public class CorsConfig {

    @Bean
    public CorsFilter corsFilter(){

        CorsConfiguration corsConfiguration = new CorsConfiguration();
//        * 允许所有地址跨域
//        corsConfiguration.addAllowedOrigin("*");
        // 允许指定地址跨域
        corsConfiguration.addAllowedOrigin("http://localhost:8888");
        // 允许 cookie 使用
        corsConfiguration.setAllowCredentials(true);
        // 允许特点请求的调用,如 post, get 等, * 允许所有请求通过
        corsConfiguration.addAllowedMethod("*");
        // 允许请求头的信息  允许所有头信息
        corsConfiguration.addAllowedHeader("*");

        // 设置拦截器
        UrlBasedCorsConfigurationSource urlBasedCorsConfigurationSource = new UrlBasedCorsConfigurationSource();
        // 注册拦截器,前一个是拦截的地址,后一个是放行的地址
        urlBasedCorsConfigurationSource.registerCorsConfiguration("/**",corsConfiguration);

        return new CorsFilter(urlBasedCorsConfigurationSource);
    }
}
