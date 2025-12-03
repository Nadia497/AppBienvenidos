package com.example.appbienvenidos;

import android.os.Bundle;
import android.util.Log;


import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.FirebaseApp;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_fragment);

        /*AppDataBase db = AppDataBase.getDatabase(this);

        Category newCat1 = new Category(
                "Café"
        );
        Category newCat2 = new Category(
                "Paysage"
        );
        Category newCat3 = new Category(
                "Culture"
        );
        Category newCat4 = new Category(
                "Shopping"
        );
        Category newCat5 = new Category(
                "Hôtel"
        );

        AppDataBase.databaseWriteExecutor.execute(() -> {

            //db.categoryDao().deleteById(7);


            List<Category> Categories = db.categoryDao().getCategoryList();
            if(Categories != null){
                for(Category c:Categories){
                    Log.d("DB_CATEGORY","Category: " +c.getId()+" : " +c.getName());
                }
            }
        });*/
        FirebaseApp.initializeApp(this);
        Log.d("FIREBASE", "Firebase connected successfully!");



    }

}
