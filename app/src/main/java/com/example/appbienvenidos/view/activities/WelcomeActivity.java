package com.example.appbienvenidos.view.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Button;


import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.appbienvenidos.R;
import com.example.appbienvenidos.view.fragments.onboardingActivity;

public class WelcomeActivity extends AppCompatActivity {

    Button Seconnecter , Inscrire, Explorer;
    ImageView logo;
    TextView Bienvenidos;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_welcome);

        // Ajustement automatique des marges système
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        //Find elements by Id
        Seconnecter = findViewById(R.id.Seconnecter);
        Inscrire = findViewById(R.id.Inscrire);
        Explorer = findViewById(R.id.Explorer);
        logo = findViewById(R.id.logo);
        Bienvenidos = findViewById(R.id.Bienvenidos);


        Inscrire.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(WelcomeActivity.this,SignupActivity.class));
            }
        });



}
}