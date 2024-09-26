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
import java.util.*;

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
    public Optional<User> findBySlackId(String id) throws SQLException, UserNotFoundException {
        Connection connection = dbUtil.getConnection();

        String sql = """
            SELECT
                *
            FROM USER
            WHERE slack_id = ?;
        """;

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return Optional.of(new User(
                        resultSet.getString("id"),
                        resultSet.getString("username"),
                        resultSet.getString("password"),
                        resultSet.getDate("created_at"),
                        resultSet.getDate("updated_at")
                ));
            } else {
                return Optional.empty();
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

    public void save(User user) throws SQLException {
        Connection connection = dbUtil.getConnection();

        String sqlSelect = "SELECT COUNT(*) FROM USER WHERE id = ?;";
        String sqlInsert = """
        INSERT INTO USER (id, username, password, created_at, updated_at, slack_id)
        VALUES (?, ?, ?, ?, ?, ?);
    """;
        String sqlUpdate = """
        UPDATE USER
        SET username = ?, password = ?, updated_at = ?, slack_id = ?
        WHERE id = ?;
    """;

        try (PreparedStatement selectStatement = connection.prepareStatement(sqlSelect)) {
            selectStatement.setString(1, user.getId());
            ResultSet resultSet = selectStatement.executeQuery();

            if (resultSet.next() && resultSet.getInt(1) > 0) {
                // User exists, perform UPDATE
                try (PreparedStatement updateStatement = connection.prepareStatement(sqlUpdate)) {
                    updateStatement.setString(1, user.getName());
                    updateStatement.setString(2, user.getPassword());
                    updateStatement.setDate(3, new java.sql.Date(System.currentTimeMillis()));
                    updateStatement.setString(4, user.getSlackId());
                    updateStatement.setString(5, user.getId());
                    updateStatement.executeUpdate();
                }
            } else {
                // User does not exist, perform INSERT
                try (PreparedStatement insertStatement = connection.prepareStatement(sqlInsert)) {
                    insertStatement.setString(1, user.getId());
                    insertStatement.setString(2, user.getName());
                    insertStatement.setString(3, user.getPassword());
                    insertStatement.setDate(4, new java.sql.Date(user.getCreatedAt().getTime()));
                    insertStatement.setDate(5, new java.sql.Date(System.currentTimeMillis()));
                    insertStatement.setString(6, user.getSlackId());
                    insertStatement.executeUpdate();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new SQLException("Error saving user to database");
        } finally {
            connection.close();
        }
    }
}
