package joo.demo.todolist.repository;

import joo.demo.todolist.domain.Token;
import org.springframework.data.repository.CrudRepository;

public interface TokenRepository extends CrudRepository<Token, Long> {

    Token findTokenByEmail(String email);

    Token findTokenByToken(String token);
}
