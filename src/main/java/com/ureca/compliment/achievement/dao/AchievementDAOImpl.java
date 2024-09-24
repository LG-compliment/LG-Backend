package com.ureca.compliment.achievement.dao;

import com.ureca.compliment.achievement.Achievement;
import com.ureca.compliment.util.DBUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Repository
public class AchievementDAOImpl implements AchievementDAO{
    @Autowired
    DBUtil dbUtil;

    @Override
    public List<Achievement> findMonthlySenderAchievements(LocalDate date, Integer limit) throws SQLException {
        Connection connection = dbUtil.getConnection();
        String sql = """
            SELECT  u.id AS userId,
                    u.username AS userName,
                    COUNT(c.id) AS complimentsCount
                    FROM
                        User u
                    LEFT JOIN
                        Compliment c ON u.id = c.sender_id
                    WHERE
                        YEAR(c.created_at) = ?
                        AND MONTH(c.created_at) = ?
                    GROUP BY
                        u.id, u.username
                    ORDER BY
                        complimentsCount DESC
                    LIMIT ?;
        """;


        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, date.getYear());
            preparedStatement.setInt(2, date.getMonthValue());
            preparedStatement.setInt(3, limit);

            ResultSet resultSet = preparedStatement.executeQuery();
            List<Achievement> achievements = new ArrayList<>();
            while (resultSet.next()) {
                Achievement achievement = new Achievement(
                        resultSet.getString("userId"),
                        resultSet.getString("userName"),
                        resultSet.getInt("complimentsCount")
                );
                achievements.add(achievement);
            }
            return achievements;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            connection.close();
        }
        return null;
    }

    @Override
    public List<Achievement> findMonthlyReceiverAchievements(LocalDate date, Integer limit) throws SQLException {
        Connection connection = dbUtil.getConnection();
        String sql = """
            SELECT  u.id AS userId,
                    u.username AS userName,
                    COUNT(c.id) AS complimentsCount
                    FROM
                        User u
                    LEFT JOIN
                        Compliment c ON u.id = c.receiver_id
                    WHERE
                        YEAR(c.created_at) = ?
                        AND MONTH(c.created_at) = ?
                    GROUP BY
                        u.id, u.username
                    ORDER BY
                        complimentsCount DESC
                    LIMIT ?;
        """;

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, date.getYear());
            preparedStatement.setInt(2, date.getMonthValue());
            preparedStatement.setInt(3, limit);

            ResultSet resultSet = preparedStatement.executeQuery();
            List<Achievement> achievements = new ArrayList<>();
            while (resultSet.next()) {
                Achievement achievement = new Achievement(
                        resultSet.getString("userId"),
                        resultSet.getString("userName"),
                        resultSet.getInt("complimentsCount")
                );
                achievements.add(achievement);
            }
            return achievements;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            connection.close();
        }
        return null;
    }
}
