package com.example.integratedbackend.Service;

import com.example.integratedbackend.DTO.DetailBoardDTO;
import com.example.integratedbackend.ErrorHandle.ItemNotFoundException;
import com.example.integratedbackend.Kradankanban.Board;
import com.example.integratedbackend.Kradankanban.BoardRepositories;
import com.example.integratedbackend.Kradankanban.StatusRepositories;
import com.example.integratedbackend.Users.UserRepository;
import io.viascom.nanoid.NanoId;
import org.apache.coyote.BadRequestException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class BoardService {
    @Autowired
    private BoardRepositories boardRepositories;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private StatusRepositories statusRepositories;
    @Autowired
    ModelMapper modelMapper;
    @Autowired
    private ListMapper listMapper;

    public DetailBoardDTO getBoardByUserId(String userId) {
        Board board = boardRepositories.getReferenceById(userId);
        if (board == null) {
            throw new ItemNotFoundException(HttpStatus.FORBIDDEN, "Board with id " + userId + " not found");
        }
        DetailBoardDTO.OwnerDTO ownerDTO = new DetailBoardDTO.OwnerDTO();
        ownerDTO.setOid(Integer.valueOf(board.getUserId()));
        ownerDTO.setName(userRepository.findById(board.getUserId()).orElseThrow(() -> new ItemNotFoundException(HttpStatus.FORBIDDEN, "User with id " + board.getUserId() + " not found")).getName());

        DetailBoardDTO boardDTO = modelMapper.map(board, DetailBoardDTO.class);
        boardDTO.setId(Integer.valueOf(board.getBoardId()));
        boardDTO.setName(board.getBoardName());
        boardDTO.setOwner(ownerDTO);

        return boardDTO;
    }

    public DetailBoardDTO getBoardById(String boardId) {
        Board board = boardRepositories.findById(boardId).orElseThrow(() -> new ItemNotFoundException(HttpStatus.FORBIDDEN, "Board with id " + boardId + " not found"));
        DetailBoardDTO.OwnerDTO ownerDTO = new DetailBoardDTO.OwnerDTO();
        ownerDTO.setOid(Integer.valueOf(board.getUserId()));
        ownerDTO.setName(userRepository.findById(board.getUserId()).orElseThrow(() -> new ItemNotFoundException(HttpStatus.FORBIDDEN, "User with id " + board.getUserId() + " not found")).getName());

        DetailBoardDTO boardDTO = modelMapper.map(board, DetailBoardDTO.class);
        boardDTO.setId(Integer.valueOf(board.getBoardId()));
        boardDTO.setName(board.getBoardName());
        boardDTO.setOwner(ownerDTO);

        return boardDTO;
    }

    @Transactional
    public DetailBoardDTO addBoard(String userId, String boardName) throws BadRequestException {

        Board existBoard = boardRepositories.getReferenceById(userId);
        if (existBoard != null) {
            throw new BadRequestException("Board with id " + userId + " already exists");
        }

        Board newBoard = modelMapper.map(new Board(), Board.class);
        newBoard.setUserId(userId);
        newBoard.setBoardName(boardName);
        newBoard.setBoardId(NanoId.generate(10));

        DetailBoardDTO.OwnerDTO ownerDTO = new DetailBoardDTO.OwnerDTO();
        ownerDTO.setOid(Integer.valueOf(userId));
        ownerDTO.setName(userRepository.findById(userId).orElseThrow(() -> new ItemNotFoundException(HttpStatus.FORBIDDEN, "User with id " + userId + " not found")).getName());

        DetailBoardDTO boardDTO = modelMapper.map(existBoard, DetailBoardDTO.class);
        boardDTO.setId(Integer.valueOf(newBoard.getBoardId()));
        boardDTO.setName(newBoard.getBoardName());
        boardDTO.setOwner(ownerDTO);

        boardRepositories.save(newBoard);
        return boardDTO;
    }
}
