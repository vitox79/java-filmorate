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