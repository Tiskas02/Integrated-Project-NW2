package com.example.integratedbackend.Controller;

import com.example.integratedbackend.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
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
    public boolean checkPassword(@RequestParam String password, @RequestParam String username) {
        return userService.checkPassword(username, password);
    }
}
