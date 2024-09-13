package com.ureca.compliment.compliment.dao;

import com.ureca.compliment.compliment.Compliment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.ureca.compliment.util.DBUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

@Repository
public class ComplimentDAOImpl implements ComplimentDAO{
    @Autowired
    DBUtil dbUtil;

    @Override
    public int insertComplimnet(Compliment compliment) throws SQLException {
        Connection connection = dbUtil.getConnection();
        String sql = """
                    INSERT INTO COMPLIMENT(ID, SENDER_ID, RECEIVER_ID, CONTENT, IS_ANONYMOUS) 
                    VALUES (?, ?, ?, ?, ?);      
                """;

        int result = 0;
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, compliment.getId());
            preparedStatement.setString(2, compliment.getSenderId());
            preparedStatement.setString(3, compliment.getReceiverId());
            preparedStatement.setString(4, compliment.getContent());
            preparedStatement.setBoolean(5, compliment.isAnonymous());

            result = preparedStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            connection.close();
        }
        return result;
    }
}
