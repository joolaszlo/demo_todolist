package joo.demo.todolist.dto;

import joo.demo.todolist.validation.annotation.UniqueEmail;
import joo.demo.todolist.validation.annotation.UniqueUsername;
import joo.demo.todolist.validation.annotation.ValidPassword;
import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.Email;

@AllArgsConstructor
@Data
public class ProfileRegistrationDto {

    @UniqueUsername
    private String username;
    @Email
    @UniqueEmail
    private String email;
    @ValidPassword
    private String password;
}
