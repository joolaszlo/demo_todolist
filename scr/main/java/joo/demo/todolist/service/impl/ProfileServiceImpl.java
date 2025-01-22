package joo.demo.todolist.service.impl;

import joo.demo.todolist.domain.Profile;
import joo.demo.todolist.dto.ProfileDto;
import joo.demo.todolist.dto.ProfileEditDto;
import joo.demo.todolist.dto.ProfilePasswordChange;
import joo.demo.todolist.dto.ProfileRegistrationDto;
import joo.demo.todolist.error.exception.ProfileException;
import joo.demo.todolist.principal.ProfileDetails;
import joo.demo.todolist.repository.ProfileRepository;
import joo.demo.todolist.service.EmailSenderService;
import joo.demo.todolist.service.ProfileService;
import joo.demo.todolist.service.TokenService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import static joo.demo.todolist.util.PasswordMatchChecker.samePassword;

@Slf4j
@Service
public class ProfileServiceImpl implements ProfileService, UserDetailsService {

    private ProfileRepository profileRepository;
    private TokenService tokenService;
    private PasswordEncoder passwordEncoder;
    private EmailSenderService emailSenderService;

    @Autowired
    public ProfileServiceImpl(ProfileRepository profileRepository, TokenService tokenService, PasswordEncoder passwordEncoder, EmailSenderService emailSenderService) {
        this.profileRepository = profileRepository;
        this.tokenService = tokenService;
        this.passwordEncoder = passwordEncoder;
        this.emailSenderService = emailSenderService;
    }

    @Override
    public void registerUser(ProfileRegistrationDto profileRegistrationDto) {

        profileRepository.save(Profile.builder()
                .username(profileRegistrationDto.getUsername())
                .email(profileRegistrationDto.getEmail())
                .password(passwordEncoder.encode(profileRegistrationDto.getPassword()))
                .userIconColor("CBFF00")
                .enabled(true)
                .confirmed(false)
                .roles("ROLE_USER")
                .build());

        emailSenderService.sendRegistrationEmail(tokenService.createToken(profileRegistrationDto.getEmail()));
    }

    @Override
    public void registerOauth2User(OAuth2User oAuth2User) {
        Profile profile = profileRepository.findProfileByEmail(oAuth2User.getAttribute("email")).orElse(null);

        if (profile != null) {
            throw new ProfileException("oauth2 registration", "this email already registered;");
        }

        profileRepository.save(Profile.builder()
                .username(oAuth2User.getAttribute("name"))
                .email(oAuth2User.getAttribute("email"))
                .password(null)
                .userIconColor("CBFF00")
                .enabled(true)
                .confirmed(true)
                .roles("ROLE_USER")
                .build());
    }

    @Override
    public void forgetPassword(String email) {
        profileRepository.findProfileByEmail(email).orElseThrow(() -> new ProfileException("forget password", "this email not registered;"));
        emailSenderService.sendPasswordResetEmail(tokenService.createToken(email));
    }

    @Override
    public void changePassword(ProfilePasswordChange profilePasswordChange) {
        String email = tokenService.getProfile(profilePasswordChange.getToken());
        Profile profile = profileRepository.findProfileByEmail(email).orElse(null);
        if (profile != null) {
            profile.setPassword(passwordEncoder.encode(profilePasswordChange.getNewPassword()));
            profileRepository.save(profile);
        }
    }

    @Override
    public void confirmRegistration(String token) {
        String email = tokenService.getProfile(token);
        Profile profile = profileRepository.findProfileByEmail(email).orElse(null);
        if (profile != null) {
            profile.setConfirmed(true);
            profileRepository.save(profile);
        }
    }

    @Override
    public ProfileDto getMyProfile(String email) {
        Profile p = profileRepository.findProfileByEmail(email).orElseThrow(() -> new ProfileException("profile", "you need to register;"));

        return ProfileDto.builder()
                .username(p.getUsername())
                .userIconColor(p.getUserIconColor())
                .email(p.getEmail())
                .confirmed(p.getConfirmed())
                .registrationDate(p.getRegistrationDate())
                .build();
    }

    @Override
    public void updateMyProfile(ProfileEditDto profileEditDto) {
        Profile profile = profileRepository.findProfileByEmail(profileEditDto.getLoggedProfile()).orElseThrow(() -> new ProfileException("profile", "you need to register;"));

        if (Boolean.TRUE.equals(samePassword(profileEditDto.getCurrentPassword(), profile.getPassword()))) {
            profile.setUsername(profileEditDto.getUsername());
            profile.setUserIconColor(profileEditDto.getUserIconColor());

            if (profileEditDto.getNewPassword() != null) {
                profile.setPassword(passwordEncoder.encode(profileEditDto.getNewPassword()));
            }
            profileRepository.save(profile);
        } else {
            throw new ProfileException("profile edit", "wrong password;");
        }
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Profile profile = profileRepository.findProfileByEmail(email).orElseThrow(() -> new ProfileException("Email", "wrong email"));
        return new ProfileDetails(profile);
    }
}
