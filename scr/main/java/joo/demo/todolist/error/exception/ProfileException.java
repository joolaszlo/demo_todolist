package joo.demo.todolist.error.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;


@Getter
@AllArgsConstructor
public class ProfileException extends RuntimeException{

    private final String field;
    private final String errorMessage;
}
