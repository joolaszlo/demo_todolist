package joo.demo.todolist.service.impl;

import joo.demo.todolist.domain.Profile;
import joo.demo.todolist.dto.ProfileDto;
import joo.demo.todolist.dto.ProfileEditDto;
import joo.demo.todolist.dto.ProfilePasswordChange;
import joo.demo.todolist.dto.ProfileRegistrationDto;
import joo.demo.todolist.error.exception.ProfileException;
import joo.demo.todolist.repository.ProfileRepository;
import joo.demo.todolist.service.EmailSenderService;
import joo.demo.todolist.service.TokenService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProfileServiceImplTest {

    @Mock
    private ProfileRepository profileRepository;
    @Mock
    private TokenService tokenService;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private EmailSenderService emailSenderService;

    private ProfileServiceImpl underTest;

    @BeforeEach
    void setUp() {
        underTest = new ProfileServiceImpl(profileRepository, tokenService, passwordEncoder, emailSenderService);
    }

    @Test
    void registerUser() {
        ProfileRegistrationDto profileRegistrationDto = new ProfileRegistrationDto("username", "email", "password");
        when(passwordEncoder.encode(any())).thenReturn("encodedPassword");
        ArgumentCaptor<Profile> profileArgumentCaptor = ArgumentCaptor.forClass(Profile.class);

        underTest.registerUser(profileRegistrationDto);

        verify(profileRepository).save(profileArgumentCaptor.capture());
        verify(emailSenderService).sendRegistrationEmail(any());
        assertEquals("username", profileArgumentCaptor.getValue().getUsername());
        assertEquals("encodedPassword", profileArgumentCaptor.getValue().getPassword());
    }

    @Test
    void registerOauth2User() {
        OAuth2User oAuth2User = new DefaultOAuth2User(null, Collections.singletonMap("email", "oauth@email.com"), "email");

        underTest.registerOauth2User(oAuth2User);

        verify(profileRepository).save(any());
    }

    @Test
    void registerOauth2UserWithRegisteredEmail() {

        OAuth2User oAuth2User = new DefaultOAuth2User(null, Collections.singletonMap("email", "oauth@email.com"), "email");
        when(profileRepository.findProfileByEmail("oauth@email.com")).thenReturn(Optional.of(new Profile()));
        try {
            underTest.registerOauth2User(oAuth2User);

        } catch (ProfileException e) {
            assertEquals("this email already registered;", e.getErrorMessage());

            verify(profileRepository, never()).save(any());
        }
    }

    @Test
    void forgetPassword() {
        Profile profile = Profile.builder().email("profile@email.com").build();
        when(profileRepository.findProfileByEmail("profile@email.com")).thenReturn(Optional.of(profile));

        underTest.forgetPassword("profile@email.com");

        verify(emailSenderService).sendPasswordResetEmail(any());
        verify(tokenService).createToken(any());
    }

    @Test
    void forgetPasswordNotRegistered() {
        when(profileRepository.findProfileByEmail("profile@email.com")).thenReturn(Optional.empty());

        try {
            underTest.forgetPassword("profile@email.com");

        } catch (ProfileException e) {
            assertEquals("this email not registered;", e.getErrorMessage());
        }
        verify(emailSenderService, never()).sendPasswordResetEmail(any());
        verify(tokenService, never()).createToken(any());
    }

    @Test
    void changePassword() {
        ProfilePasswordChange profilePasswordChange = new ProfilePasswordChange("token", "password");
        ArgumentCaptor<Profile> profileArgumentCaptor = ArgumentCaptor.forClass(Profile.class);
        when(tokenService.getProfile(profilePasswordChange.getToken())).thenReturn("profile@email.com");
        when(profileRepository.findProfileByEmail("profile@email.com")).thenReturn(Optional.of(Profile.builder().email("profile@email.com").password("password").build()));
        when(passwordEncoder.encode(any())).thenReturn("encodedPassword");

        underTest.changePassword(profilePasswordChange);

        verify(profileRepository).save(profileArgumentCaptor.capture());
        assertEquals("encodedPassword", profileArgumentCaptor.getValue().getPassword());
    }

    @Test
    void confirmRegistration() {
        Profile profile = Profile.builder().email("profile@email.com").password("password").confirmed(false).build();
        when(tokenService.getProfile("token")).thenReturn("profile@email.com");
        when(profileRepository.findProfileByEmail("profile@email.com")).thenReturn(Optional.of(profile));
        ArgumentCaptor<Profile> profileArgumentCaptor = ArgumentCaptor.forClass(Profile.class);

        underTest.confirmRegistration("token");

        verify(profileRepository).save(profileArgumentCaptor.capture());
        assertTrue(profileArgumentCaptor.getValue().getConfirmed());
    }

    @Test
    void getMyProfile() {
        Profile profile = Profile.builder().email("profile@email.com").build();
        when(profileRepository.findProfileByEmail("profile@email.com")).thenReturn(Optional.of(profile));

        ProfileDto result = underTest.getMyProfile("profile@email.com");

        assertEquals("profile@email.com", result.getEmail());
    }

    @Test
    void updateMyProfile() {
        PasswordEncoder passwordEncoderTrue = new BCryptPasswordEncoder();
        Profile profile = Profile.builder().username("username").password(passwordEncoderTrue.encode("password")).email("profile@email.com").build();
        ProfileEditDto profileEditDto = new ProfileEditDto("newUsername", "profile@email.com", null, "password", null);
        ArgumentCaptor<Profile> profileArgumentCaptor = ArgumentCaptor.forClass(Profile.class);
        when(profileRepository.findProfileByEmail("profile@email.com")).thenReturn(Optional.of(profile));


        underTest.updateMyProfile(profileEditDto);

        verify(profileRepository).save(profileArgumentCaptor.capture());
        assertEquals("newUsername", profileArgumentCaptor.getValue().getUsername());
    }
}