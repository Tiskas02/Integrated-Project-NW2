package com.example.integratedbackend.Service;

import com.example.integratedbackend.ErrorHandle.ItemNotFoundException;
import com.example.integratedbackend.Kradankanban.Board;
import com.example.integratedbackend.Kradankanban.BoardRepositories;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BoardService {
    @Autowired
    private BoardRepositories boardRepositories;
    @Autowired
    ModelMapper modelMapper;
    @Autowired
    private ListMapper listMapper;

    public List<Board> getAllBoards(){
        return boardRepositories.findAll();
    }
    public Board getBoardById(String id) throws ItemNotFoundException {
        return boardRepositories.findById(id).orElseThrow(
                () -> new ItemNotFoundException(
                        "Board" + "" + id + "doesn't exist")
        );
    }
}
