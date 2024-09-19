package com.ureca.compliment.compliment.service;

import com.ureca.compliment.compliment.Compliment;
import com.ureca.compliment.compliment.exceptions.ComplimentAlreadyExistsException;

import java.sql.SQLException;
import java.util.Map;

public interface ComplimentService {
    Map<String, Integer> create(Compliment compliment) throws SQLException, ComplimentAlreadyExistsException;
    Map<String, Object> senderList(String senderId, String date) throws SQLException;
}
