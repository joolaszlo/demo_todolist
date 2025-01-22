package joo.demo.todolist.service;

import joo.demo.todolist.dto.TodoCreationDto;
import joo.demo.todolist.dto.TodoDto;

import java.util.List;

public interface TodoService {
    void createTodo(TodoCreationDto todoCreationDto);

    List<TodoDto> getAllMyTodo(String email);

    void editTodos(TodoDto todoDto);

    void deleteMyTodo(Long todoId, String loggedInUserEmail);
}
