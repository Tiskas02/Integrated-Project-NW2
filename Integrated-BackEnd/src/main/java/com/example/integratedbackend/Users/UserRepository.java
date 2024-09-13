package com.example.integratedbackend.Users;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;


@Repository

public interface UserRepository extends JpaRepository<User, String> {
    User findByUsername(String username);
}
