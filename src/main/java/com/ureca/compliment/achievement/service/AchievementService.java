package com.ureca.compliment.achievement.service;

import com.ureca.compliment.achievement.Achievement;

import java.sql.SQLException;
import java.util.List;

public interface AchievementService {
    List<Achievement> getSenderAchievements() throws SQLException;
    List<Achievement> getReceiverAchievements() throws SQLException;
}
