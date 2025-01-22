package joo.demo.todolist.validation;

import joo.demo.todolist.repository.ProfileRepository;
import joo.demo.todolist.validation.annotation.UniqueEmail;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class UniqueEmailValidator implements ConstraintValidator<UniqueEmail, String> {

    private ProfileRepository profileRepository;
    @Autowired
    public UniqueEmailValidator(ProfileRepository profileRepository) {
        this.profileRepository = profileRepository;
    }

    @Override
    public void initialize(UniqueEmail constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }
    @Override
    public boolean isValid(String email, ConstraintValidatorContext constraintValidatorContext) {
        return (profileRepository.findProfileByEmail(email).isEmpty());
    }
}
