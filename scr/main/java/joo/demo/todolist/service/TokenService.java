package joo.demo.todolist.service;


import joo.demo.todolist.domain.Token;

public interface TokenService {

    Token createToken(String email);

    String getProfile(String token);
}
