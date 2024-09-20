package com.ureca.compliment.compliment.service;

import com.ureca.compliment.compliment.Compliment;
import com.ureca.compliment.compliment.dao.ComplimentDAO;
import com.ureca.compliment.compliment.exceptions.ComplimentAlreadyExistsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.*;

@Service
public class ComplimentServiceImpl implements ComplimentService{
    @Autowired
    ComplimentDAO dao;

    @Override
    public Map<String, Integer> create(Compliment compliment) throws SQLException, ComplimentAlreadyExistsException {
        List<Compliment> senderList = dao.findBySenderIdAndDate(compliment.getSenderId(), compliment.getCreatedAt());

        if (!senderList.isEmpty()) {
            throw new ComplimentAlreadyExistsException("Compliment already exists for this sender on this date");
        }


        compliment.setId(UUID.randomUUID().toString());
        
        Map<String, Integer> response = new HashMap<>();
        int insert = dao.insertCompliment(compliment);
        response.put("insert", insert);
        return response;
    }

    @Override
    public Map<String, List<Compliment>> getCompliments(String senderId, Date date) throws SQLException {
        List<Compliment> compliments;

        if (senderId != null && date != null) {
            compliments = dao.findBySenderIdAndDate(senderId, date);
        } else if (senderId != null) {
            compliments = dao.findBySenderId(senderId);
        } else if (date != null) {
            compliments = dao.findByDate(date);
        } else {
            compliments = dao.findAll(); 
        }
        Map<String, List<Compliment>> data = new HashMap<>();
        data.put("compliments", compliments);
        return data;
    }
}
