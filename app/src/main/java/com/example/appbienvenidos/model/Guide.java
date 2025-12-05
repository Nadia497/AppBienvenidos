package com.example.appbienvenidos.model;

import com.google.firebase.firestore.DocumentId;

public class Guide {

    @DocumentId // Récupère l'ID du document
    private String uid;

    // Infos copiées du User
    private String firstName;
    private String lastName;
    private String profileImageUrl;

    // Infos spécifiques au Guide
    private String cityServed;
    private String hourlyRate;    // Nom correct : hourlyRate
    private String specialities;  // Nom correct : specialities
    private String languages;     // J'ai AJOUTÉ cette variable qui manquait
    private boolean isAvailable;  // Nom correct : isAvailable

    // 1. Constructeur VIDE (Obligatoire Firebase)
    public Guide() { }

    // 2. Constructeur COMPLET
    public Guide(String uid, String firstName, String lastName, String profileImageUrl, String cityServed, String hourlyRate, String specialities, String languages, boolean isAvailable) {
        this.uid = uid;
        this.firstName = firstName;
        this.lastName = lastName;
        this.profileImageUrl = profileImageUrl;
        this.cityServed = cityServed;
        this.hourlyRate = hourlyRate;
        this.specialities = specialities;
        this.languages = languages;
        this.isAvailable = isAvailable;
    }

    // 3. Getters et Setters (Tout est corrigé ici)

    public String getUid() { return uid; }
    public void setUid(String uid) { this.uid = uid; }

    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }

    public String getFullName() { return firstName + " " + lastName; }

    public String getProfileImageUrl() { return profileImageUrl; }
    public void setProfileImageUrl(String profileImageUrl) { this.profileImageUrl = profileImageUrl; }

    public String getCityServed() { return cityServed; }
    public void setCityServed(String cityServed) { this.cityServed = cityServed; }

    public String getHourlyRate() { return hourlyRate; } // Corrigé
    public void setHourlyRate(String hourlyRate) { this.hourlyRate = hourlyRate; }

    public String getSpecialities() { return specialities; } // Corrigé
    public void setSpecialities(String specialities) { this.specialities = specialities; }

    public String getLanguages() { return languages; } // Corrigé
    public void setLanguages(String languages) { this.languages = languages; }

    public boolean isAvailable() { return isAvailable; } // Corrigé
    public void setAvailable(boolean available) { isAvailable = available; }
}