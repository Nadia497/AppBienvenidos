package com.example.appbienvenidos.Data.Entities;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;
import androidx.room.ColumnInfo;

@Entity(tableName = "Guide",
        foreignKeys = @ForeignKey(entity = User.class,
                parentColumns = "id",
                childColumns = "user_id",
                onDelete = ForeignKey.CASCADE)) // Si l'utilisateur est supprimé, le guide aussi)

public class Guide {

    @PrimaryKey //le user_id est à la fois l id du guide et du user
    //puisque l user peut lui mem devenir un guide

    @ColumnInfo(name = "User_id")
    private int User_id ;

    @ColumnInfo(name = "Specialities")
    private String Specialities ;

    @ColumnInfo(name = "Languages")
    private String Languages ;

    @ColumnInfo(name = "Hourly_rate")
    private String Hourly_rate ;

    @ColumnInfo(name = "is_available")
    private boolean is_availalble ;

    @ColumnInfo(name = "city_served")
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

