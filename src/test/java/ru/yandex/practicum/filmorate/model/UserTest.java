package ru.yandex.practicum.filmorate.model;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {
    private static Validator validator;
    static {
        ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.usingContext().getValidator();
    }
    @Test
    void validEmptyLogin(){
        User user = new User();
        Set<ConstraintViolation<User>> violations;
        user.setLogin("");
        violations = validator.validate(user);
        assertEquals(1,violations.size(),"Invalid login");
    }
    @Test
    void validBlankLogin(){
        User user = new User();
        Set<ConstraintViolation<User>> violations;
        user.setEmail("mail@mail.ru");
        user.setName("name");
        user.setLogin(" ");
        violations = validator.validate(user);
        assertEquals(1,violations.size(),"Invalid login");
    }
    @Test
    void validBirthday(){
        User user = new User();
        Set<ConstraintViolation<User>> violations;
        user.setEmail("mail@mail.ru");
        user.setName("name");
        user.setLogin("login");
        user.setBirthday(LocalDate.of(2100,10,10));
        violations = validator.validate(user);
        assertEquals(1,violations.size(),"Invalid birthday");
    }
    @Test
    void validEmail(){
        User user = new User();
        Set<ConstraintViolation<User>> violations;
        user.setEmail("mailmail.@ru");
        user.setName("name");
        user.setLogin("login");
        user.setBirthday(LocalDate.of(2000,10,10));
        violations = validator.validate(user);
        assertEquals(1,violations.size(),"Invalid email");
    }



}