package com.ureca.compliment.util.auth;

import com.ureca.compliment.user.dao.UserDAOImpl;
import com.ureca.compliment.util.auth.service.OAuth2AuthenticationSuccessHandler;
import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * SecurityConfig는 Spring Security의 전반적인 설정을 담당합니다:
 * HTTP 보안 설정 (어떤 URL에 인증이 필요한지 등)
 * JwtAuthenticationFilter를 필터 체인에 추가
 */

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthFilter;
    private final OAuth2AuthenticationSuccessHandler successHandler;

    @Autowired
    private Dotenv dotenv;

    public SecurityConfig(JwtAuthenticationFilter jwtAuthFilter, OAuth2AuthenticationSuccessHandler successHandler) {
        this.jwtAuthFilter = jwtAuthFilter;
        this.successHandler = successHandler;
    }

    /**
     * @param http HttpSecurity 객체로 보안 규칙을 설정합니다.
     * SecurityFilterChain은 HTTP 요청에 대한 보안 필터 체인을 구성합니다.
     * - csrf(AbstractHttpConfigurer::disable): CSRF 보호를 비활성화합니다. 일반적으로 REST API에서는 비활성화합니다.
     * - authorizeHttpRequests: 각 요청에 대해 인증 규칙을 정의합니다.
     *   - "/users/login" 경로는 모든 사용자에게 열려 있습니다 (permitAll).
     *   - 그 외의 모든 요청(anyRequest)은 인증이 필요합니다 (authenticated).
     * - sessionManagement: 세션 관리를 설정합니다.
     *   - SessionCreationPolicy.STATELESS: 서버가 세션을 생성하지 않도록 설정합니다. 즉, 상태가 없는(Stateless) JWT 인증 방식을 사용합니다.
     * - addFilterBefore: jwtAuthFilter를 UsernamePasswordAuthenticationFilter 앞에 추가하여 JWT를 통한 인증을 처리합니다.
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/users/login", "/api/users/sign-up", "/api/users/check-id",  "/api/oauth2/**", "/api/login/**").permitAll()
                        .anyRequest().authenticated()
                )
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.ALWAYS)
                )
                .oauth2Login(oauth2 -> oauth2
                        .loginPage("/api/oauth2/authorization/slack")
                        .successHandler(successHandler)
                        .failureHandler((request, response, exception) -> {
                            // 로그를 추가하여 실패 원인을 파악
                            exception.printStackTrace();
                            response.sendRedirect(dotenv.get("FRONT_PAGE_URL") + "/login");
                        })
                        .authorizationEndpoint(authorizationEndpoint ->
                                authorizationEndpoint.baseUri("/api/oauth2/authorization")
                        )
                        .redirectionEndpoint(redirectionEndpoint ->
                                redirectionEndpoint.baseUri("/api/login/oauth2/code/*")
                        ).userInfoEndpoint(userInfo -> userInfo
                                .userService(this.oauth2UserService())
                        )
                )
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                .cors(Customizer.withDefaults());
        return http.build();
    }

    @Bean
    public OAuth2UserService<OAuth2UserRequest, OAuth2User> oauth2UserService() {
        DefaultOAuth2UserService delegate = new DefaultOAuth2UserService();
        return (userRequest) -> {
            OAuth2User oauth2User = delegate.loadUser(userRequest);

            System.out.println("😍");
            System.out.println(oauth2User);

            // Slack에서 받은 사용자 정보 처리 로직
            return oauth2User;
        };
    }

    /**
     * AuthenticationManager는 인증을 처리하는 핵심 컴포넌트입니다.
     * - AuthenticationManager를 통해 사용자의 인증 정보를 검증합니다.
     * - AuthenticationConfiguration를 사용해 Spring Security가 자동으로 구성한 AuthenticationManager를 가져옵니다.
     *
     * @param authenticationConfiguration 인증 관리를 위한 설정을 제공합니다.
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    /**
     * PasswordEncoder는 비밀번호를 해싱하고 검증하는 데 사용됩니다.
     * - BCryptPasswordEncoder는 BCrypt 알고리즘을 사용하여 비밀번호를 해싱합니다.
     * - BCrypt는 강력한 보안성을 제공하며, 해시된 비밀번호를 안전하게 저장할 수 있습니다.
     *
     * @return BCryptPasswordEncoder 객체를 반환하여 비밀번호 해싱 기능을 제공합니다.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();  // BCrypt 해시 알고리즘 사용
    }
}