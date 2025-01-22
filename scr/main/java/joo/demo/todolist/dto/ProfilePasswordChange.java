package joo.demo.todolist.dto;

import joo.demo.todolist.validation.annotation.ValidPassword;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class ProfilePasswordChange {

    @ValidPassword
    private String newPassword;
    private String token;

}
