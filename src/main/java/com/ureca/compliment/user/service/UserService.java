package com.ureca.compliment.user.service;

import com.ureca.compliment.compliment.Compliment;
import com.ureca.compliment.user.User;
import com.ureca.compliment.user.exceptions.UserNotFoundException;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface UserService {
    Map<String, Object> logIn(String id, String password) throws SQLException;
    Map<String, Object> getAllUsers() throws SQLException;
    Map<String, Object> getUser(String id) throws SQLException, UserNotFoundException;
    List<Compliment> getCompliments(String id) throws SQLException;
}
