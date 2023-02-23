package ru.yandex.practicum.filmorate.controller;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.repository.UserRepository;
import ru.yandex.practicum.filmorate.service.ValidationException;
import ru.yandex.practicum.filmorate.service.ValidateService;

import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class UserController {

    private  int count =1;
    final ValidateService validateService;
    final  UserRepository users = new UserRepository();

    public  UserController(ValidateService validateService){
        this.validateService = validateService;
    }
    private final static Logger log = LoggerFactory.getLogger(UserController.class);

    @PostMapping(value = "/users")
    public User addUser(@Valid @RequestBody   User user, BindingResult bindingResult)throws ValidationException {
        if (bindingResult.hasErrors()) {
            log.error("Invalid user data");
            throw new ValidationException("Invalid user data");
        }
        validateService.validateUser(user);
        user.setId(users.size()+1);
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
