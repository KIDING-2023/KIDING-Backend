package com.demo.KIDING.global.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
public class CorsConfig {

    @Bean
    public CorsFilter corsFilter(){
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);  // 내서버가 응답할때 json을 js에서 처리할 수 있게 설정
        config.addAllowedOrigin("http://localhost:3000");  // 모든 ip의 응답을 허용
        config.addAllowedHeader("*");  // 모든 header의 응답을 허용
        config.addAllowedMethod("*");  // 모든 post, get, delete, patch요청을 허용하겠다

        config.addExposedHeader("Authorization");   // react header 참조
        source.registerCorsConfiguration("/api/**", config);  // "/api/**"로 오는 모든 설정은 config설정을 따른다
        return new CorsFilter(source);
//        return new CorsFilter((CorsConfigurationSource) source);

    }
}