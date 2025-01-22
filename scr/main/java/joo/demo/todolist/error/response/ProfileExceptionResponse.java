package joo.demo.todolist.error.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ProfileExceptionResponse {

    private String field;
    private String errorMessage;
}
