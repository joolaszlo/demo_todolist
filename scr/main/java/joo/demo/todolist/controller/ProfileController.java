package joo.demo.todolist.controller;

import javax.validation.Valid;
import joo.demo.todolist.dto.ProfileDto;
import joo.demo.todolist.dto.ProfileEditDto;
import joo.demo.todolist.dto.ProfilePasswordChange;
import joo.demo.todolist.dto.ProfileRegistrationDto;
import joo.demo.todolist.error.exception.ProfileException;
import joo.demo.todolist.service.ProfileService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;

import static joo.demo.todolist.util.ProfileInformation.loggedProfile;


@Slf4j
@RestController
@RequestMapping()
public class ProfileController {

    private ProfileService profileService;

    @Autowired
    public ProfileController(ProfileService profileService) {
        this.profileService = profileService;
    }

    @PostMapping("/registration")
    @ResponseStatus(HttpStatus.CREATED)
    public void registerUser(@RequestBody @Valid ProfileRegistrationDto profileRegistrationDto) {
        profileService.registerUser(profileRegistrationDto);

    }

    @GetMapping(value = "/registration/oauth2")
    @ResponseStatus(HttpStatus.CREATED)
    public void createAccount() {

        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
            profileService.registerOauth2User(oAuth2User);
        } catch (Exception e) {
            throw new ProfileException("oauth2 registration", "you need to log in first with oauth2");
        }
    }

    @GetMapping(value = "/myprofile")
    @ResponseStatus(HttpStatus.OK)
    public ProfileDto getMyProfile() {

        return profileService.getMyProfile(loggedProfile());
    }

    @PutMapping(value = "/myprofile")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void editMyProfile(@RequestBody @Valid ProfileEditDto profileEditDto) {
        profileEditDto.setLoggedProfile(loggedProfile());
        profileService.updateMyProfile(profileEditDto);

    }

    @PostMapping(value = "/forgetpassword")
    @ResponseStatus(HttpStatus.OK)
    public void forgetPassword(@RequestParam("email") String email) {

        profileService.forgetPassword(email);

    }

    @PostMapping(value = "/changepassword")
    @ResponseStatus(HttpStatus.OK)
    public void changePassword(@RequestBody @Valid ProfilePasswordChange profilePasswordChange) {

        profileService.changePassword(profilePasswordChange);

    }

    @GetMapping(value = "/confirmregistration")
    @ResponseStatus(HttpStatus.OK)
    public void confirmRegistration(@RequestParam("token") String token) {

        profileService.confirmRegistration(token);

    }
}
