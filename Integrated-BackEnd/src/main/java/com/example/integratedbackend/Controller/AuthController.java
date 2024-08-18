package com.example.integratedbackend.Controller;

import com.example.integratedbackend.JWT.JwtUtil;
import com.example.integratedbackend.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired
    private UserService userService;
    @Autowired
    private JwtUtil jwtUtil;

    @GetMapping("/check-password")
    public boolean checkPassword(@RequestParam String password, @RequestParam String username) {
        return userService.checkPassword(username, password);
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
