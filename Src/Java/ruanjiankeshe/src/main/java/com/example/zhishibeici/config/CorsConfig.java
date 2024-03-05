package com.example.zhishibeici.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

//跨域配置
//http请求到达时序为：filter，servlet，拦截器
@Configuration
public class CorsConfig {
    @Bean
    public CorsFilter corsFilter() {
        CorsConfiguration config = new CorsConfiguration();
        config.addAllowedOrigin("*");           //允许任何域发送跨域请求
        config.setAllowCredentials(true);       //允许cookie
        config.addAllowedMethod("*");           //允许任何方法发送跨域请求
        config.addAllowedHeader("*");           //允许跨域请求包含任何header
        UrlBasedCorsConfigurationSource configSource = new UrlBasedCorsConfigurationSource();
        //添加映射路径，此时拦截一切请求
        configSource.registerCorsConfiguration("/**", config);
        return new CorsFilter(configSource);
    }
}
