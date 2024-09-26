package com.ureca.compliment.util.auth.service;

import com.ureca.compliment.user.User;
import com.ureca.compliment.user.dao.UserDAO;
import com.ureca.compliment.user.exceptions.UserNotFoundException;
import com.ureca.compliment.util.auth.JwtUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Map;

@Component
public class OAuth2AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    private final JwtUtil jwtUtil;
    private final UserDAO userDAO;


    public OAuth2AuthenticationSuccessHandler(JwtUtil jwtUtil, UserDAO userDAO) {
        this.jwtUtil = jwtUtil;
        this.userDAO = userDAO;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

        System.out.println("🚨");

        OAuth2AuthenticationToken authToken = (OAuth2AuthenticationToken) authentication;
        Map<String, Object> userAttributes = authToken.getPrincipal().getAttributes();

        // 사용자 정보를 DB에 저장
        String slackId = (String) userAttributes.get("id");
        String email = (String) userAttributes.get("email");
        String name = (String) userAttributes.get("name");

        User user = null;
        try {
            user = userDAO.findBySlackId(slackId).orElse(new User(
                    (String) userAttributes.get("name")
            ));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (UserNotFoundException e) {
            throw new RuntimeException(e);
        }

        user.setSlackId(slackId);
        user.setName(name);
        try {
            userDAO.save(user);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        // JWT 토큰 생성 및 응답
        String token = jwtUtil.generateToken(user.getId());
        // JWT 토큰을 포함하여 프론트엔드로 리다이렉트
        String redirectUrl = "http://127.0.0.1:3000/home?token=" + token;
        getRedirectStrategy().sendRedirect(request, response, redirectUrl);
    }
}
