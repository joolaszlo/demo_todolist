package joo.demo.todolist.dto;

import joo.demo.todolist.validation.annotation.UniqueUsername;
import joo.demo.todolist.validation.annotation.ValidPassword;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;


@AllArgsConstructor
@Builder
@Data

public class ProfileEditDto {

    @UniqueUsername
    private String username;
    private String loggedProfile;
    private String userIconColor;
    private String currentPassword;
    @ValidPassword
    private String newPassword;

}
