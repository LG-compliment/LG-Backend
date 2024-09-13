package com.ureca.compliment.compliment.controller;

import com.ureca.compliment.compliment.Compliment;
import com.ureca.compliment.compliment.service.ComplimentService;
import com.ureca.compliment.util.response.ResponseWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.sql.SQLException;
import java.util.Map;

@Controller
@RequestMapping("/compliments")
public class ComplimentController {
    @Autowired
    ComplimentService service;

    @PostMapping("/create")
    public ResponseEntity<?> create(@RequestBody Compliment compliment) throws SQLException{
        Map<String, Integer> data = service.create(compliment);
        ResponseWrapper<Map<String, Integer>> response = new ResponseWrapper<>(
                String.valueOf(HttpStatus.OK.value()),  // Status code as a string
                HttpStatus.OK.getReasonPhrase(),  // "OK"
                data
        );
        return ResponseEntity.ok().body(response);
    }

    @GetMapping("")
    public ResponseEntity<?> senderList(@RequestParam("senderId") String senderId,
                                      @RequestParam("date")String date) throws SQLException{
        Map<String, Object> data = service.senderList(senderId, date);
        ResponseWrapper<Map<String, Object>> response = new ResponseWrapper<>(
                String.valueOf(HttpStatus.OK.value()),  // Status code as a string
                HttpStatus.OK.getReasonPhrase(),  // "OK"
                data
        );
        return ResponseEntity.ok().body(response);
    }

}
