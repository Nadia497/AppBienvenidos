package com.example.appbienvenidos.model;

public class SpotRating {
    private int id;
    private int userId; // ID de l'utilisateur qui a posté l'évaluation
    private int spotId; // ID du spot évalué
    private float rating;
    private String comment;
    private String ratingDate;

    public SpotRating(int id, int userId, int spotId, float rating, String comment, String ratingDate) {
        this.id = id;
        this.userId = userId;
        this.spotId = spotId;
        this.rating = rating;
        this.comment = comment;
        this.ratingDate = ratingDate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getSpotId() {
        return spotId;
    }

    public void setSpotId(int spotId) {
        this.spotId = spotId;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getRatingDate() {
        return ratingDate;
    }

    public void setRatingDate(String ratingDate) {
        this.ratingDate = ratingDate;
    }
}
