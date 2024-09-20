package com.ureca.compliment.compliment.dao;

import com.ureca.compliment.compliment.Compliment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.ureca.compliment.util.DBUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Repository
public class ComplimentDAOImpl implements ComplimentDAO{
    @Autowired
    DBUtil dbUtil;

    @Override
    public int insertCompliment(Compliment compliment) throws SQLException {
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

    @Override
    public List<Compliment> findAll() throws SQLException {
        Connection connection = dbUtil.getConnection();
        String sql = "SELECT * FROM COMPLIMENT";

        List<Compliment> compliments = new ArrayList<>();
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                compliments.add(new Compliment(
                        resultSet.getString("id"),
                        resultSet.getString("sender_id"),
                        resultSet.getString("receiver_id"),
                        resultSet.getString("content"),
                        resultSet.getBoolean("is_anonymous")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            connection.close();
        }

        return compliments;
    }

    @Override
    public List<Compliment> findBySenderIdAndDate(String senderId, Date date) throws SQLException {
        
        Connection connection = dbUtil.getConnection();
        String sql = """
            SELECT * FROM COMPLIMENT WHERE SENDER_ID = ?
            AND DATE(CREATED_AT) = ?;
        """;
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, senderId);
            preparedStatement.setDate(2, new java.sql.Date(date.getTime()));
            ResultSet resultSet =  preparedStatement.executeQuery();
            List<Compliment> compliments = new ArrayList<>();
            while(resultSet.next()){
                compliments.add(new Compliment(
                    resultSet.getString("id"),
                    resultSet.getString("sender_id"),
                    resultSet.getString("receiver_id"),
                    resultSet.getString("content"),
                    resultSet.getBoolean("is_anonymous")
                ));
            }
            return compliments;

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            connection.close();
        }
        return null;
    }

    @Override
    public List<Compliment> findBySenderId(String senderId) throws SQLException {
        Connection connection = dbUtil.getConnection();
        String sql = "SELECT * FROM COMPLIMENT WHERE SENDER_ID = ?";

        List<Compliment> compliments = new ArrayList<>();
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, senderId);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                compliments.add(new Compliment(
                        resultSet.getString("id"),
                        resultSet.getString("sender_id"),
                        resultSet.getString("receiver_id"),
                        resultSet.getString("content"),
                        resultSet.getBoolean("is_anonymous")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            connection.close();
        }

        return compliments;
    }

    @Override
    public List<Compliment> findByDate(Date date) throws SQLException {
        Connection connection = dbUtil.getConnection();
        String sql = "SELECT * FROM COMPLIMENT WHERE DATE(CREATED_AT) = ?";

        List<Compliment> compliments = new ArrayList<>();
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setDate(1, new java.sql.Date(date.getTime()));  // java.util.Date -> java.sql.Date 변환
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                compliments.add(new Compliment(
                        resultSet.getString("id"),
                        resultSet.getString("sender_id"),
                        resultSet.getString("receiver_id"),
                        resultSet.getString("content"),
                        resultSet.getBoolean("is_anonymous")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            connection.close();
        }

        return compliments;
    }

}
