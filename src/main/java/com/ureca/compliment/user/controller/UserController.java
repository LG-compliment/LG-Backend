package com.ureca.compliment.user.controller;

import com.ureca.compliment.user.exceptions.UserNotFoundException;
import com.ureca.compliment.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.sql.SQLException;

@Controller
@RequestMapping("/users")
public class UserController {
    @Autowired
    UserService userService;

    @GetMapping("/{userId}")
    public ResponseEntity<?> getUser(
            @PathVariable("userId") String id
    ) throws UserNotFoundException, SQLException {
        return ResponseEntity.ok().body(userService.getUser(id));
    }
}
