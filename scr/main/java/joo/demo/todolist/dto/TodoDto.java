package joo.demo.todolist.dto;

import joo.demo.todolist.domain.PriorityType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@AllArgsConstructor
@Builder
@Data
public class TodoDto {

    private Long id;
    @NotBlank
    private String task;
    private Boolean done;
    private PriorityType priorityType = PriorityType.DEFAULT;
    private LocalDateTime createdDate;
    private LocalDateTime finishDate;

}
