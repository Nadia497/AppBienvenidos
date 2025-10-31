package com.example.appbienvenidos.Data.DAO;

import androidx.room.Dao;
import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import androidx.room.Delete;

import com.example.appbienvenidos.Data.Entities.Spot;

import java.util.List;

@Dao
public interface SpotDAO {

    @Insert
    void insertSpot(Spot spot);

    @Update
    void updateSpot(Spot spot);

    @Delete
    void deleteSpot(Spot spot);

    @Query("SELECT * FROM Spot WHERE id = :spotId")
    Spot getSpotById(int spotId);

    @Query("SELECT * FROM Spot ORDER BY publication_date DESC")
    LiveData<List<Spot>> getAllSpots();

    @Query("SELECT * FROM Spot WHERE publisher_id = :publisherId ORDER BY publication_date DESC")
    LiveData<List<Spot>> getSpotsByPublisher(int publisherId);

    @Query("SELECT * FROM Spot WHERE category_id = :categoryId ORDER BY publication_date DESC")
    LiveData<List<Spot>> getSpotsByCategory(int categoryId);

    @Query("SELECT * FROM Spot ORDER BY average_rating DESC LIMIT 10") // Pour les "meilleurs spots"
    LiveData<List<Spot>> getTopRatedSpots();

    @Query("SELECT * FROM Spot WHERE title LIKE '%' || :query || '%' OR description LIKE '%' || :query || '%' ORDER BY publication_date DESC")
    LiveData<List<Spot>> searchSpots(String query);
}