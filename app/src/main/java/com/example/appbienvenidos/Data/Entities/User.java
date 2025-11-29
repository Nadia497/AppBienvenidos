<<<<<<< Updated upstream
=======
package com.example.appbienvenidos.Data.Entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "User")
public class User {

    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "Last_Name")
    private String Last_Name;

    @ColumnInfo(name = "First_Name")
    private String First_Name;

    @ColumnInfo(name="Email")
    private String Email;

    @ColumnInfo(name="Password")
    private String PasswordHash;  //Stocker un hash, pas le mot de passe en clair

    @ColumnInfo(name = "Location")//Ville ou région
    private String Location;

    @ColumnInfo(name="Profile_Picture_URL")
    private String Profile_Picture_URL;

    @ColumnInfo(name = "Bio")
    private String Bio;

    @ColumnInfo(name = "Role")
    private String Role;

    @ColumnInfo(name = "Registration_Date")
    private String Registration_Date;

    //Constructeur utilisé par le Room pour créer nos objets

    public User(String Last_Name , String First_Name, String Email ,
                String PasswordHash , String Location , String Profile_Picture_URL,
                String Bio , String Role , String Registration_Date){

        this.Last_Name = Last_Name ;
        this.First_Name = First_Name;
        this.Email = Email;
        this.PasswordHash = PasswordHash;
        this.Location = Location;
        this.Profile_Picture_URL = Profile_Picture_URL;
        this.Bio = Bio;
        this.Role = Role ;
        this.Registration_Date = Registration_Date;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLast_Name() {
        return Last_Name;
    }

    public void setLast_Nom(String last_Name) {
        Last_Name = last_Name;
    }

    public String getFirst_Name() {
        return First_Name;
    }

    public void setFirst_Name(String first_Name) {
        First_Name = first_Name;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getPasswordHash() {
        return PasswordHash;
    }

    public void setPasswordHash(String passwordHash) {
        PasswordHash = passwordHash;
    }

    public String getLocation() {
        return Location;
    }

    public void setLocation(String location) {
        Location = location;
    }

    public String getProfile_Picture_URL() {
        return Profile_Picture_URL;
    }

    public void setProfile_Picture_URL(String profile_Picture_URL) {
        Profile_Picture_URL = profile_Picture_URL;
    }

    public String getBio() {
        return Bio;
    }

    public void setBio(String bio) {
        Bio = bio;
    }

    public String getRole() {
        return Role;
    }

    public void setRole(String role) {
        Role = role;
    }

    public String getRegistration_Date() {
        return Registration_Date;
    }

    public void setRegistration_Date(String registration_Date) {
        Registration_Date = registration_Date;
    }
}
>>>>>>> Stashed changes
