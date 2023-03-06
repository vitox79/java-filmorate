package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.repository.UserRepository;
import ru.yandex.practicum.filmorate.service.ValidationException;
import ru.yandex.practicum.filmorate.service.ValidateService;

import javax.validation.Valid;
import java.util.List;


@RestController
@Slf4j
public class UserController {

    final ValidateService validateService;
    final  UserRepository users = new UserRepository();
    
    public  UserController(ValidateService validateService){
        this.validateService = validateService;
    }

    @PostMapping(value = "/users")
    public User addUser(@Valid @RequestBody   User user, BindingResult bindingResult)throws ValidationException {
        if (bindingResult.hasErrors()) {
            log.error("Invalid user data");
            throw new ValidationException("Invalid user data");
        }
        validateService.validateUser(user);
        users.save( user);
        return user;
    }
    @PutMapping(value = "/users")

    public User updateUser(@Valid @RequestBody  User user, BindingResult bindingResult) throws ValidationException{
        if (bindingResult.hasErrors()) {
            log.error("Invalid user data");
            throw new ValidationException("Invalid user data");
        }
        validateService.validateUser(user);
        User currentUser = users.getByID(user.getId());
        if (currentUser == null) {
            String message  = "User id does not exist";
            log.error(message);
            throw new ValidationException(message);
        }

        currentUser.setEmail(user.getEmail());
        currentUser.setLogin(user.getLogin());
        currentUser.setName(user.getName());
        currentUser.setBirthday(user.getBirthday());
        return user;
    }
    @GetMapping("/users")
    public List<User> allUsers(){
        return users.getAll();
    }

}
