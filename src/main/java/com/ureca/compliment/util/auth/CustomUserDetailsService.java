package com.ureca.compliment.util.auth;

import com.ureca.compliment.user.User;
import com.ureca.compliment.user.dao.UserDAO;
import com.ureca.compliment.user.exceptions.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.sql.SQLException;

/**
 * CustomUserDetailsService는 Spring Security의 UserDetailsService를 구현하여
 * 데이터베이스에서 사용자를 로드하는 역할을 합니다.
 * - 이 클래스는 사용자 이름(ID)으로 사용자를 검색하고 UserDetails 객체로 반환합니다.
 */
@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    UserDAO dao;

    /**
     * 주어진 사용자 이름(여기서는 사용자 ID)을 기반으로 데이터베이스에서 사용자를 검색하고,
     * UserDetails 객체로 변환하여 반환합니다.
     *
     * @param username 사용자의 ID
     * @return UserDetails 객체로 변환된 사용자 정보
     * @throws UsernameNotFoundException 사용자를 찾지 못했을 때 예외를 던집니다.
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user;

        try {
            // DAO를 사용해 사용자 ID로 사용자를 검색
            user = dao.selectUserById(username);
        } catch (SQLException | UserNotFoundException e) {
            // SQL 또는 사용자를 찾지 못한 경우 예외 발생
            throw new RuntimeException(e);
        }

        // Spring Security의 UserDetails 객체로 사용자 정보를 변환하여 반환
        return org.springframework.security.core.userdetails.User
                .withUsername(user.getId())  // 사용자 ID를 설정
                .password(user.getPassword())  // 사용자 비밀번호 설정
                .roles("USER")  // 기본 역할(USER) 설정
                .build();  // UserDetails 객체 생성
    }
}