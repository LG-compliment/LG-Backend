package com.ureca.compliment.user.service;

import com.ureca.compliment.compliment.Compliment;
import com.ureca.compliment.user.User;
import com.ureca.compliment.user.dao.UserDAO;
import com.ureca.compliment.user.exceptions.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    UserDAO dao;

    @Override
    public void logIn(String id, String password) throws SQLException{

    }

    @Override
    public List<User> getAllUsers() throws SQLException{
        return List.of();
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
