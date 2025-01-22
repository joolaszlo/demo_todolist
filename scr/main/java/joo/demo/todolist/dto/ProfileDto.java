package joo.demo.todolist.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class ProfileDto {

    private Long id;
    private String username;
    private String email;
    private String userIconColor;
    private String password;
    private Boolean enabled = true;
    private Boolean confirmed = false;
    private String roles;
    private LocalDateTime registrationDate;
    private List<TodoDto> todoList;
}
