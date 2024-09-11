package com.ureca.compliment.user.controller;

import com.ureca.compliment.user.exceptions.UserNotFoundException;
import com.ureca.compliment.user.service.UserService;
import com.ureca.compliment.util.auth.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
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
        return ResponseEntity.ok().body(userService.getAllUsers());
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
            Map<String, Object> response = userService.logIn(id, password);
            return ResponseEntity.ok(response);
        } catch (SQLException e) {
            return ResponseEntity.badRequest().body("Invalid credentials");
        }
    }
}
