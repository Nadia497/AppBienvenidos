package com.example.appbienvenidos.Data;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.appbienvenidos.Data.DAO.CategoryDAO;
import com.example.appbienvenidos.Data.DAO.GuideDAO;
import com.example.appbienvenidos.Data.DAO.MessageDAO;
import com.example.appbienvenidos.Data.DAO.SpotDAO;
import com.example.appbienvenidos.Data.DAO.SpotRatingDAO;
import com.example.appbienvenidos.Data.DAO.UserDAO;
import com.example.appbienvenidos.Data.Entities.Category;
import com.example.appbienvenidos.Data.Entities.Guide;
import com.example.appbienvenidos.Data.Entities.Message;
import com.example.appbienvenidos.Data.Entities.Spot;
import com.example.appbienvenidos.Data.Entities.SpotRating;
import com.example.appbienvenidos.Data.Entities.User;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

//Lister toutes les entitées qu on a créer
@Database(entities = {User.class, Category.class, Guide.class, Spot.class, Message.class , SpotRating.class}, version = 1, exportSchema = false)

public abstract class AppDataBase extends RoomDatabase{

    // Déclare les DAOs pour y accéder
    public abstract UserDAO userDao();
    public abstract CategoryDAO categoryDao();
    public abstract GuideDAO guideDao();
    public abstract SpotDAO spotDao();
    public abstract MessageDAO messageDao();
    public abstract SpotRatingDAO spotratingDao();

    // Singleton pour s'assurer qu'il n'y a qu'une seule instance de la base de données
    private static volatile AppDataBase INSTANCE;
    private static final int NUMBER_OF_THREADS = 4;

    // Un ExecutorService pour l'éxecution asynchrone des opérations de la bdd
    public static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    public static AppDataBase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (AppDataBase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    AppDataBase.class, "AppBienvenidos_database") // Nom de la bdd
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return INSTANCE;
    }

}