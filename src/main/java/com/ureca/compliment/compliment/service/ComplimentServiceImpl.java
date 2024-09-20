package com.ureca.compliment.compliment.service;

import com.ureca.compliment.compliment.Compliment;
import com.ureca.compliment.compliment.dao.ComplimentDAO;
import com.ureca.compliment.compliment.exceptions.ComplimentAlreadyExistsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.*;

@Service
public class ComplimentServiceImpl implements ComplimentService{
    @Autowired
    ComplimentDAO dao;

    @Override
    public Map<String, Integer> create(Compliment compliment) throws SQLException, ComplimentAlreadyExistsException {
        List<Compliment> senderList = dao.senderList(compliment.getSenderId(), compliment.getDate());

        if (!senderList.isEmpty()) {
            throw new ComplimentAlreadyExistsException("Compliment already exists for this sender on this date");
        }


        compliment.setId(UUID.randomUUID().toString());
        
        Map<String, Integer> response = new HashMap<>();
        int insert = dao.insertComplimnet(compliment);
        response.put("insert", insert);
        return response;
    }

    @Override
    public Map<String,  Object> senderList(String senderId, String date) throws SQLException {
        Map<String, Object> response = new HashMap<>();
        List<Compliment> senderList = dao.senderList(senderId, date);
        response.put("list", senderList);
        return response;
    }

    
}
