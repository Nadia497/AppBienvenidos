package com.example.appbienvenidos.view.activities;

import static android.app.ProgressDialog.show;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDelegate;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.appbienvenidos.R;
import com.google.android.material.card.MaterialCardView;
import com.google.firebase.auth.FirebaseAuth;

public class ParametreActivity extends AppCompatActivity {
    private ImageButton btnback;
    private View info_pers, notifs, theme, language, logout;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parametre);

        btnback = findViewById(R.id.btnBack);

        int nightModeFlags = getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
        //information personnelle
        info_pers = findViewById(R.id.info_pers);
        reglage(info_pers, "informations personnelles", R.drawable.outline_person_24, "");

        //Notifications
        notifs = findViewById(R.id.notifs);
        reglage(notifs, "Notifications", R.drawable.ic_notifications_n, "Activées");

        //language
        language = findViewById(R.id.language);
        reglage(language, "Language",R.drawable.ic_language ,"Français");

        //theme
        theme = findViewById(R.id.theme);
        String currentThemeTxt = (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES) ? "Sombre" : "Clair";
        reglage(theme, "Thème", R.drawable.ic_theme, currentThemeTxt);

        theme.setOnClickListener(v -> showThemeDialog());
        info_pers.setOnClickListener(v ->{
            Intent intent = new Intent(this, EditProfileActivity.class);
            startActivity(intent);
        });

        btnback.setOnClickListener(v -> finish());

        logout = findViewById(R.id.btnlogout);
        logout.setOnClickListener(v -> {
            new AlertDialog.Builder(this)
                    .setTitle("Déconnexion")
                    .setMessage("Voulez-vous vraiment vous déconnecter ?")
                    .setNegativeButton("Annuler", null)
                    .setPositiveButton("Déconnecter", (dialog, which) -> {
                        //la déconnexion de firabase aussi
                        FirebaseAuth.getInstance().signOut();
                        Intent intent = new Intent(this, LoginActivity.class);
                        //pour empêcher l'utilisateur de revenir en arrière avec le btn retour
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        finish();
                    })
                    .show();
        });

    }
    private void reglage(View view, String title, int image, String value){
        ((TextView)
                view.findViewById(R.id.rowTitle)).setText(title);

        ((ImageView)
                view.findViewById(R.id.rowIcon)).setImageResource(image);

        ((TextView) view.findViewById(R.id.rowValue)).setText(value);
    }

    private void showThemeDialog(){
        final String[] themes = {"Clair", "Sombre"};

        //on vérifier quel est le mode actuel pour cocher la bonne case
        int checkedItem = 0;
        int currentMode = AppCompatDelegate.getDefaultNightMode();
        if(currentMode == AppCompatDelegate.MODE_NIGHT_YES){
            checkedItem = 1;
        }
        else if (currentMode == AppCompatDelegate.MODE_NIGHT_NO){
            checkedItem = 0;
        }

        new AlertDialog.Builder(this)
                .setTitle("Choisir le thème")
                .setSingleChoiceItems(themes, checkedItem, (dialog, which) -> {
                    if(which == 0){
                        //forcer le mode clair

                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                        ((TextView)
                        theme.findViewById(R.id.rowValue)).setText("Clair");
                    }
                    else if(which == 1){
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                        ((TextView)
                                theme.findViewById(R.id.rowValue)).setText("Sombre");
                    }
                    dialog.dismiss();
                })
                .setNegativeButton("Annuler", null)
                .show();
    }
}
