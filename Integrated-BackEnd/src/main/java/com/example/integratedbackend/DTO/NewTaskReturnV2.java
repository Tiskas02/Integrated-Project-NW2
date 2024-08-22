package com.example.integratedbackend.DTO;

import com.example.integratedbackend.Kradankanban.Status;
import lombok.Data;

@Data
public class NewTaskReturnV2 {
    private Integer id;
    private String Title;
    private String Assignees;
    private String Description;
    private Status status;
}
