package com.ureca.compliment.achievement.service;

import com.ureca.compliment.achievement.Achievement;
import com.ureca.compliment.achievement.dao.AchievementDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

@Service
public class AchievementServiceImpl implements AchievementService{
    @Autowired
    AchievementDAO dao;

    final int USERLIMIT = 3;

    @Override
    public List<Achievement> getSenderAchievements() throws SQLException {
        LocalDate now = LocalDate.now();
        List<Achievement> test = dao.findMonthlySenderAchievements(now, USERLIMIT);
        return test;
    }

    @Override
    public List<Achievement> getReceiverAchievements() throws SQLException {
        LocalDate now = LocalDate.now();
        return dao.findMonthlyReceiverAchievements(now, USERLIMIT);
    }
}
