package joo.demo.todolist.repository;

import joo.demo.todolist.domain.Todo;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TodoRepository extends CrudRepository<Todo,Long> {

    List<Todo> findTodosByProfile_EmailOrderByCreatedDate(String email);
}
