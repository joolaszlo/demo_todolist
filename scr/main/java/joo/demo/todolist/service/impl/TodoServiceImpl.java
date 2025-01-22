package joo.demo.todolist.service.impl;

import joo.demo.todolist.domain.PriorityType;
import joo.demo.todolist.domain.Profile;
import joo.demo.todolist.domain.Todo;
import joo.demo.todolist.dto.TodoCreationDto;
import joo.demo.todolist.dto.TodoDto;
import joo.demo.todolist.error.exception.ProfileException;
import joo.demo.todolist.error.exception.TodoException;
import joo.demo.todolist.repository.ProfileRepository;
import joo.demo.todolist.repository.TodoRepository;
import joo.demo.todolist.service.TodoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Slf4j
@Service
public class TodoServiceImpl implements TodoService {
    private TodoRepository todoRepository;
    private ProfileRepository profileRepository;

    @Autowired
    public TodoServiceImpl(TodoRepository todoRepository, ProfileRepository profileRepository) {
        this.todoRepository = todoRepository;
        this.profileRepository = profileRepository;
    }


    @Override
    public void createTodo(TodoCreationDto todoCreationDto) {
        Profile profile = profileRepository.findProfileByEmail(todoCreationDto.getLoggedProfile()).orElseThrow(() -> new ProfileException("create todo", "you need to register first"));
        if (todoCreationDto.getPriorityType() == null) {
            todoCreationDto.setPriorityType(PriorityType.DEFAULT);
        }

        todoRepository.save(Todo.builder()
                .task(todoCreationDto.getTask())
                .done(false)
                .priorityType(todoCreationDto.getPriorityType())
                .finishDate(todoCreationDto.getFinishDate())
                .profile(profile)
                .build());
    }

    @Override
    public List<TodoDto> getAllMyTodo(String email) {

        return StreamSupport.stream(todoRepository.findTodosByProfile_EmailOrderByCreatedDate(email).spliterator(), false).map(t -> TodoDto.builder()
                .id(t.getId())
                .task(t.getTask())
                .done(t.getDone())
                .priorityType(t.getPriorityType())
                .createdDate(t.getCreatedDate())
                .finishDate(t.getFinishDate())
                .build()).collect(Collectors.toList());
    }

    @Transactional
    @Override
    public void editTodos(TodoDto todoDto) {
        Todo todo = todoRepository.findById(todoDto.getId()).get();
        todo.setTask(todoDto.getTask());
        todo.setDone(todoDto.getDone());
        todo.setPriorityType(todoDto.getPriorityType());
        todo.setFinishDate(todoDto.getFinishDate());

        todoRepository.save(todo);
    }

    @Override
    public void deleteMyTodo(Long todoId, String email) {
        Profile profile = profileRepository.findProfileByEmail(email).orElseThrow(() -> new ProfileException("delete todo", "you need to register first"));
        Todo todo = todoRepository.findById(todoId).orElseThrow(() -> new TodoException("delete my todo", "wrong todo id"));

        if (todo.getProfile().getId().equals(profile.getId())) {
            todoRepository.delete(todo);
        } else {
            throw new TodoException("delete todo", "it's not your todo");
        }
    }
}
