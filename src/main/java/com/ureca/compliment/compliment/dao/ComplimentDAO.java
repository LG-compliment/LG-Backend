package com.ureca.compliment.compliment.dao;

import com.ureca.compliment.compliment.Compliment;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;

public interface ComplimentDAO {
    int insertCompliment(Compliment compliment) throws SQLException;

    List<Compliment> findAll() throws SQLException;
    List<Compliment> findBySenderIdAndDate(String senderId, Date date) throws SQLException;

    List<Compliment> findBySenderId(String senderId) throws SQLException;

    List<Compliment> findByDate(Date date) throws SQLException;
}
