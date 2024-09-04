package com.example.integratedbackend.DTO;

import lombok.Data;

@Data
public class DetailBoardDTO {

    private Integer id;
    private String name;
    private OwnerDTO owner;

    @Data
    public static class OwnerDTO {
        private Integer oid;
        private String name;
    }
}
