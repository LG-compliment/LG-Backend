package com.ureca.compliment.compliment.service;

import com.ureca.compliment.compliment.Compliment;
import com.ureca.compliment.compliment.exceptions.ComplimentAlreadyExistsException;

import java.sql.SQLException;
import java.util.Date;
import java.util.Map;

public interface ComplimentService {
    Map<String, Integer> create(Compliment compliment) throws SQLException, ComplimentAlreadyExistsException;
    Map<String, Object> getCompliments(String senderId, Date date) throws SQLException;
}
