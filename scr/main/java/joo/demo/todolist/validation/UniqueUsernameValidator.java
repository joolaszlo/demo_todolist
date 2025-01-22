package joo.demo.todolist.validation;

import joo.demo.todolist.repository.ProfileRepository;
import joo.demo.todolist.validation.annotation.UniqueUsername;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class UniqueUsernameValidator implements ConstraintValidator<UniqueUsername, String> {

    private ProfileRepository profileRepository;
    @Autowired
    public UniqueUsernameValidator(ProfileRepository profileRepository) {
        this.profileRepository = profileRepository;
    }

    @Override
    public void initialize(UniqueUsername constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String newUsername, ConstraintValidatorContext constraintValidatorContext) {
        return (profileRepository.findProfileByUsername(newUsername) == null);
    }
}
