package com.example.integratedbackend.Controller.ControllerV3;

import com.example.integratedbackend.DTO.DTOV3.NewBoardDTO;
import com.example.integratedbackend.DTO.NewTaskDTOV2;
import com.example.integratedbackend.DTO.NewTaskReturnV2;
import com.example.integratedbackend.JWT.JwtUtil;
import com.example.integratedbackend.Kradankanban.kradankanbanV3.Entities.Boards;
import com.example.integratedbackend.Service.ServiceV3.BoardService;
import com.example.integratedbackend.Service.ServiceV3.UserServiceV3;
import io.jsonwebtoken.Claims;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    @Autowired
    private ModelMapper modelMapper;


        @GetMapping("")
        public ResponseEntity<Object> getBoardByUserId(@RequestHeader("Authorization") String authHeader) {
            if (authHeader != null && authHeader.startsWith("Bearer ")) {
                String jwt = authHeader.substring(7);
                String userId = jwtUtil.extractClaim(jwt, Claims::getSubject);
                if (userId != null) {
                    List<Boards> boards = boardService.getBoardByUserId(userId);
                    return ResponseEntity.ok(boards);
                }
            }
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Authorization Error");
        }

        @PostMapping("")
        public ResponseEntity<Object> createBoard(@RequestHeader("Authorization") String authHeader,@Valid @RequestBody String newBoard) {
            if (authHeader != null && authHeader.startsWith("Bearer ")) {
                String jwt = authHeader.substring(7);
                String userId = jwtUtil.extractClaim(jwt, Claims::getSubject);
                if (userId != null) {
                    Boards board = boardService.createBoard(userId, newBoard);
                    return ResponseEntity.ok(board);
                }
            }
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Authorization Error");

        }
    }

