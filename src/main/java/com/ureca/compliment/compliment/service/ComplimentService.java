package com.ureca.compliment.compliment.service;

import com.ureca.compliment.compliment.Compliment;

import java.sql.SQLException;
import java.util.Map;

public interface ComplimentService {
    Map<String, Integer> create(Compliment compliment) throws SQLException;
}
