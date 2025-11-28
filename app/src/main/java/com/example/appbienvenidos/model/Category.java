package com.example.appbienvenidos.model;

public class Category {

    private int id;

    private String name;

    //Constructeur

    public Category(String name){
        this.name = name;
    }

    //Getters Setters


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

