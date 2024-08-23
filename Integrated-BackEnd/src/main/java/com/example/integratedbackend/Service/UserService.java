package com.example.integratedbackend.Service;

import com.example.integratedbackend.Users.User;
import com.example.integratedbackend.Users.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class UserService implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;

    public boolean isValidUsername(String username){
        return username != null && username.matches("^[a-zA-Z0-9._-]{3,50}$");
    }

    public boolean isValidPassword(String rawPassword){

        return rawPassword != null && rawPassword.length() <= 14;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(username));
    }

}
