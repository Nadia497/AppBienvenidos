package com.example.appbienvenidos.Data.DAO;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import androidx.room.Delete;

import com.example.appbienvenidos.Data.Entities.User;

import java.util.List;

@Dao
public interface UserDAO {
    @Insert
    void insertUser(User user);

    @Update
    void updateUser(User user);

    @Delete
    void deleteUser(User user);

    @Query("SELECT * FROM User WHERE id = :userId")
    User getUserById(int userId);

    @Query("SELECT * FROM User WHERE Last_Name = :username")
    User getUserByUsername(String username);

    @Query("SELECT * FROM User WHERE Last_Name = :userlastname and First_Name = :userfirstname")
    User getUserByUsername(String userlastname , String userfirstname);

    @Query("SELECT * FROM User WHERE email = :email")
    User getUserByEmail(String email);

    @Query("SELECT * FROM User ORDER BY Last_Name ASC")
    LiveData<List<User>> getAllUsers();

    @Query("SELECT * FROM User WHERE Role = :userType")
    LiveData<List<User>> getUsersByType(String userType); // Ex: "local" ou "guide"
}
