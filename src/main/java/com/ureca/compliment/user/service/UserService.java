package com.ureca.compliment.user.service;

import com.ureca.compliment.compliment.Compliment;
import com.ureca.compliment.user.User;
import com.ureca.compliment.user.exceptions.UserNotFoundException;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface UserService {
    Optional<User> logIn(String id, String password) throws SQLException;
    List<User> getAllUsers() throws SQLException;
    Optional<User> getUser(String id) throws SQLException, UserNotFoundException;
    List<Compliment> getCompliments(String id) throws SQLException;
}
