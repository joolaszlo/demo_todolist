package joo.demo.todolist.error;

import joo.demo.todolist.error.exception.ProfileException;
import joo.demo.todolist.error.exception.TodoException;
import joo.demo.todolist.error.response.ProfileExceptionResponse;
import joo.demo.todolist.error.response.TodoExceptionResponse;
import joo.demo.todolist.error.response.ValidationError;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.ArrayList;
import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public List<ValidationError> handleValidationException(MethodArgumentNotValidException exception) {
        List<ValidationError> validationErrors = new ArrayList<>();
        for (FieldError fieldError : exception.getBindingResult().getFieldErrors()) {
            ValidationError validationError = new ValidationError(fieldError.getField(), fieldError.getDefaultMessage());
            validationErrors.add(validationError);
        }
        return validationErrors;
    }

    @ExceptionHandler(ProfileException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    ProfileExceptionResponse onProfileError(ProfileException e) {
        return new ProfileExceptionResponse(e.getField(), e.getErrorMessage());
    }

    @ExceptionHandler(TodoException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    TodoExceptionResponse onTodoError(TodoException e) {
        return new TodoExceptionResponse(e.getField(), e.getErrorMessage());
    }
}
