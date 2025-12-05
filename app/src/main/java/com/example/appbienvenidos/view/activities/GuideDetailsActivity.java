package com.example.appbienvenidos.view.activities;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.example.appbienvenidos.R;

public class GuideDetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // C'EST CETTE LIGNE QUI LIE AU XML
        // Assure-toi que tu as bien créé un fichier XML (ex: activity_guide_details.xml)
        setContentView(R.layout.fragment_profile_guide);
    }
}