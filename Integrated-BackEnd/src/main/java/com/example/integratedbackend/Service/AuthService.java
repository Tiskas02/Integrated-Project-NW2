package com.example.integratedbackend.Service;

import com.example.integratedbackend.DTO.DTOV3.RefreshTokenRequest;
import com.example.integratedbackend.DTO.LoginRequest;
import com.example.integratedbackend.DTO.LoginResponse;

import com.example.integratedbackend.JWT.JwtUtil;
import com.example.integratedbackend.Kradankanban.kradankanbanV3.Entities.Users;
import com.example.integratedbackend.Kradankanban.kradankanbanV3.Repositories.UsersRepositoriesV3;
import com.example.integratedbackend.Users.User;
import com.example.integratedbackend.Users.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
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

    @Autowired
    private UsersRepositoriesV3 usersRepositoriesV3;
    @Autowired
    ModelMapper modelMapper;
    @Qualifier("userEntityManager")
    @Autowired
    private LocalContainerEntityManagerFactoryBean userEntityManager;

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
        if (user != null) {
            Users users = modelMapper.map(user, Users.class);
            Users addedUser = usersRepositoriesV3.save(users);
        }
        String accessToken = jwtUtil.generateToken(user);
        String refreshToken = jwtUtil.generateRefreshToken(user);
        return new LoginResponse(accessToken, refreshToken);
    }

    public LoginResponse refreshToken(String refreshToken) {
        if (refreshToken != null && refreshToken.startsWith("Bearer ")) {
            String username = jwtUtil.extractUsername(refreshToken.substring(7));
            if (username != null) {
                User user = userRepository.findByUsername(username);
                if (jwtUtil.validateRefreshToken(refreshToken.substring(7), username) ){
                    String newAccessToken = jwtUtil.generateToken(user);
                    return new LoginResponse(newAccessToken, null);
                }
            }
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid refresh token");
        }
        throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid refresh token");
    }


}
