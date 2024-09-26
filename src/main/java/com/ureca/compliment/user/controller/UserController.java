package com.ureca.compliment.user.controller;

import com.ureca.compliment.user.User;
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
        Map<String, Object> data = userService.getUser(id);
        ResponseWrapper<Map<String, Object>> response = new ResponseWrapper<>(
                String.valueOf(HttpStatus.OK.value()),  // Status code as a string
                HttpStatus.OK.getReasonPhrase(),  // "OK"
                data  // Data from userService
        );
        return ResponseEntity.ok().body(response);
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

    @PostMapping("/sign-up")
    public ResponseEntity<?> signUp(@RequestBody Map<String, String> signUpRequest) {
        String id = signUpRequest.get("id");
        String name = signUpRequest.get("name");
        String password = signUpRequest.get("password");

        try {
            userService.signUp(id, name, password);
            ResponseWrapper<String> successResponse = new ResponseWrapper<>(
                    String.valueOf(HttpStatus.CREATED.value()),
                    HttpStatus.CREATED.getReasonPhrase(),
                    "회원가입이 성공적으로 완료되었습니다."
            );
            return ResponseEntity.status(HttpStatus.CREATED).body(successResponse);
        } catch (SQLException e) {
            ResponseWrapper<String> errorResponse = new ResponseWrapper<>(
                    String.valueOf(HttpStatus.BAD_REQUEST.value()),
                    "회원가입 중 오류가 발생했습니다: " + e.getMessage(),
                    null
            );
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        } catch (Exception e) {
            ResponseWrapper<String> errorResponse = new ResponseWrapper<>(
                    String.valueOf(HttpStatus.BAD_REQUEST.value()),
                    "회원가입 요청을 처리할 수 없습니다: " + e.getMessage(),
                    null
            );
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }

    @GetMapping("/check-id")
    public ResponseEntity<?> checkId(@RequestParam(required = false) String id) throws UserNotFoundException, SQLException {
        Map<String, Object> data = userService.checkUserId(id);
        ResponseWrapper<Map<String, Object>> response = new ResponseWrapper<>(
            String.valueOf(HttpStatus.OK.value()),  // Status code as a string
            HttpStatus.OK.getReasonPhrase(),  // "OK"
            data  // Data from userService
        );
        return ResponseEntity.ok().body(response);
    }
}
