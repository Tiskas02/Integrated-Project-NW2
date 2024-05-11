package com.example.integratedbackend;

import com.example.integratedbackend.Service.ListMapper;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApplicationConfig {
    @Bean
    public ModelMapper modelMapper(){
        return new ModelMapper();
    }
    @Bean
    public ListMapper listMapper(){
        return ListMapper.getInstance();
    }
}
