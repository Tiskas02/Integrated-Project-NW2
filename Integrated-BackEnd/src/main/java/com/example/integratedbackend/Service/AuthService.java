package com.example.integratedbackend.Service;

import com.example.integratedbackend.DTO.LoginRequest;
import com.example.integratedbackend.DTO.LoginResponse;

import com.example.integratedbackend.JWT.JwtUtil;
import com.example.integratedbackend.Users.User;
import com.example.integratedbackend.Users.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class AuthService {
    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserRepository userRepository;

    private Argon2PasswordEncoder encoder() {
        return new Argon2PasswordEncoder(8,16,3,2048,1);
    }

    public User matchUser(LoginRequest loginRequest) throws ResponseStatusException {
        User user = userRepository.findByUsername(loginRequest.getUserName());

        if (user == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        }

        if (!encoder().matches(loginRequest.getPassword(), user.getPassword())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Password does not match");
        }

        return user; // Return the user if everything is valid
    }

    public LoginResponse authenticate(LoginRequest loginRequest) {
        User user = matchUser(loginRequest);
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getUserName(), loginRequest.getPassword(
                )));
        String userToken = jwtUtil.generateToken(user);
        return new LoginResponse(userToken);
    }
}
