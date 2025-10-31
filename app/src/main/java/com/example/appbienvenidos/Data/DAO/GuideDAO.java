package com.example.appbienvenidos.Data.DAO;

import androidx.room.Dao;

import androidx.lifecycle.LiveData;

import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import androidx.room.Delete;

import com.example.appbienvenidos.Data.Entities.Guide;

import java.util.List;

@Dao
public interface GuideDAO {

    @Insert
    void insertGuide(Guide guide);

    @Update
    void updateGuide(Guide guide);

    @Delete
    void deleteGuide(Guide guide);

    @Query("SELECT * FROM Guide WHERE user_id = :userId")
    Guide getGuideByUserId(int userId);

    @Query("SELECT * FROM Guide ORDER BY city_served ASC")
    LiveData<List<Guide>> getAllGuides();

    @Query("SELECT * FROM Guide WHERE city_served = :city ORDER BY user_id ASC")
    LiveData<List<Guide>> getGuidesByCity(String city);
}

