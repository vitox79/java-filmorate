package ru.yandex.practicum.filmorate.repository;

import ru.yandex.practicum.filmorate.model.User;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class UserRepository {
    HashMap<Integer, User> users = new HashMap<>();
    private int count = 0;
    public void save(User user){
        count++;
        user.setId(count);
        users.put(count,user);
    }
    public User getByID(int id){
        return users.get(id);
    }
    public int size(){
        return users.size();
    }
    public List<User> getAll(){
        return users.values().stream().collect(Collectors.toList());
    }


    }
