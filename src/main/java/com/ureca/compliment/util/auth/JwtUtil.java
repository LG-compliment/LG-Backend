package com.ureca.compliment.util.auth;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.function.Function;

/**
 * JwtUtil 클래스는 JWT(Json Web Token) 관련 작업을 수행하는 유틸리티 클래스입니다.
 * - 토큰 생성, 유효성 검사, 클레임 추출 등의 기능을 제공합니다.
 * - JJWT 라이브러리를 사용하여 JWT 처리
 */

@Component
public class JwtUtil {
    /**
     * 주어진 사용자 이름을 기반으로 JWT 토큰을 생성합니다.
     * - 토큰에는 발행 시각과 만료 시각이 포함되며, 주어진 사용자 이름을 'subject'로 설정합니다.
     * - HMAC SHA-512 알고리즘으로 서명합니다.
     *
     * @param username 토큰에 포함될 사용자 이름
     * @return 생성된 JWT 토큰 문자열을 반환합니다.
     */
    public String generateToken(String username) {
        return Jwts.builder()
                .setSubject(username)  // 토큰의 subject로 사용자 이름 설정
                .setIssuedAt(new Date(System.currentTimeMillis()))  // 토큰 발행 시간 설정
                .setExpiration(new Date(System.currentTimeMillis() + expiration * 1000))  // 만료 시간 설정
                .signWith(getSigningKey(), SignatureAlgorithm.HS512)  // HMAC SHA-512 알고리즘으로 서명
                .compact();  // 토큰을 문자열로 변환
    }

    /**
     * 주어진 토큰이 유효한지 확인합니다.
     * - 토큰에서 추출한 사용자 이름과 주어진 사용자 이름이 일치하는지 확인합니다.
     * - 토큰이 만료되지 않았는지도 확인합니다.
     *
     * @param token 검증할 JWT 토큰
     * @param username 토큰과 비교할 사용자 이름
     * @return 토큰이 유효하면 true, 그렇지 않으면 false를 반환합니다.
     */
    public Boolean validateToken(String token, String username) {
        final String tokenUsername = extractUsername(token);
        return (tokenUsername.equals(username) && !isTokenExpired(token));  // 사용자 이름 확인 및 토큰 만료 여부 확인
    }

    /**
     * 토큰에서 사용자 이름(subject)을 추출합니다.
     * - JWT의 subject는 일반적으로 사용자 ID나 이름입니다.
     *
     * @param token JWT 토큰
     * @return 토큰에서 추출한 사용자 이름을 반환합니다.
     */
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);  // 토큰에서 subject 클레임 추출
    }

    @Value("${jwt.secret}")  // application.properties 또는 application.yml에 정의된 JWT 비밀키를 주입받음
    private String secret;

    @Value("${jwt.expiration}")  // JWT 만료 시간을 주입받음 (초 단위)
    private Long expiration;

    /**
     * JWT 서명을 위한 키를 생성합니다.
     * - secret 문자열을 UTF-8로 인코딩한 후 HMAC SHA 알고리즘을 사용해 서명 키를 생성합니다.
     *
     * @return 생성된 서명 키를 반환합니다.
     */
    private Key getSigningKey() {
        byte[] keyBytes = secret.getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(keyBytes);  // HMAC SHA를 사용한 키 생성
    }


    /**
     * 토큰에서 만료 시간을 추출합니다.
     *
     * @param token JWT 토큰
     * @return 토큰에서 추출한 만료 시간을 반환합니다.
     */
    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);  // 토큰에서 expiration 클레임 추출
    }

    /**
     * 토큰에서 지정한 클레임을 추출하는 유틸리티 메서드입니다.
     * - 클레임이란 JWT 내에서 전달되는 정보(예: subject, expiration 등)를 말합니다.
     *
     * @param token JWT 토큰
     * @param claimsResolver 클레임을 추출하는 함수
     * @param <T> 반환 타입
     * @return 추출한 클레임의 값
     */
    private <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);  // 토큰의 모든 클레임을 추출
        return claimsResolver.apply(claims);  // 지정한 클레임 추출
    }

    /**
     * JWT 토큰에서 모든 클레임을 추출합니다.
     * - 서명 키를 사용해 토큰을 파싱하고 클레임을 얻습니다.
     *
     * @param token JWT 토큰
     * @return 토큰의 모든 클레임을 반환합니다.
     */
    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())  // 서명 키 설정
                .build()
                .parseClaimsJws(token)  // 토큰을 파싱하여 클레임 추출
                .getBody();  // 클레임 반환
    }

    /**
     * 주어진 토큰이 만료되었는지 확인합니다.
     * - 토큰의 만료 시간이 현재 시간보다 이전인지 확인합니다.
     *
     * @param token JWT 토큰
     * @return 토큰이 만료되었으면 true, 그렇지 않으면 false를 반환합니다.
     */
    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());  // 만료 시간이 현재 시간보다 이전이면 true
    }
}