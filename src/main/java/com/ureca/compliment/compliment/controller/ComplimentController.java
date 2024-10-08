package com.ureca.compliment.compliment.controller;

import com.ureca.compliment.compliment.Compliment;
import com.ureca.compliment.compliment.dto.ComplimentDTO;
import com.ureca.compliment.compliment.exceptions.ComplimentAlreadyExistsException;
import com.ureca.compliment.compliment.service.ComplimentService;
import com.ureca.compliment.util.response.ResponseWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/compliments")
public class ComplimentController {
    @Autowired
    ComplimentService service;

    @PostMapping("")
    public ResponseEntity<?> create(@RequestBody Compliment compliment) throws SQLException{

        try {
            Map<String, Integer> data = service.create(compliment);
            ResponseWrapper<Map<String, Integer>> response = new ResponseWrapper<>(
                    String.valueOf(HttpStatus.OK.value()),  // Status code as a string
                    HttpStatus.OK.getReasonPhrase(),  // "OK"
                    data
            );
            return ResponseEntity.ok().body(response);
        } catch (ComplimentAlreadyExistsException e) {
            // Handle the case where the compliment already exists (return 409 Conflict)
            ResponseWrapper<String> errorResponse = new ResponseWrapper<>(
                    String.valueOf(HttpStatus.CONFLICT.value()),  // 409 status code
                    e.getMessage(),  // Error message
                    null
            );
            return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse);
        } catch (IllegalArgumentException ie){
            ResponseWrapper<String> errorResponse = new ResponseWrapper<>(
                    String.valueOf(HttpStatus.BAD_REQUEST.value()),  // 400 status code
                    ie.getMessage(),  // Error message
                    null
            );
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }

    }

    @GetMapping("")
    public ResponseEntity<?> list(
            @RequestParam(required = false) String senderId,
            @RequestParam(required = false) @DateTimeFormat(pattern="yyyy-MM-dd") Date date
    ) throws SQLException{
        Map<String, List<ComplimentDTO>> data = service.getCompliments(senderId, date);
        ResponseWrapper<Map<String, List<ComplimentDTO>>> response = new ResponseWrapper<>(
                String.valueOf(HttpStatus.OK.value()),  // Status code as a string
                HttpStatus.OK.getReasonPhrase(),  // "OK"
                data
        );
        return ResponseEntity.ok().body(response);
    }
}
