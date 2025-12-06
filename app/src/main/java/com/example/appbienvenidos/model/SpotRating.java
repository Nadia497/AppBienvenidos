package com.example.appbienvenidos.model;

public class SpotRating {
    private String id;
    private String userId; // ID de l'utilisateur qui a posté l'évaluation
    private String spotId; // ID du spot évalué
    private float rating;
    private String comment;
    private String ratingDate;

    public SpotRating(String id, String userId, String spotId, float rating, String comment, String ratingDate) {
        this.id = id;
        this.userId = userId;
        this.spotId = spotId;
        this.rating = rating;
        this.comment = comment;
        this.ratingDate = ratingDate;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getSpotId() {
        return spotId;
    }

    public void setSpotId(String spotId) {
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
