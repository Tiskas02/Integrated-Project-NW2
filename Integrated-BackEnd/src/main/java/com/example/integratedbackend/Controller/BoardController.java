package com.example.integratedbackend.Controller;

import com.example.integratedbackend.DTO.DetailBoardDTO;
import com.example.integratedbackend.Kradankanban.Board;
import com.example.integratedbackend.Service.BoardService;
import com.example.integratedbackend.Service.ListMapper;
import com.example.integratedbackend.Service.UserService;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v3/boards")
@CrossOrigin(origins = {"http://ip23nw2.sit.kmutt.ac.th", "http://intproj23.sit.kmutt.ac.th","*"})
public class BoardController {
    @Autowired
    private BoardService boardService;
    @Autowired
    private UserService userService;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private ListMapper listMapper;

    @GetMapping("")
    public ResponseEntity<Object> getAllBoards(){
        return ResponseEntity.ok(boardService.getAllBoards());
    }
    @GetMapping("{boardId}")
    public ResponseEntity<Object> getBoardById(@PathVariable String boardId){
        return ResponseEntity.ok(modelMapper.map(boardService.getBoardById(boardId), DetailBoardDTO.class));
    }

    @PostMapping("")
    public ResponseEntity<Object> createBoard(@Valid @RequestBody Board board){
        return ResponseEntity.status(HttpStatus.CREATED).body(modelMapper.map(createBoard(board), Board.class));
    }
}
