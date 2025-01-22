package joo.demo.todolist.service.impl;

import joo.demo.todolist.domain.Token;
import joo.demo.todolist.repository.TokenRepository;
import joo.demo.todolist.service.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;

@Service
public class TokenServiceImpl implements TokenService {

    private TokenRepository tokenRepository;

    @Autowired
    public TokenServiceImpl(TokenRepository tokenRepository) {
        this.tokenRepository = tokenRepository;
    }


    @Override
    public Token createToken(String email) {
        String tokenString = makeToken();

        Token token = tokenRepository.findTokenByEmail(email);
        if (token != null) {
            tokenRepository.delete(token);
        }

        return tokenRepository.save(Token.builder().token(tokenString).email(email).build());

    }

    @Override
    public String getProfile(String token) {
        Token findToken = tokenRepository.findTokenByToken(token);
        tokenRepository.delete(findToken);
        return findToken.getEmail();
    }


    private String makeToken() {
        String characters = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        StringBuilder token = new StringBuilder(32);
        SecureRandom random = new SecureRandom();

        for (int i = 0; i < 32; i++) {
            token.append(characters.charAt(random.nextInt(characters.length())));

        }

        return token.toString();
    }
}
