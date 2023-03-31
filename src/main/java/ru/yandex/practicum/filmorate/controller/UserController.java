package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserNotFoundException;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.service.ValidateService;
import ru.yandex.practicum.filmorate.service.ValidationException;
import ru.yandex.practicum.filmorate.strorage.InMemoryUserStorage;
import javax.validation.Valid;
import java.util.List;


@RestController
@Slf4j
public class UserController {

    private final ValidateService validateService;
    private final UserService userService;

    @Autowired
    public UserController(ValidateService validateService, UserService userService, InMemoryUserStorage users) {
        this.userService = userService;
        this.validateService = validateService;
    }

    @PostMapping(value = "/users")
    public User addUser(@Valid @RequestBody User user, BindingResult bindingResult) throws ValidationException {
        if (bindingResult.hasErrors()) {
            log.error("Invalid user data");
            throw new ValidationException("Invalid user data");
        }
        validateService.validateUser(user);
        userService.addUser(user);
        return user;
    }

    @PutMapping(value = "/users")
    public User updateUser(@Valid @RequestBody User user, BindingResult bindingResult) throws ValidationException {
        if (bindingResult.hasErrors()) {
            log.error("Invalid user data");
            throw new ValidationException("Invalid user data");
        }
        validateService.validateUser(user);
        User currentUser = userService.getByID(user.getId());
        if (currentUser == null) {
            String message = "User id does not exist";
            log.error(message);
            throw new UserNotFoundException(message);
        }

        currentUser.setEmail(user.getEmail());
        currentUser.setLogin(user.getLogin());
        currentUser.setName(user.getName());
        currentUser.setBirthday(user.getBirthday());
        return user;
    }

    @GetMapping("/users")
    public List<User> allUsers() {
        return userService.getAll();
    }

    @PutMapping("/users/{id}/friends/{friendId}")
    public void addFriend(@PathVariable int id, @PathVariable int friendId) {
        if ((userService.getByID(id) == null) || (userService.getByID(friendId) == null)) {
            String message = "User id does not exist";
            log.error(message);
            throw new UserNotFoundException(message);
        }
        userService.addFriend(userService.getByID(id), userService.getByID(friendId));
    }

    @DeleteMapping("/users/{id}/friends/{friendId}")
    public void removeFriend(@PathVariable int id, @PathVariable int friendId) {
        if ((userService.getByID(id) == null) || (userService.getByID(friendId) == null)) {
            String message = "User id does not exist";
            log.error(message);
            throw new UserNotFoundException(message);
        }
        userService.removeFriend(userService.getByID(id), userService.getByID(friendId));

    }

    @GetMapping("/users/{id}/friends")
    public List<User> getFriends(@PathVariable int id) {
        if ((userService.getByID(id) == null)) {
            String message = "User id does not exist";
            log.error(message);
            throw new UserNotFoundException(message);
        }

        return userService.getFriends(userService.getByID(id));
    }

    @GetMapping("/users/{id}/friends/common/{otherId}")
    public List<User> getCommonFriends(@PathVariable int id, @PathVariable int otherId) {
        if ((userService.getByID(id) == null) || (userService.getByID(otherId) == null)) {
            String message = "User id does not exist";
            log.error(message);
            throw new UserNotFoundException(message);
        }

        return userService.getCommonFriends(userService.getByID(id), userService.getByID(otherId));

    }

    @GetMapping("/users/{id}")
    public User getUser(@PathVariable int id) {
        if ((userService.getByID(id) == null)) {
            String message = "User id does not exist";
            log.error(message);
            throw new UserNotFoundException(message);
        }
        return userService.getByID(id);
    }

}
