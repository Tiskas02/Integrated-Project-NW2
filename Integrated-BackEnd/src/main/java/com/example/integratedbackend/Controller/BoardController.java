package com.example.integratedbackend.Controller;

import com.example.integratedbackend.DTO.BoardNameDTO;
import com.example.integratedbackend.DTO.DetailBoardDTO;
import com.example.integratedbackend.JWT.JwtUtil;
import com.example.integratedbackend.Service.BoardService;
import com.example.integratedbackend.Service.ListMapper;
import com.example.integratedbackend.Service.UserService;
import io.jsonwebtoken.Claims;
import jakarta.validation.Valid;
import org.apache.coyote.BadRequestException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.crypto.SecretKey;
import javax.xml.bind.annotation.XmlType;
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
    @Autowired
    private JwtUtil jwtUtil;
    @Value("${jwt.secret}")
    private String SECRET_KEY;

    //Board
    @GetMapping("/{boardId}")
    public DetailBoardDTO getBoardById(@PathVariable String boardId){
        return boardService.getBoardById(boardId);
    }

    @GetMapping("")
    public DetailBoardDTO getBoardByUserId(@RequestHeader("Authorization") String token){
        if (token != null && token.startsWith("Bearer ")) {
            String jwt = token.substring(7);
            String userId = jwtUtil.extractClaim(token, Claims::getSubject);
            return boardService.getBoardByUserId(userId);
        } else {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Authorization Error");
        }
    }

    @PostMapping("")
    public DetailBoardDTO createBoard(@RequestHeader("Authorization") String token , @RequestBody @Valid BoardNameDTO boardNameDTO) throws BadRequestException {
        if (token != null && token.startsWith("Bearer ")) {
            String jwt = token.substring(7);
            String userId = jwtUtil.extractClaim(token, Claims::getSubject);
            return boardService.addBoard(userId, boardNameDTO.getBoardName());
        } else {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Authorization Error");
        }
    }

}
