package com.example.appbienvenidos.model;

import com.google.firebase.firestore.DocumentId;
import com.google.firebase.firestore.PropertyName;

import java.io.Serializable;

public class Guide implements Serializable{

    @DocumentId // Récupère l'ID du document
    private String uid;

    // Infos copiées du User
    private String firstName;
    private String lastName;
    private String profileImageUrl;

    // Infos spécifiques au Guide
    private String cityServed;
    private String hourlyRate;
    private String Specialities;
    private String langages;
    private String phoneNumber;
    private boolean isAvailable;

    // 1. Constructeur VIDE (Obligatoire Firebase)
    public Guide() { }

    // 2. Constructeur COMPLET
    public Guide(String uid, String firstName, String lastName, String profileImageUrl, String cityServed, String hourlyRate, String specialities, String languages, String phoneNumber ,boolean isAvailable) {
        this.uid = uid;
        this.firstName = firstName;
        this.lastName = lastName;
        this.profileImageUrl = profileImageUrl;
        this.cityServed = cityServed;
        this.hourlyRate = hourlyRate;
        this.Specialities = specialities;
        this.langages = languages;
        this.phoneNumber = phoneNumber ;
        this.isAvailable = isAvailable;
    }

    // 3. Getters et Setters

    public String getFullName() { return firstName + " " + lastName; }

    public String getUid() { return uid; }
    public void setUid(String uid) { this.uid = uid; }

    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }

    public String getProfileImageUrl() { return profileImageUrl; }
    public void setProfileImageUrl(String profileImageUrl) { this.profileImageUrl = profileImageUrl; }

    // --- CORRECTION DES NOMS ---

    @PropertyName("cityServed") // Match le Log
    public String getCityServed() { return cityServed; }
    @PropertyName("cityServed")
    public void setCityServed(String cityServed) { this.cityServed = cityServed; }

    @PropertyName("hourlyRate") // Match le Log
    public String getHourlyRate() { return hourlyRate; }
    @PropertyName("hourlyRate")
    public void setHourlyRate(String hourlyRate) { this.hourlyRate = hourlyRate; }

    @PropertyName("specialities") // Match le Log
    public String getSpecialities() { return Specialities; }
    @PropertyName("specialities")
    public void setSpecialities(String specialities) { this.Specialities = specialities; }

    @PropertyName("langages") // Match le Log (qui semble écrit 'langages' et pas 'languages')
    public String getLangages() { return langages; }
    @PropertyName("langages")
    public void setLangages(String langages) { this.langages = langages; }

    @PropertyName("phoneNumber") // Match le Log
    public String getPhoneNumber() { return phoneNumber; }
    @PropertyName("phoneNumber")
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }

    @PropertyName("available")
    public boolean isAvailable() { return isAvailable; }
    @PropertyName("available")
    public void setAvailable(boolean available) { this.isAvailable = available; }
}