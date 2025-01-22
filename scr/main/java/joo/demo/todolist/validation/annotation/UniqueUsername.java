package joo.demo.todolist.validation.annotation;

import javax.validation.Constraint;
import javax.validation.Payload;
import joo.demo.todolist.validation.UniqueUsernameValidator;

import java.lang.annotation.*;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.*;

@Target({FIELD, METHOD})
@Retention(RUNTIME)
@Constraint(validatedBy = UniqueUsernameValidator.class)
@Documented
public @interface UniqueUsername {

    String message() default "username already in use!";

    Class<?>[] groups() default { };

    Class<? extends Payload>[] payload() default { };
}
