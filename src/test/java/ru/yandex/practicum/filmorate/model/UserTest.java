package ru.yandex.practicum.filmorate.model;

import org.junit.jupiter.api.Test;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.ValidatorFactory;
import javax.validation.Validator;
import java.time.LocalDate;
import java.util.Set;
import static org.junit.jupiter.api.Assertions.*;

class UserTest {
    private static final Validator validator;
    static {
        ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
        validator =validatorFactory.usingContext().getValidator();
    }
    @Test
    void validEmptyLogin(){
        User user = User.builder()
                .name("name")
                .email("mail@mail.ru")
                .birthday(LocalDate.of(2002, 12, 12))
                .login("")
                .build();

        Set<ConstraintViolation<User>> violations;

        violations = validator.validate(user);
        assertEquals(1,violations.size(),"Invalid login");
    }
    @Test
    void validBlankLogin(){
        User user = User.builder()
                .name("name")
                .email("mail@mail.ru")
                .birthday(LocalDate.of(2002, 12, 12))
                .login(" ")
                .build();

        Set<ConstraintViolation<User>> violations;
        violations = validator.validate(user);
        System.out.println(violations);
        assertEquals(1,violations.size(),"Invalid login");
    }
    @Test
    void validBirthday(){
        User user = User.builder()
                .name("name")
                .email("mail@mail.ru")
                .birthday(LocalDate.of(3002, 12, 12))
                .login("login")
                .build();
        Set<ConstraintViolation<User>> violations;
        violations = validator.validate(user);
        assertEquals(1,violations.size(),"Invalid birthday");
    }
    @Test
    void validEmail(){
        User user = User.builder()
                .name("name")
                .email("mailmail@.ru")
                .birthday(LocalDate.of(2002, 12, 12))
                .login("login")
                .build();

        Set<ConstraintViolation<User>> violations;
        violations = validator.validate(user);
        assertEquals(1,violations.size(),"Invalid email");
    }
    @Test
    void validLogin(){
        User user = User.builder()
                .name("name")
                .email("mail@mail.ru")
                .birthday(LocalDate.of(2002, 12, 12))
                .login("log in")
                .build();

        Set<ConstraintViolation<User>> violations;
        violations = validator.validate(user);
        assertEquals(1,violations.size(),"Invalid login");
    }

}