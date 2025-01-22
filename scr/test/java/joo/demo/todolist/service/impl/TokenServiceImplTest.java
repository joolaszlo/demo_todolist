package joo.demo.todolist.service.impl;

import joo.demo.todolist.domain.Token;
import joo.demo.todolist.repository.TokenRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TokenServiceImplTest {

    @Mock
    private TokenRepository tokenRepository;

    private TokenServiceImpl underTest;

    @BeforeEach
    void setUp() {
        underTest = new TokenServiceImpl(tokenRepository);
    }

    @Test
    void createToken() {
        ArgumentCaptor<Token> tokenArgumentCaptor = ArgumentCaptor.forClass(Token.class);
        underTest.createToken("test@email.com");
        verify(tokenRepository).save(tokenArgumentCaptor.capture());
        Token capturedToken = tokenArgumentCaptor.getValue();
        assertEquals("test@email.com", capturedToken.getEmail());
    }

    @Test
    void createTokenButAlreadyHasAToken() {
        Token token = new Token();
        token.setEmail("test@email.com");
        token.setId(1L);
        when(tokenRepository.findTokenByEmail("test@email.com")).thenReturn(token);
        ArgumentCaptor<Token> tokenArgumentCaptor = ArgumentCaptor.forClass(Token.class);
        underTest.createToken("test@email.com");
        verify(tokenRepository).delete(tokenArgumentCaptor.capture());
        Token capturedToken = tokenArgumentCaptor.getValue();
        assertEquals("test@email.com", capturedToken.getEmail());
        assertEquals(1L, capturedToken.getId());
    }

    @Test
    void getProfile() {

        Token token = new Token();
        token.setEmail("test@email.com");
        token.setId(1L);
        when(tokenRepository.findTokenByToken("testtoken")).thenReturn(token);
        String profileEmail = underTest.getProfile("testtoken");
        assertEquals("test@email.com", profileEmail);
    }
}