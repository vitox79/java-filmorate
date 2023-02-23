package ru.yandex.practicum.filmorate.model;

import jakarta.validation.ConstraintViolation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.PropertyEditorRegistry;
import org.springframework.stereotype.Repository;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.repository.UserRepository;
import ru.yandex.practicum.filmorate.service.ValidateService;
import ru.yandex.practicum.filmorate.service.ValidationException;

import java.beans.PropertyEditor;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class UserControllerTest {

    private UserController userController;
    @BeforeEach
    @Test
    void emptyName(){
        ValidateService validateService = new ValidateService();
        User user = new User();
        user.setName(" ");
        user.setEmail("mail@mail.ru");
        user.setLogin("login");
        user.setBirthday(LocalDate.of(2000,10,10));
        validateService.validateUser(user);
        assertEquals(user.getLogin(),user.getName());
    }
    @Test
    void invalidLogin(){
        ValidateService validateService = new ValidateService();
        User user = new User();
        user.setName("name");
        user.setEmail("mail@mail.ru");
        user.setLogin("log in");
        user.setBirthday(LocalDate.of(2000,10,10));
        assertThrows(ValidationException.class,()->validateService.validateUser(user));
    }
    @Test
    void addUser(){
        UserRepository repository = new UserRepository();
        User user = new User();
        user.setName("name");
        user.setEmail("mail@mail.ru");
        user.setLogin("login");
        user.setBirthday(LocalDate.of(2000,10,10));
        repository.save(user);
        assertEquals(repository.getByID(user.getId()),user);
    }
    @Test
    void getUsers(){
        UserRepository repository = new UserRepository();
        User user = new User();
        user.setId(1);
        user.setName("name");
        user.setEmail("mail@mail.ru");
        user.setLogin("login");
        user.setBirthday(LocalDate.of(2000,10,10));
        repository.save(user);
        User user2 = new User();
        user2.setId(2);
        user2.setName("name2");
        user2.setEmail("mail2@mail.ru");
        user2.setLogin("login2");
        user2.setBirthday(LocalDate.of(2000,10,10));
        repository.save(user2);
        assertEquals(repository.getAll().size(),2);
        assertEquals(repository.getByID(2),user2);
    }


}