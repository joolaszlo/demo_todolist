package joo.demo.todolist.service;

import joo.demo.todolist.dto.ProfileDto;
import joo.demo.todolist.dto.ProfileEditDto;
import joo.demo.todolist.dto.ProfilePasswordChange;
import joo.demo.todolist.dto.ProfileRegistrationDto;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.List;

public interface ProfileService {
    void registerUser(ProfileRegistrationDto profileRegistrationDto);

    ProfileDto getMyProfile(String email);

    void updateMyProfile(ProfileEditDto profileEditDto);

    void registerOauth2User(OAuth2User profileDto);

    void forgetPassword(String email);

    void changePassword(ProfilePasswordChange profilePasswordChange);

    void confirmRegistration(String token);
}
