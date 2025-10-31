package com.example.appbienvenidos.Data.DAO;

import androidx.room.Dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import androidx.room.Delete;

import com.example.appbienvenidos.Data.Entities.Category;

import java.util.List;

@Dao
public interface CategoryDAO {

    @Insert
    void insertCategory(Category category);

    @Update
    void updateCategory(Category category);

    @Delete
    void deleteCategory(Category category);

    @Query("SELECT * FROM Category WHERE id = :categoryId")
    Category getCategoryById(int categoryId);

    @Query("SELECT * FROM Category ORDER BY name ASC")
    LiveData<List<Category>> getAllCategories();
}
