package com.ureca.compliment.compliment.service;

import com.ureca.compliment.compliment.Compliment;
import com.ureca.compliment.compliment.dao.ComplimentDAO;
import com.ureca.compliment.compliment.dto.ComplimentDTO;
import com.ureca.compliment.compliment.exceptions.ComplimentAlreadyExistsException;
import com.ureca.compliment.compliment.mapper.ComplimentMapper;
import com.ureca.compliment.user.dto.UserDTO;
import com.ureca.compliment.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class ComplimentServiceImpl implements ComplimentService{
    @Autowired
    ComplimentDAO dao;

    @Autowired
    UserService userService;

    @Override
    public Map<String, Integer> create(Compliment compliment) throws SQLException, ComplimentAlreadyExistsException {
        if(Objects.equals(compliment.getSenderId(), compliment.getReceiverId())){
            throw new IllegalArgumentException("Compliments cannot be sent to yourself");
        }
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
    public Map<String, List<ComplimentDTO>> getCompliments(String senderId, Date date) throws SQLException {
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

        Map<String, List<ComplimentDTO>> result = new HashMap<>();

        Set<String> userIds = Stream.concat(
                compliments.stream().map(Compliment::getSenderId), // senderId 스트림
                compliments.stream().map(Compliment::getReceiverId) // receiverId 스트림
        ).collect(Collectors.toSet()); // 두 스트림을 합친 후 Set으로 수집
        List<UserDTO> users = userService.getUsersByIds(userIds);
        Map<String, UserDTO> userMap = users.stream()
                .collect(Collectors.toMap(UserDTO::getId, user -> user));
        for (Compliment compliment : compliments) {
            UserDTO sender = userMap.get(compliment.getSenderId());
            UserDTO receiver = userMap.get(compliment.getReceiverId());
            ComplimentDTO complimentDTO = ComplimentMapper.toDTO(compliment, sender, receiver);
            result.computeIfAbsent("compliments", k -> new ArrayList<>()).add(complimentDTO);
        }

        return result;
    }
}
