package com.example.integratedbackend.Controller;

import com.example.integratedbackend.DTO.DTOV3.RefreshTokenRequest;
import com.example.integratedbackend.DTO.LoginRequest;
import com.example.integratedbackend.DTO.LoginResponse;
import com.example.integratedbackend.Service.AuthService;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
//@RequestMapping("/login")
@CrossOrigin(origins = {"http://ip23nw2.sit.kmutt.ac.th", "http://intproj23.sit.kmutt.ac.th","*"})
public class AuthController {
    @Autowired
    private AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody @Valid LoginRequest loginRequest) {
        return ResponseEntity.ok(authService.authenticate(loginRequest));
    }

//    @PostMapping("/token")
//    public ResponseEntity<LoginResponse> refreshAccessToken(@RequestHeader @Valid RefreshTokenRequest refreshTokenRequest) {
//        return ResponseEntity.ok(authService.refreshToken(refreshTokenRequest.getRefresh_token()));
//    }

    @PostMapping("/token")
    public ResponseEntity<LoginResponse> refreshAccessToken(@RequestHeader("Authorization") String refreshToken) {
        return ResponseEntity.ok(authService.refreshToken(refreshToken));
    }


}
