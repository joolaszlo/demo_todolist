package joo.demo.todolist.util;

import joo.demo.todolist.error.exception.ProfileException;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

public class ProfileInformation {

    @Bean
    public static String loggedProfile() {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

            if (authentication.getPrincipal() instanceof UserDetails) {

                return ((UserDetails) authentication.getPrincipal()).getUsername();

            } else {

                OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();

                return oAuth2User.getAttribute("email");
            }
        } catch (Exception e) {
            throw new ProfileException("login", "you need to login first");
        }
    }

}
