package joo.demo.todolist.config;

import org.springframework.stereotype.Component;

@Component
public class ApplicationConfiguration {

    public static boolean REGISTRATION_OPEN;
    public static String SITE_URL;
    public static String EMAIL_REGISTRATION_BODY;
    public static String EMAIL_PASSWORD_RESET_BODY;

    private ApplicationConfiguration() {
        REGISTRATION_OPEN = true;
        SITE_URL = "http://localhost:8080/";
        EMAIL_REGISTRATION_BODY = "Click here to confirm your registration: ";
        EMAIL_PASSWORD_RESET_BODY = "Click here to confirm, you want to change your password: ";

    }
}
