package com.ureca.compliment.compliment.dao;

import com.ureca.compliment.compliment.Compliment;

import java.sql.SQLException;
import java.util.List;

public interface ComplimentDAO {
    int insertCompliment(Compliment compliment) throws SQLException;

    List<Compliment> senderList(String senderId, String date) throws SQLException;
}
