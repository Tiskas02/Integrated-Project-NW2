package com.example.integratedbackend.Controller;

import com.example.integratedbackend.DTO.LoginRequest;
import com.example.integratedbackend.DTO.LoginResponse;
import com.example.integratedbackend.JWT.JwtUtil;
import com.example.integratedbackend.Service.AuthService;
import com.example.integratedbackend.Service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/login")
@CrossOrigin(origins = {"http://ip23nw2.sit.kmutt.ac.th", "http://intproj23.sit.kmutt.ac.th","*"})
public class AuthController {
    @Autowired
    private AuthService authService;

//    @GetMapping("/check-password")
//    public ResponseEntity<?> checkPassword(@RequestParam String password, @RequestParam String username) {
//        if (username == null || username.isEmpty() || username.length() > 50 || password == null || password.isEmpty() || password.length() > 14) {
//            // Return 400 Bad Request
//            return ResponseEntity.badRequest().body("Username or Password is invalid");
//        }
//
//        boolean isValid = userService.checkPassword(username, password);
//        if (!isValid) {
//            // Return 401 Unauthorized
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Username or Password is incorrect");
//        }
//
//        // Return 200 OK
//        return ResponseEntity.ok(true);
//    }

    @PostMapping
    public ResponseEntity<LoginResponse> login(@RequestBody @Valid LoginRequest loginRequest) {
        return ResponseEntity.ok(authService.authenticate(loginRequest));
    }

}
