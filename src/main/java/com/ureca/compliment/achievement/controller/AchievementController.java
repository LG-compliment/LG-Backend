package com.ureca.compliment.achievement.controller;

import com.ureca.compliment.achievement.Achievement;
import com.ureca.compliment.achievement.service.AchievementService;
import com.ureca.compliment.util.response.ResponseWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/achievements")
public class AchievementController {
    @Autowired
    AchievementService service;

    @GetMapping("/monthly-senders")
    public ResponseEntity<?> monthlySenderList() throws SQLException {
        List<Achievement> senders =  service.getSenderAchievements();
        Map<String, List<Achievement>> data = new HashMap<>();
        data.put("achievements", senders);
        ResponseWrapper<Map<String, List<Achievement>>> response = new ResponseWrapper<>(
                String.valueOf(HttpStatus.OK.value()),
                HttpStatus.OK.getReasonPhrase(),
                data
        );
        return ResponseEntity.ok(response);
    };

    @GetMapping("/monthly-receivers")
    public ResponseEntity<?> monthlyReceiverList() throws SQLException {
        List<Achievement> receivers =  service.getReceiverAchievements();
        Map<String, List<Achievement>> data = new HashMap<>();
        data.put("achievements", receivers);
        ResponseWrapper<Map<String, List<Achievement>>> response = new ResponseWrapper<>(
                String.valueOf(HttpStatus.OK.value()),
                HttpStatus.OK.getReasonPhrase(),
                data
        );
        return ResponseEntity.ok(response);
    };
}
