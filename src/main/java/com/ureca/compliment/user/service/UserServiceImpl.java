package com.ureca.compliment.user.service;

import com.ureca.compliment.compliment.Compliment;
import com.ureca.compliment.user.User;
import com.ureca.compliment.user.dao.UserDAO;
import com.ureca.compliment.user.dto.UserDTO;
import com.ureca.compliment.user.exceptions.UserNotFoundException;

import com.ureca.compliment.user.mapper.UserMapper;
import com.ureca.compliment.util.auth.service.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    UserDAO dao;

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
