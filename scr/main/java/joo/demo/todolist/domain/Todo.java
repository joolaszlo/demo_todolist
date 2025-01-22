package joo.demo.todolist.domain;

import javax.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@Entity
@Table(name = "todo")
public class Todo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "task")
    private String task;

    @Column(name = "done")
    private Boolean done;

    @Column(name = "priority")
    @Enumerated(EnumType.STRING)
    private PriorityType priorityType;

    @Column(name = "created_date", columnDefinition = "TIMESTAMP")
    @CreationTimestamp
    private LocalDateTime createdDate;

    @Column(name = "finish_date", columnDefinition = "TIMESTAMP")
    private LocalDateTime finishDate;

    @ManyToOne
    @JoinColumn(name = "profile_id")
    private Profile profile;

}
