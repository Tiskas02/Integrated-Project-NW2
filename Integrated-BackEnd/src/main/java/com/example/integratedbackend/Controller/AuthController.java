package com.example.integratedbackend.Controller;

import com.example.integratedbackend.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired
    private UserService userService;

    @GetMapping("/check-password")
    public ResponseEntity<?> checkPassword(@RequestParam String password, @RequestParam String username) {
        if (username == null || username.isEmpty() || username.length() > 50 || password == null || password.isEmpty() || password.length() > 14) {
            // Returning 400 Bad Request with a custom error message
            return ResponseEntity.badRequest().body("Username or Password is invalid");
        }

        boolean isValid = userService.checkPassword(username, password);
        if (!isValid) {
            // Returning 401 Unauthorized with a custom error message
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Username or Password is incorrect");
        }

        // Returning 200 OK with a success message or true
        return ResponseEntity.ok(true);
    }
}
