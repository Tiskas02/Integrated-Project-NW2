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
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/login")
@CrossOrigin(origins = {"http://ip23nw2.sit.kmutt.ac.th", "http://intproj23.sit.kmutt.ac.th","*"})
public class AuthController {
    @Autowired
    private AuthService authService;

//    @Autowired
//    private UserService userService;

//    @PostMapping
//    public ResponseEntity<LoginResponse> login(@RequestBody @Valid LoginRequest loginRequest) {
//        return ResponseEntity.ok(authService.authenticate(loginRequest));
//    }

    @PostMapping
    public ResponseEntity<LoginResponse> login(@RequestBody @Valid LoginRequest loginRequest) {
        try {
            LoginResponse response = authService.authenticate(loginRequest);
            return ResponseEntity.ok(response);

        } catch (AuthenticationException ex) {
            // Handle authentication failures (e.g., bad credentials)
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new LoginResponse("Username or Password is incorrect"));

        } catch (Exception ex) {
            // Handle unexpected exceptions (500 Internal Server Error)
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new LoginResponse("There is a problem. Please try again later."));
        }
    }

}
