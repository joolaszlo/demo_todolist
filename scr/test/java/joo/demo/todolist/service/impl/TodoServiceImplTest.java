package joo.demo.todolist.service.impl;

import joo.demo.todolist.domain.PriorityType;
import joo.demo.todolist.domain.Profile;
import joo.demo.todolist.domain.Todo;
import joo.demo.todolist.dto.TodoCreationDto;
import joo.demo.todolist.dto.TodoDto;
import joo.demo.todolist.error.exception.TodoException;
import joo.demo.todolist.repository.ProfileRepository;
import joo.demo.todolist.repository.TodoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TodoServiceImplTest {

    @Mock
    private TodoRepository todoRepository;
    @Mock
    private ProfileRepository profileRepository;

    private TodoServiceImpl underTest;

    @BeforeEach
    void setUp() {
        underTest = new TodoServiceImpl(todoRepository, profileRepository);
    }

    @Test
    void createTodo() {
        TodoCreationDto todoCreationDto = new TodoCreationDto("Task", null, LocalDateTime.now().plusDays(1), "test@email.com");
        Profile profile = Profile.builder().email("test@email.com").build();
        when(profileRepository.findProfileByEmail("test@email.com")).thenReturn(Optional.ofNullable(profile));
        ArgumentCaptor<Todo> todoArgumentCaptor = ArgumentCaptor.forClass(Todo.class);

        underTest.createTodo(todoCreationDto);

        verify(todoRepository).save(todoArgumentCaptor.capture());
        Todo capturedTodo = todoArgumentCaptor.getValue();
        assertEquals("Task", capturedTodo.getTask());
        assertEquals(false, capturedTodo.getDone());
        assertEquals(PriorityType.DEFAULT, capturedTodo.getPriorityType());
        assertEquals(todoCreationDto.getFinishDate(), capturedTodo.getFinishDate());
        assertEquals("test@email.com", capturedTodo.getProfile().getEmail());
    }

    @Test
    void getAllMyTodo() {
        Profile profile = Profile.builder().email("test@email.com").build();
        Todo todo = Todo.builder().id(1L).profile(profile).build();
        Todo todo2 = Todo.builder().id(2L).profile(profile).build();
        when(todoRepository.findTodosByProfile_EmailOrderByCreatedDate("test@email.com")).thenReturn(List.of(todo, todo2));

        List<TodoDto> result = underTest.getAllMyTodo("test@email.com");

        assertEquals(2, result.size());
    }

    @Test
    void editTodos() {
        TodoDto todoDto = new TodoDto(1L, "New task", false, PriorityType.HIGH, LocalDateTime.now().plusDays(1), LocalDateTime.now().plusDays(2));
        Todo todo = Todo.builder().id(1L).task("Old task").build();
        ArgumentCaptor<Todo> todoArgumentCaptor = ArgumentCaptor.forClass(Todo.class);
        when(todoRepository.findById(1L)).thenReturn(Optional.of(todo));
        when(todoRepository.save(todoArgumentCaptor.capture())).thenReturn(todo);

        underTest.editTodos(todoDto);

        Todo capturedTodo = todoArgumentCaptor.getValue();
        assertEquals("New task", capturedTodo.getTask());
        assertEquals(todoDto.getFinishDate(), capturedTodo.getFinishDate());
    }

    @Test
    void deleteMyTodo() {
        Profile profile = Profile.builder().id(2L).email("test@email.com").build();
        Todo todo = Todo.builder().id(1L).profile(profile).build();
        when(profileRepository.findProfileByEmail("test@email.com")).thenReturn(Optional.ofNullable(profile));
        when(todoRepository.findById(1L)).thenReturn(Optional.of(todo));

        underTest.deleteMyTodo(1L, "test@email.com");

        verify(todoRepository).delete(todo);
    }

    @Test
    void deleteMyTodoNotYour() {
        Profile profileToDto = Profile.builder().id(2L).email("test@email.com").build();
        Profile profile = Profile.builder().id(3L).email("profile@email.com").build();
        Todo todo = Todo.builder().id(1L).profile(profileToDto).build();
        when(profileRepository.findProfileByEmail("profile@email.com")).thenReturn(Optional.ofNullable(profile));
        when(todoRepository.findById(1L)).thenReturn(Optional.of(todo));

        try {
            underTest.deleteMyTodo(1L, "profile@email.com");
        } catch (TodoException e) {
            assertEquals("it's not your todo", e.getErrorMessage());
        }

        verify(todoRepository, never()).delete(todo);
    }
}