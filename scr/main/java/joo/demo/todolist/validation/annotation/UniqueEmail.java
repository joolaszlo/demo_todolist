package joo.demo.todolist.validation.annotation;

import javax.validation.Constraint;
import javax.validation.Payload;
import joo.demo.todolist.validation.UniqueEmailValidator;

import java.lang.annotation.*;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.*;

@Target({FIELD, METHOD})
@Retention(RUNTIME)
@Constraint(validatedBy = UniqueEmailValidator.class)
@Documented
public @interface UniqueEmail {

    String message() default "this email already registered;";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}