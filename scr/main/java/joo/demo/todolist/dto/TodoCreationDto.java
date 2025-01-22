package joo.demo.todolist.dto;

import joo.demo.todolist.domain.PriorityType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@AllArgsConstructor
@Builder
@Data
public class TodoCreationDto {

    private String task;
    private PriorityType priorityType;
    private LocalDateTime finishDate;
    private String loggedProfile;
}
