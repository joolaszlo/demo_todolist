package joo.demo.todolist.service;

import joo.demo.todolist.domain.Token;

public interface EmailSenderService {
    void sendRegistrationEmail(Token token);

    void sendPasswordResetEmail(Token token);
}
