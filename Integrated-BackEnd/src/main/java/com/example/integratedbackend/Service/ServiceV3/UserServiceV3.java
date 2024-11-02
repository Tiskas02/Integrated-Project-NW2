package com.example.integratedbackend.Service.ServiceV3;

import com.example.integratedbackend.Kradankanban.kradankanbanV3.Entities.Users;
import com.example.integratedbackend.Kradankanban.kradankanbanV3.Repositories.UsersRepositoriesV3;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceV3 {
    @Autowired
    UsersRepositoriesV3 usersRepositoriesV3;

    private List<Users> findUserByUsername(String username) {
        System.out.println(usersRepositoriesV3.findAllByUsername(username));
        return usersRepositoriesV3.findAllByUsername(username);
    }


}
