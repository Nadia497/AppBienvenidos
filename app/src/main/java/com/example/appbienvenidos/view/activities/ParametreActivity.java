package com.example.appbienvenidos.view.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.appbienvenidos.R;
import com.google.android.material.card.MaterialCardView;

public class ParametreActivity extends AppCompatActivity {
    private Button btnback;
    private View info_pers, notifs, theme, language, logout;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parametre);

        //information personnelle
        info_pers = findViewById(R.id.info_pers);
        reglage(info_pers, "informations personnelles", R.drawable.outline_person_24, "");

        //Notifications
        notifs = findViewById(R.id.notifs);
        reglage(notifs, "Notifications", R.drawable.ic_notifications_n, "Activées");

        //language
        language = findViewById(R.id.language);
        reglage(language, "Language",0 ,"Français");

        //theme
        theme = findViewById(R.id.theme);
        reglage(theme, "Thème", 0, "Clair");




    }
    private void reglage(View view, String title, int image, String value){
        ((TextView)
                view.findViewById(R.id.rowTitle)).setText(title);

        ((ImageView)
                view.findViewById(R.id.rowIcon)).setImageResource(image);

        ((TextView) view.findViewById(R.id.rowValue)).setText(value);
    }
}
