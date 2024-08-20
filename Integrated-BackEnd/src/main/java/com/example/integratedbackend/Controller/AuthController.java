package com.example.integratedbackend.Controller;

import com.example.integratedbackend.JWT.JwtUtil;
import com.example.integratedbackend.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired
    private UserService userService;
    @Autowired
    private JwtUtil jwtUtil;

    @GetMapping("/check-password")
    public ResponseEntity<?> checkPassword(@RequestParam String password, @RequestParam String username) {
        if (username == null || username.isEmpty() || username.length() > 50 || password == null || password.isEmpty() || password.length() > 14) {
            // Return 400 Bad Request
            return ResponseEntity.badRequest().body("Username or Password is invalid");
        }

        boolean isValid = userService.checkPassword(username, password);
        if (!isValid) {
            // Return 401 Unauthorized
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Username or Password is incorrect");
        }

        // Return 200 OK
        return ResponseEntity.ok(true);
    }

    @PostMapping("/login")
    public String login(@RequestParam String username, @RequestParam String password) {
        boolean valid = userService.checkPassword(username, password);
        if (valid) {
            return jwtUtil.generateToken(username);
        }else {
            throw new RuntimeException("Invalid username or password");
        }
    }
}
