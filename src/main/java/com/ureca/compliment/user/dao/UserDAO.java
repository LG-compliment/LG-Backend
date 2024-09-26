package com.ureca.compliment.user.dao;

import com.ureca.compliment.user.User;
import com.ureca.compliment.user.exceptions.UserNotFoundException;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface UserDAO {
    User selectUserByIdAndPassword(String id, String password) throws SQLException, UserNotFoundException;
    User selectUserById(String id) throws SQLException, UserNotFoundException;
<<<<<<< HEAD
=======
    Optional<User> findBySlackId(String id) throws  SQLException, UserNotFoundException;
    void save(User user) throws SQLException;
>>>>>>> ddf5994 (🚧 Feat: Slack Oauth)

    List<User> selectUsersByIds(Set<String> ids) throws SQLException;
    List<User> selectAllUsers() throws SQLException;
    void saveUser(User user) throws SQLException;
}
