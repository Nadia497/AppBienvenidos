package com.example.appbienvenidos.model;

import com.google.firebase.firestore.DocumentId;
import com.google.firebase.firestore.Exclude;

import java.io.Serializable;
import java.util.List;

public class Spot implements Serializable {

    @DocumentId // L'ID du document
    private String id;

    private String publisher_id;
    private String title;
    private String description;
    private String adress;
    private String category_id;
    @Exclude
    private String categoryNameDisplay;
    private List<String> image_URL;
    private double average_Rating;
    private double total_Rating;
    private String publication_Date;

    // 1. Constructeur VIDE
    public Spot() { }

    // 2. Constructeur COMPLET
    public Spot(String publisher_id, String title, String description, String adress, String category_id, List<String> image_URL, double average_Rating, double total_Rating, String publication_Date) {
        this.publisher_id = publisher_id;
        this.title = title;
        this.description = description;
        this.adress = adress;
        this.category_id = category_id;
        this.image_URL = image_URL;
        this.average_Rating = average_Rating;
        this.total_Rating = total_Rating;
        this.publication_Date = publication_Date;
    }

    // 3. GETTERS et SETTERS
    // (Mis à jour avec les nouveaux noms)

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getPublisher_id() { return publisher_id; }
    public void setPublisher_id(String publisher_id) { this.publisher_id = publisher_id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getAdress() { return adress; }
    public void setAdress(String adress) { this.adress = adress; }

    public String getCategory_id() { return category_id; }
    public void setCategory_id(String category_id) { this.category_id = category_id; }

    @Exclude
    public String getCategoryNameDisplay(){return categoryNameDisplay;}
    @Exclude
    public void setCategoryNameDisplay(String name){this.categoryNameDisplay=name;}
    public List<String> getImage_URL() { return image_URL; }
    public void setImage_URL(List<String> image_URL) { this.image_URL = image_URL; }

    public double getAverage_Rating() { return average_Rating; }
    public void setAverage_Rating(double average_Rating) { this.average_Rating = average_Rating; }

    public double getTotal_Rating() { return total_Rating; }
    public void setTotal_Rating(double total_Rating) { this.total_Rating = total_Rating; }

    public String getPublication_Date() { return publication_Date; }
    public void setPublication_Date(String publication_Date) { this.publication_Date = publication_Date; }
}