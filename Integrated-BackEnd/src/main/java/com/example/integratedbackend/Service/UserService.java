package com.example.integratedbackend.Service;

import com.example.integratedbackend.Entities.User;
import com.example.integratedbackend.Repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.nio.file.attribute.UserPrincipal;
import java.util.ArrayList;
import java.util.Optional;

@Service
public class UserService implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    public boolean isValidUsername(String username){
        return username != null && username.matches("^[a-zA-Z0-9._-]{3,20}$");
    }

    public boolean isValidPassword(String rawPassword){
        return rawPassword != null && rawPassword.length() <= 14;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("User not found");
        }
        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPasswordHash(), new ArrayList<>());
    }

    public boolean checkPassword(String username, String rawPassword) {
        if(!isValidUsername(username) || !isValidPassword(rawPassword)){
            return false;
        }
        User user = userRepository.findByUsername(username);
        if (user == null) {
            return false;
        }
        return passwordEncoder.matches(rawPassword, user.getPasswordHash());
    }
}