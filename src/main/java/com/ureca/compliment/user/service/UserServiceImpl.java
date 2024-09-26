package com.ureca.compliment.user.service;

import com.ureca.compliment.compliment.Compliment;
import com.ureca.compliment.user.User;
import com.ureca.compliment.user.dao.UserDAO;
import com.ureca.compliment.user.dto.UserDTO;
import com.ureca.compliment.user.exceptions.UserNotFoundException;

import com.ureca.compliment.user.mapper.UserMapper;
import com.ureca.compliment.util.auth.service.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.*;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    UserDAO dao;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    TokenService tokenService;

    @Override
    public Map<String, Object> logIn(String id, String password) throws SQLException{
        try {
            Map<String, Object> response = new java.util.HashMap<>();

            String token =  tokenService.generateToken(id, password);
            response.put("token", token);
            return response;
        } catch (Exception e) {
            throw new SQLException("Invalid credentials");
        }
    }

    @Override
    public void signUp(String id, String name, String password) throws SQLException {
        try {
            // 비밀번호 암호화
            String encodedPassword = passwordEncoder.encode(password);
            // 새 사용자 객체 생성
            User newUser = new User(id, name, encodedPassword);
            // 사용자 저장
            dao.saveUser(newUser);

        } catch (SQLException e) {
            // 중복된 ID 등의 무결성 위반 예외 처리
            throw new SQLException("이미 존재하는 사용자 ID입니다.");
        } catch (Exception e) {
            // 기타 예외 처리
            throw new SQLException("회원가입 중 오류가 발생했습니다: " + e.getMessage());
        }
    }

    @Override
    public Map<String, Object> getAllUsers() throws SQLException {
        Map<String, Object> response = new java.util.HashMap<>();

        List<User> users = dao.selectAllUsers();
        response.put("users", users);
        return response;
    }

    @Override
    public Map<String, Object> getUser(String id) throws SQLException, UserNotFoundException {
            User user = dao.selectUserById(id);
            Map<String, Object> response = new java.util.HashMap<>();
            response.put("name", user.getName());
            return response;
    }

    @Override
    public Map<String, Object> checkUserId(String id) throws SQLException, UserNotFoundException {
        Map<String, Object> response = new java.util.HashMap<>();

        try {
            dao.selectUserById(id);
            response.put("available", true);   // ID가 사용 가능함
        } catch (UserNotFoundException e) {
            response.put("available", false);  // ID가 중복됨
        }
        return response;
    }

    @Override
    public List<UserDTO> getUsersByIds(Set<String> userIds) throws SQLException {
        try {
            List<User> users = dao.selectUsersByIds(userIds);
            return UserMapper.toDTOList(users);
        } catch (Exception e) {
            throw new SQLException(e);
        }
    }

    @Override
    public List<Compliment> getCompliments(String id) throws SQLException{
        return List.of();
    }
}
