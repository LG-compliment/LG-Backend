package com.ureca.compliment.util.auth;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.List;

@Configuration
public class CorsConfig {

    /**
     * CORS 설정을 정의하는 CorsFilter를 구성합니다.
     * - 허용할 출처(Allowed Origins) 설정
     * - 허용할 HTTP 메서드(Allowed Methods) 설정
     * - 허용할 헤더(Allowed Headers) 설정
     * - 기타 CORS 관련 설정도 추가 가능 (예: 허용할 자격 증명, 캐시 시간 등)
     *
     * @return 전역적으로 CORS 규칙을 적용하는 CorsFilter 객체를 반환합니다.
     */
    @Bean
    public CorsFilter corsFilter() {
        // CORS 설정을 담을 객체를 생성
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(List.of("http://localhost:3000"));  // 허용할 출처 (여러 출처도 가능)
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE"));  // 허용할 HTTP 메서드
        config.setAllowedHeaders(List.of("Authorization", "Content-Type"));  // 허용할 헤더
        config.setAllowCredentials(true);  // 자격 증명 (예: 쿠키) 허용 여부
        config.setMaxAge(3600L);  // 캐시 시간 (초 단위로 설정)

        // 특정 URL 패턴에 대한 CORS 설정을 매핑할 수 있는 소스 객체 생성
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);  // 모든 URL에 대해 CORS 설정 적용

        // CorsFilter 객체를 생성하여 반환
        return new CorsFilter(source);
    }
}