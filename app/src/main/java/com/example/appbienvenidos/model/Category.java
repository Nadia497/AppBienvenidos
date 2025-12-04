package com.example.appbienvenidos.model;

public class Category {

    private String id;

    private String name;

    //Constructeur

    public Category(String name){
        this.name = name;
    }

    //Getters Setters


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

