package com.example.integratedbackend.DTO.DTOV3;

import com.example.integratedbackend.Kradankanban.kradankanbanV3.Entities.Collab;

import java.util.List;

public class CollabBoardResponse {
    private String userName;
    private List<Collab> collabs;

    // Constructor
    public CollabBoardResponse(String userName, List<Collab> collabs) {
        this.userName = userName;
        this.collabs = collabs;
    }

    // Getters and Setters
    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public List<Collab> getCollabs() {
        return collabs;
    }

    public void setCollabs(List<Collab> collabs) {
        this.collabs = collabs;
    }
}

