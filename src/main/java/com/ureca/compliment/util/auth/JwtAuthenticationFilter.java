package com.ureca.compliment.util.auth;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * JwtAuthenticationFilter는 JWT를 사용한 인증을 처리하는 필터입니다.
 * - 각 요청마다 JWT 토큰을 검사하고, 유효하면 해당 사용자 정보를 SecurityContext에 설정합니다.
 * - OncePerRequestFilter를 상속받아 각 요청당 한 번만 필터링됩니다.
 */
@Component  // 스프링 컨텍스트에 빈으로 등록
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final UserDetailsService userDetailsService;

    // JwtUtil과 UserDetailsService는 생성자 주입을 통해 받아옵니다.
    public JwtAuthenticationFilter(JwtUtil jwtUtil, UserDetailsService userDetailsService) {
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
    }

    /**
     * doFilterInternal은 각 요청마다 JWT 토큰을 확인하고 유효성을 검증합니다.
     * - Authorization 헤더에서 JWT를 추출합니다.
     * - 추출한 JWT에서 사용자 이름을 추출하고, 토큰이 유효하면 SecurityContext에 인증 정보를 설정합니다.
     *
     * @param request HttpServletRequest 객체로 클라이언트 요청 정보를 담고 있습니다.
     * @param response HttpServletResponse 객체로 서버 응답을 담고 있습니다.
     * @param filterChain FilterChain을 통해 다음 필터를 호출합니다.
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        // 요청 헤더에서 Authorization 값을 가져옵니다.
        final String authorizationHeader = request.getHeader("Authorization");

        String username = null;
        String jwt = null;

        // Authorization 헤더가 존재하고, Bearer 토큰이 포함된 경우
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            // Bearer 접두사를 제거하고 JWT만 추출
            jwt = authorizationHeader.substring(7);
            // JWT에서 사용자 이름을 추출
            username = jwtUtil.extractUsername(jwt);
        }

        // 사용자 이름이 존재하고, 현재 요청이 이미 인증된 상태가 아닌 경우
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            // 사용자 이름을 기반으로 사용자 정보를 로드
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);

            // JWT가 유효한지 확인
            if (jwtUtil.validateToken(jwt, userDetails.getUsername())) {
                // 사용자가 유효한 경우 UsernamePasswordAuthenticationToken을 생성하여 인증 설정
                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities()
                );
                // 요청에 대한 추가적인 세부 정보를 설정
                authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                // 인증 정보를 SecurityContext에 설정
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            }
        }
        // 필터 체인의 다음 필터를 호출하여 필터링 작업을 이어갑니다.
        filterChain.doFilter(request, response);
    }
}