package com.ureca.compliment.util.auth;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.oauth2.client.web.AuthorizationRequestRepository;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;
import org.springframework.stereotype.Component;


@Component
public class CustomAuthorizationRequestRepository implements AuthorizationRequestRepository<OAuth2AuthorizationRequest> {

    @Override
    public OAuth2AuthorizationRequest loadAuthorizationRequest(HttpServletRequest request) {
        // 항상 고정된 AuthorizationRequest를 반환
        return OAuth2AuthorizationRequest.authorizationCode()
                .authorizationUri("https://slack.com/openid/connect/authorize")
                .clientId("7102032032211.7802787937170")
                .redirectUri("{baseUrl}/api/oauth2/callback/{registrationId}")
                .state("123") // state를 고정된 값으로 설정
                .build();
    }

    @Override
    public void saveAuthorizationRequest(OAuth2AuthorizationRequest authorizationRequest, HttpServletRequest request, HttpServletResponse response) {
        // 실제로 AuthorizationRequest를 저장하지 않음
    }

    @Override
    public OAuth2AuthorizationRequest removeAuthorizationRequest(HttpServletRequest request, HttpServletResponse response) {
        return null;
    }
}
