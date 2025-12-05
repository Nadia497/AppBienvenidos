package com.example.appbienvenidos.model;

public class User {
    private int id;
    private String lastName;
    private String firstName;
    private String Email;
    private String PasswordHash;  //Stocker un hash, pas le mot de passe en clair
    private String Location;
    private String photoUrl;
    private String Role;
    private String Registration_Date;

    //Constructeur utilisé par le Room pour créer nos objets

    public User(String lastName, String firstName, String Email,
                String PasswordHash, String Location, String photoUrl,
                String Role, String Registration_Date) {

        this.lastName = lastName;
        this.firstName = firstName;
        this.Email = Email;
        this.PasswordHash = PasswordHash;
        this.Location = Location;
        this.photoUrl = photoUrl;
        this.Role = Role;
        this.Registration_Date = Registration_Date;
    }

    public User() {
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String last_Name) {
        lastName = last_Name;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        firstName = firstName;
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

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        photoUrl = photoUrl;
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