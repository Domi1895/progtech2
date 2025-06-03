package hu.nye.progtech.todolist.ToDoList.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class LoginRequest {
    private String name;
    private String password;
}
