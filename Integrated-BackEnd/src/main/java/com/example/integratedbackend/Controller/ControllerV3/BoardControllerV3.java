package com.example.integratedbackend.Controller.ControllerV3;

import com.example.integratedbackend.JWT.JwtUtil;
import com.example.integratedbackend.Kradankanban.kradankanbanV3.Entities.Boards;
import com.example.integratedbackend.Service.ServiceV3.BoardService;
import com.example.integratedbackend.Service.ServiceV3.UserServiceV3;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/board")
@CrossOrigin(origins = {"http://ip23nw2.sit.kmutt.ac.th", "http://intproj23.sit.kmutt.ac.th","*"})
public class BoardControllerV3 {
    @Autowired
    private BoardService boardService;
    @Autowired
    private UserServiceV3 UserService;
    @Autowired
    private JwtUtil jwtUtil;


        @GetMapping("")
        public Boards getBoardByUserId(@RequestHeader("Authorization") String authHeader) {
            if (authHeader != null && authHeader.startsWith("Bearer ")) {
                System.out.println("in");
                String jwt = authHeader.substring(7);
                // Parse and use the JWT as needed
                String userId = jwtUtil.extractClaim(jwt, Claims::getSubject);
                if (userId != null) {
                    Boards boards = boardService.getBoardByUserId(userId);
                    return boards;
                }
            }
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Authorization Error");
        }
    }

