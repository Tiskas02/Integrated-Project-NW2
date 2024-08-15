package com.example.integratedbackend.Service;

import com.example.integratedbackend.Entities.User;
import com.example.integratedbackend.Repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    public boolean checkPassword(String username, String rawPassword) {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            return false;
        }
        return passwordEncoder.matches(rawPassword, user.getPasswordHash());

    }
}
