package com.example.appbienvenidos.view.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.appbienvenidos.R;
import com.example.appbienvenidos.view.fragments.onboardingActivity;

import java.util.Locale;

public class WelcomeActivity extends BaseActivity {

    // Déclaration des variables
    Button Seconnecter, Inscrire, Explorer;
    ImageView logo;
    TextView Bienvenidos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_welcome);

        // Ajustement des marges
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Initialisation des vues (Liaison avec le XML)
        Seconnecter = findViewById(R.id.Seconnecter);
        Inscrire = findViewById(R.id.Inscrire);
        Explorer = findViewById(R.id.Explorer);
        logo = findViewById(R.id.logo);
        Bienvenidos = findViewById(R.id.Bienvenidos);

        // 1. Action pour "S'INSCRIRE" -> Ouvre SignupActivity
        Inscrire.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(WelcomeActivity.this, SignupActivity.class);
                // On envoie un message pour dire : "La destination finale est SIGNUP"
                intent.putExtra("DESTINATION", "SIGNUP");
                startActivity(intent);
            }
        });

        // 2. Action pour "SE CONNECTER" -> Ouvre LoginActivity
        Seconnecter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Assure-toi d'avoir créé le fichier LoginActivity.java
                Intent intent = new Intent(WelcomeActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

        // 3. Action pour "EXPLORER" -> Ouvre directement l'Accueil (MainActivity)
        Explorer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // On va directement sur la page principale avec la barre de navigation
                Intent intent = new Intent(WelcomeActivity.this, onboardingActivity.class);
                // On envoie un message pour dire : "La destination finale est HOME"
                intent.putExtra("DESTINATION", "HOME");
                startActivity(intent);

                // Optionnel : finish() pour fermer la page de bienvenue
                // finish();
            }
        });
    }
}