package com.example.appbienvenidos.Data.DAO;

import androidx.room.Dao;
import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import androidx.room.Delete;

import com.example.appbienvenidos.Data.Entities.SpotRating;

import java.util.List;

@Dao
public interface SpotRatingDAO {

    @Insert
    long insert(SpotRating spotRating);

    @Update
    void update(SpotRating spotRating);

    @Delete
    void delete(SpotRating spotRating);

    @Query("SELECT * FROM SpotRating WHERE id = :ratingId")
    SpotRating getSpotRatingById(int ratingId);

    @Query("SELECT * FROM SpotRating WHERE spot_id = :spotId ORDER BY rating_date DESC")
    List<SpotRating> getRatingsForSpot(int spotId);

    @Query("SELECT * FROM SpotRating WHERE user_id = :userId ORDER BY rating_date DESC")
    List<SpotRating> getRatingsByUser(int userId);

    @Query("SELECT AVG(rating) FROM SpotRating WHERE spot_id = :spotId")
    float getAverageRatingForSpot(int spotId);

    @Query("SELECT COUNT(id) FROM SpotRating WHERE spot_id = :spotId")
    int getTotalRatingsCountForSpot(int spotId);

    @Query("SELECT * FROM SpotRating")
    List<SpotRating> getAllSpotRatings();

}