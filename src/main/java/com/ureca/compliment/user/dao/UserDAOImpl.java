package com.ureca.compliment.user.dao;

import com.ureca.compliment.user.User;
import com.ureca.compliment.user.exceptions.UserNotFoundException;
import com.ureca.compliment.util.DBUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

@Repository
public class UserDAOImpl implements UserDAO{
    @Autowired
    DBUtil dbUtil;

    @Override
    public User selectUserByIdAndPassword(String id, String password) throws SQLException, UserNotFoundException {
        Connection connection = dbUtil.getConnection();

        String sql = """
            SELECT
                *
            FROM USER
            WHERE id = ? AND password = ?;
        """;

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, id);
            preparedStatement.setString(2, password);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return new User(
                        resultSet.getString("id"),
                        resultSet.getString("username"),
                        resultSet.getString("password"),
                        resultSet.getDate("created_at"),
                        resultSet.getDate("updated_at")
                );
            } else {
                throw new UserNotFoundException("User with ID" + id + " not found");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public User selectUserById(String id) throws SQLException, UserNotFoundException {
        Connection connection = dbUtil.getConnection();

        String sql = """
            SELECT
                *
            FROM USER
            WHERE id = ?;
        """;

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return new User(
                        resultSet.getString("id"),
                        resultSet.getString("username"),
                        resultSet.getString("password"),
                        resultSet.getDate("created_at"),
                        resultSet.getDate("updated_at")
                );
            } else {
                throw new UserNotFoundException("User with ID" + id + " not found");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            connection.close();
        }
        return null;
    }

    @Override
    public List<User> selectUsersByIds(Set<String> ids) throws SQLException {
        Connection connection = dbUtil.getConnection();

        if (ids.isEmpty()) {
            return Collections.emptyList();
        }

        String placeholders = String.join(",", Collections.nCopies(ids.size(), "?"));
        String sql = """
        SELECT
            *
        FROM USER
        WHERE id IN (%s);
    """.formatted(placeholders);

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            int index = 1;
            for (String id : ids) {
                preparedStatement.setString(index++, id);
            }

            ResultSet resultSet = preparedStatement.executeQuery();
            List<User> users = new ArrayList<>();
            while (resultSet.next()) {
                users.add(new User(
                        resultSet.getString("id"),
                        resultSet.getString("username"),
                        "",
                        resultSet.getDate("created_at"),
                        resultSet.getDate("updated_at")
                ));
            }
            return users;
        } catch (SQLException e) {
            e.printStackTrace();
            throw e; // SQLException을 다시 던져서 호출한 쪽에서 처리하게 할 수도 있음.
        } finally {
            connection.close();
        }
    }

    @Override
    public List<User> selectAllUsers() throws SQLException {
        Connection connection = dbUtil.getConnection();

        String sql = """
            SELECT
                *
            FROM USER
            ORDER BY USERNAME;
        """;

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            ResultSet resultSet = preparedStatement.executeQuery();
            List<User> users = new ArrayList<>();
            while (resultSet.next()) {
                users.add(new User(
                        resultSet.getString("id"),
                        resultSet.getString("username"),
                        resultSet.getString("password"),
                        resultSet.getDate("created_at"),
                        resultSet.getDate("updated_at")
                ));
            }
            return users;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            connection.close();
        }
        return null;
    }

    @Override
    public void saveUser(User user) throws SQLException {
        Connection connection = dbUtil.getConnection();

        String sql = """
        INSERT INTO USER (id, username, password, created_at, updated_at)
        VALUES (?, ?, ?, ?, ?);
    """;

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, user.getId());
            preparedStatement.setString(2, user.getName());
            preparedStatement.setString(3, user.getPassword());
            preparedStatement.setDate(4, new java.sql.Date(user.getCreatedAt().getTime()));
            preparedStatement.setDate(5, new java.sql.Date(user.getUpdatedAt().getTime()));

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new SQLException();
        } finally {
            connection.close();
        }
    }
}
