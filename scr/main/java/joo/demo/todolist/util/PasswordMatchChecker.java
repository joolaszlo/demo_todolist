package joo.demo.todolist.util;

import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordMatchChecker {

    private static final BCryptPasswordEncoder passwordEcorder = new BCryptPasswordEncoder();

    private PasswordMatchChecker() {

    }

    @Bean
    public static Boolean samePassword(String rawPassword, String encodedPassword) {
        return passwordEcorder.matches(rawPassword, encodedPassword);
    }
}
