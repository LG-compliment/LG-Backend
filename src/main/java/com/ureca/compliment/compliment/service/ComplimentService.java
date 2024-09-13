package com.ureca.compliment.compliment.service;

import com.ureca.compliment.compliment.Compliment;

import java.sql.SQLException;

public interface ComplimentService {
    int create(Compliment compliment) throws SQLException;
}
