package ru.yandex.practicum.filmorate.model;

import java.util.ArrayList;
import java.util.List;

public class Response {
    int id;
    List<String> data = new ArrayList<>();
    public Response(){
    }
    public Response(int id, String name){
        this.id = id;
        data.add(name);
    }
    public void add(String newGenre){
        data.add(newGenre);
    }
    public void setId(int id){
        this.id = id;
    }
}
