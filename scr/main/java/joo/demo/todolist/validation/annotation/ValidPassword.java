package joo.demo.todolist.validation.annotation;

import javax.validation.Constraint;
import javax.validation.Payload;
import joo.demo.todolist.validation.PasswordValidator;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({ FIELD })
@Retention(RUNTIME)
@Constraint(validatedBy = PasswordValidator.class)
@Documented
public @interface ValidPassword {

    String message() default "invalid password!";

    Class<?>[] groups() default { };

    Class<? extends Payload>[] payload() default { };
}
