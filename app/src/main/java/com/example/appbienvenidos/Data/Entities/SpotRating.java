package com.example.appbienvenidos.Data.Entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(tableName = "SpotRating",
        foreignKeys = {
                // Clé étrangère de l'utilisateur qui a fait le "rating"
                @ForeignKey(entity = User.class,
                        parentColumns = "id",
                        childColumns = "user_id",
                        onDelete = ForeignKey.CASCADE), // Si l'utilisateur est supprimé, ses évaluations le sont aussi
                // Clé étrangère vers le spot qui a été évalué
                @ForeignKey(entity = Spot.class,
                        parentColumns = "id",
                        childColumns = "spot_id",
                        onDelete = ForeignKey.CASCADE) // Si le spot est supprimé, ses évaluations le sont aussi
        })

public class SpotRating {

    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "user_id")
    private int userId; // ID de l'utilisateur qui a posté l'évaluation

    @ColumnInfo(name = "spot_id")
    private int spotId; // ID du spot évalué

    @ColumnInfo(name = "rating")
    private float rating;

    @ColumnInfo(name = "comment")
    private String comment;

    @ColumnInfo(name = "rating_date")
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