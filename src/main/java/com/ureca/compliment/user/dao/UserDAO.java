package com.ureca.compliment.user.dao;

import com.ureca.compliment.user.User;
import com.ureca.compliment.user.exceptions.UserNotFoundException;

import java.sql.SQLException;
import java.util.List;

public interface UserDAO {
    User selectUserByIdAndPassword(String id, String password) throws SQLException, UserNotFoundException;
    User selectUserById(String id) throws SQLException, UserNotFoundException;
    List<User> selectAllUsers() throws SQLException;
}
