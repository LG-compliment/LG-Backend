package com.ureca.compliment.user.controller;

import com.ureca.compliment.user.exceptions.UserNotFoundException;
import com.ureca.compliment.user.service.UserService;
import com.ureca.compliment.util.auth.JwtUtil;
import com.ureca.compliment.util.response.ResponseWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.Map;

@Controller
@RequestMapping("/users")
public class UserController {
    @Autowired
    UserService userService;

    @GetMapping()
    public ResponseEntity<?> getAllUsers() throws SQLException {
        Map<String, Object> data = userService.getAllUsers();
        ResponseWrapper<Map<String, Object>> response = new ResponseWrapper<>(
                String.valueOf(HttpStatus.OK.value()),  // Status code as a string
                HttpStatus.OK.getReasonPhrase(),  // "OK"
                data  // Data from userService
        );
        return ResponseEntity.ok().body(response);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<?> getUser(
            @PathVariable("userId") String id
    ) throws UserNotFoundException, SQLException {
        return ResponseEntity.ok().body(userService.getUser(id));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> loginRequest) {
        String id = loginRequest.get("id");
        String password = loginRequest.get("password");

        try {
            Map<String, Object> data = userService.logIn(id, password);
            ResponseWrapper<Map<String, Object>> response = new ResponseWrapper<>(
                    String.valueOf(HttpStatus.OK.value()),  // Status code as a string
                    HttpStatus.OK.getReasonPhrase(),  // "OK"
                    data  // Data from userService
            );
            return ResponseEntity.ok(response);
        } catch (SQLException e) {
            ResponseWrapper<Map<String, Object>> response = new ResponseWrapper<>(
                    String.valueOf(HttpStatus.BAD_REQUEST.value()),  // Status code as a string
                    "Invalid credentials",
                    null  // No data to send back
            );
            return ResponseEntity.badRequest().body(response);
        }
    }
}
