package com.example.appbienvenidos.model;

public class User {
    private int id;
    private String lastName;
    private String First_Name;
    private String Email;
    private String PasswordHash;  //Stocker un hash, pas le mot de passe en clair
    private String Location;
    private String Profile_Picture_URL;
    private String Role;
    private String Registration_Date;
    //Constructeur sans password
    public User(String lastName, String First_Name, String Email,
                String Location, String Profile_Picture_URL,
                 String Role) {

        this.lastName = lastName;
        this.First_Name = First_Name;
        this.Email = Email;
        this.Location = Location;
        this.Profile_Picture_URL = Profile_Picture_URL;
        this.Role = Role;
    }
    //Constructeur complet

    public User(String lastName, String First_Name, String Email,
                String PasswordHash, String Location, String Profile_Picture_URL,
                 String Role, String Registration_Date) {

        this.lastName = lastName;
        this.First_Name = First_Name;
        this.Email = Email;
        this.PasswordHash = PasswordHash;
        this.Location = Location;
        this.Profile_Picture_URL = Profile_Picture_URL;
        this.Role = Role;
        this.Registration_Date = Registration_Date;
    }

    public User() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String last_Name) {
        lastName = last_Name;
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