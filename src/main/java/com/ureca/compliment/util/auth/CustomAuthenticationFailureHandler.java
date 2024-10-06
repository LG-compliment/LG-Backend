package com.ureca.compliment.util.auth;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
    public class CustomAuthenticationFailureHandler implements AuthenticationFailureHandler {

        private static final Logger logger = LoggerFactory.getLogger(CustomAuthenticationFailureHandler.class);

        @Override
        public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
                                            AuthenticationException exception) throws IOException, ServletException {
            if (exception instanceof OAuth2AuthenticationException) {
                OAuth2AuthenticationException oauth2AuthenticationException = (OAuth2AuthenticationException) exception;
                OAuth2Error error = oauth2AuthenticationException.getError();
                logger.error("OAuth2 authentication error: {}", error);
                // 여기에 추가적인 오류 정보 로깅
            }
            // 오류 응답 처리
        }
    }