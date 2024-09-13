package com.ureca.compliment.compliment.dao;

import com.ureca.compliment.compliment.Compliment;

import java.sql.SQLException;

public interface ComplimentDAO {
    int insertComplimnet(Compliment compliment) throws SQLException;
}
