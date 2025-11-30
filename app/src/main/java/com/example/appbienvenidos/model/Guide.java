package com.example.appbienvenidos.model;

import com.google.firebase.firestore.DocumentId;

public class Guide {

    @DocumentId // Récupère l'ID du document (qui est aussi l'ID du User)
    private String uid;

    // Infos copiées du User
    private String firstName;
    private String lastName;
    private String profileImageUrl;

    // Infos spécifiques au Guide
    private String cityServed;
    private String hourlyRate;
    private String specialities;
    private boolean isAvailable;

    // 1. Constructeur VIDE (Obligatoire Firebase)
    public Guide() { }

    // 2. Constructeur COMPLET
    public Guide(String uid, String firstName, String lastName, String profileImageUrl, String cityServed, String hourlyRate, String specialities, boolean isAvailable) {
        this.uid = uid;
        this.firstName = firstName;
        this.lastName = lastName;
        this.profileImageUrl = profileImageUrl;
        this.cityServed = cityServed;
        this.hourlyRate = hourlyRate;
        this.specialities = specialities;
        this.isAvailable = isAvailable;
    }

    // 3. Getters
    public String getUid() { return uid; }
    public String getFirstName() { return firstName; }
    public String getLastName() { return lastName; }

    public String getFullName() { return firstName + " " + lastName; }

    public String getProfileImageUrl() { return profileImageUrl; }
    public String getCityServed() { return cityServed; }
    public String getHourlyRate() { return hourlyRate; }
    public String getSpecialities() { return specialities; }
    public boolean isAvailable() { return isAvailable; }

    // Setters
    public void setFirstName(String firstName) { this.firstName = firstName; }
    public void setLastName(String lastName) { this.lastName = lastName; }
    public void setProfileImageUrl(String profileImageUrl) { this.profileImageUrl = profileImageUrl; }
    public void setCityServed(String cityServed) { this.cityServed = cityServed; }
}