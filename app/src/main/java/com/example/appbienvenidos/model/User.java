package com.example.appbienvenidos.model;

import com.google.firebase.firestore.PropertyName;

public class User {
    private String lastName;
    private String firstName;
    private String email;
    private String PasswordHash;  //Stocker un hash, pas le mot de passe en clair
    private String location;
    private String photoUrl;
    private String role;
    private String Registration_Date;

    //Constructeur utilisé par le Room pour créer nos objets

    public User(String lastName, String firstName, String email,
                String PasswordHash, String location, String photoUrl,
                String role, String Registration_Date) {

        this.lastName = lastName;
        this.firstName = firstName;
        this.email = email;
        this.PasswordHash = PasswordHash;
        this.location = location;
        this.photoUrl = photoUrl;
        this.role = role;
        this.Registration_Date = Registration_Date;
    }

    public User(String lastName, String firstName, String email, String location, String role, String photoUrl) {
        this.lastName = lastName;
        this.firstName = firstName;
        this.email = email;
        this.location = location;
        this.role = role;
        this.photoUrl = photoUrl;

        this.PasswordHash = ""; // On ne stocke pas le mot de passe ici si on utilise Firebase Auth


    }
    public User() {
    }

    @PropertyName("lastName")
    public String getLastName() {
        return lastName;
    }

    @PropertyName("lastName")
    public void setLastName(String last_Name) {
        this.lastName = last_Name;
    }

    @PropertyName("firstName")
    public String getFirstName() { return firstName; }

    @PropertyName("firstName")
    public void setFirstName(String firstName) { this.firstName = firstName; }

    @PropertyName("First_Name") // Au cas où
    public void setFirst_Name_Alt(String firstName) { this.firstName = firstName; }

    public String getFullName(){

        String f = firstName != null ? firstName:"";
        String l = lastName != null ?lastName:"";
        return (l + " " + f).trim() ;
    }

    @PropertyName("email")
    public String getEmail() {
        return email;
    }

    @PropertyName("email")
    public void setEmail(String email) {
        this.email = email;
    }

    public String getPasswordHash() {
        return PasswordHash;
    }

    public void setPasswordHash(String passwordHash) {
        PasswordHash = passwordHash;
    }

    @PropertyName("location")
    public String getLocation() {   return location;   }

    @PropertyName("location")
    public void setLocation(String location) {
        this.location = location;
    }

    // 1. Le Getter principal (utilisé par l'appli)
    // Par défaut, on le lie à "photoUrl"
    @PropertyName("photoUrl")
    public String getPhotoUrl() { return photoUrl; }

    // 2. Setter 1 : Pour le nom standard "photoUrl"
    @PropertyName("photoUrl")
    public void setPhotoUrl(String photoUrl) { this.photoUrl = photoUrl; }

    @PropertyName("role")
    public String getRole() {
        return role;
    }

    @PropertyName("role")
    public void setRole(String role) {
        this.role = role;
    }

    public String getRegistration_Date() {
        return Registration_Date;
    }

    public void setRegistration_Date(String registration_Date) {
        Registration_Date = registration_Date;
    }
}