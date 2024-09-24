package com.ureca.compliment.achievement.dao;

import com.ureca.compliment.achievement.Achievement;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public interface AchievementDAO {
    List<Achievement> findMonthlySenderAchievements(LocalDate date, Integer limit) throws SQLException;
    List<Achievement> findMonthlyReceiverAchievements(LocalDate date, Integer limit) throws SQLException;
}
