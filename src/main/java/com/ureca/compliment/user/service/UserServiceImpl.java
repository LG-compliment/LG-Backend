package com.ureca.compliment.user.service;

import com.ureca.compliment.compliment.Compliment;
import com.ureca.compliment.user.User;
import com.ureca.compliment.user.dao.UserDAO;
import com.ureca.compliment.user.exceptions.UserNotFoundException;

import com.ureca.compliment.util.auth.service.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

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
    public Optional<User> getUser(String id) throws SQLException, UserNotFoundException {
        try {
            User user = dao.selectUserById(id);
            return Optional.of(user);
        } catch (UserNotFoundException e) {
            return Optional.empty();
        }

    }

    @Override
    public List<Compliment> getCompliments(String id) throws SQLException{
        return List.of();
    }
}
