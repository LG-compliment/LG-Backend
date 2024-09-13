package com.ureca.compliment.compliment.service;

import com.ureca.compliment.compliment.Compliment;
import com.ureca.compliment.compliment.dao.ComplimentDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.UUID;

@Service
public class ComplimentServiceImpl implements ComplimentService{
    @Autowired
    ComplimentDAO dao;

    @Override
    public int create(Compliment compliment) throws SQLException {
        compliment.setId(UUID.randomUUID().toString());
        return dao.insertComplimnet(compliment);
    }
}
