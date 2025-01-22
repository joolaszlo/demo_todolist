package joo.demo.todolist.controller;

import javax.validation.Valid;
import joo.demo.todolist.dto.TodoCreationDto;
import joo.demo.todolist.dto.TodoDto;
import joo.demo.todolist.service.TodoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static joo.demo.todolist.util.ProfileInformation.loggedProfile;


@RestController
@RequestMapping("/todos")
public class TodoController {

    private TodoService todoService;

    @Autowired
    public TodoController(TodoService todoService) {
        this.todoService = todoService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void createTodo(@RequestBody @Valid TodoCreationDto todoCreationDto) {
        todoCreationDto.setLoggedProfile(loggedProfile());
        todoService.createTodo(todoCreationDto);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.FOUND)
    public List<TodoDto> getAllMyTodo() {

        return todoService.getAllMyTodo(loggedProfile());
    }

    @PutMapping
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void editMyTodo(@RequestBody TodoDto todoDto) {
        todoService.editTodos(todoDto);
    }

    @DeleteMapping(value = "/delete")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void deleteMyTodo(@RequestParam(name = "todoid") Long todoId) {
        todoService.deleteMyTodo(todoId, loggedProfile());
    }

}
