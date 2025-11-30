package com.example.appbienvenidos.model;

public class Guide {
    private int User_id ;

    private String Specialities ;

    private String Languages ;

    private String Hourly_rate ;

    private boolean is_availalble ;

    private String cityServed;

    //Getters and Setters

    public int getUser_id() {
        return User_id;
    }

    public String getCityServed() {
        return cityServed;
    }

    public void setCityServed(String cityServed) {
        this.cityServed = cityServed;
    }

    public void setUser_id(int user_id) {
        User_id = user_id;
    }

    public String getSpecialities() {
        return Specialities;
    }

    public void setSpecialities(String specialities) {
        Specialities = specialities;
    }

    public String getLanguages() {
        return Languages;
    }

    public void setLanguages(String languages) {
        Languages = languages;
    }

    public String getHourly_rate() {
        return Hourly_rate;
    }

    public void setHourly_rate(String hourly_rate) {
        Hourly_rate = hourly_rate;
    }

    public boolean isIs_availalble() {
        return is_availalble;
    }

    public void setIs_availalble(boolean is_availalble) {
        this.is_availalble = is_availalble;
    }
}