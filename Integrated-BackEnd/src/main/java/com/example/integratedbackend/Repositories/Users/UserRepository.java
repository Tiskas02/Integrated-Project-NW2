package com.example.integratedbackend.Repositories.Users;

import com.example.integratedbackend.Entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Integer> {
    User findByUsername(String username);
}
