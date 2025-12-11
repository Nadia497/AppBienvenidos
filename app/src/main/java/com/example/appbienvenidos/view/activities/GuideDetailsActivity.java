package com.example.appbienvenidos.view.activities;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.example.appbienvenidos.R;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Context;
import android.content.Intent;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.activity.EdgeToEdge;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.RecyclerView;


import com.example.appbienvenidos.model.Guide;
import com.example.appbienvenidos.view.adapter.SpotAdapter;
import com.example.appbienvenidos.viewmodel.SpotViewModel ;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.imageview.ShapeableImageView;
// Imports pour la carte (OSMDroid)
import org.osmdroid.config.Configuration;
import com.bumptech.glide.Glide;


import java.util.ArrayList;
import java.util.List;

public class GuideDetailsActivity extends AppCompatActivity {

    ShapeableImageView imgProfileGuide;
    TextView GuideName , GuideCity,GuideHourlyRate ,GuideSpecialities,
            GuideLanguages;
    RecyclerView recyclerSpotsGuide;
    MaterialButton btnCallGuide;

    private SpotViewModel spotviewmodel ;
    private SpotAdapter spotadapter ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        EdgeToEdge.enable(this);
        setContentView(R.layout.fragment_profile_guide);

        // Initialisation des Vues (Liaison avec le XML)
        initViews();

        Guide guide = (Guide) getIntent().getSerializableExtra("GUIDE_KEY");
        if(guide!= null){
            setupGuide(guide);
        }else{
            Toast.makeText(this, "Erreur : Impossible de charger le guide", Toast.LENGTH_SHORT).show();
            finish();
        }


        // Initialiser lu boutton
        setupButt(guide);

        }



    public void initViews(){
        imgProfileGuide = findViewById(R.id.imgProfileGuide);
        GuideName = findViewById(R.id.GuideName);
        GuideCity = findViewById(R.id.GuideCity);
        GuideHourlyRate = findViewById(R.id.GuideHourlyRate);
        GuideSpecialities = findViewById(R.id.GuideSpecialities);
        GuideLanguages = findViewById(R.id.GuideLanguages);
        recyclerSpotsGuide = findViewById(R.id.recyclerSpotsGuide);
        btnCallGuide = findViewById(R.id.btnCallGuide);

    }

    public void setupGuide(Guide guide){
        Intent intent = getIntent() ;
            GuideName.setText(guide.getFullName());
            GuideCity.setText(guide.getCityServed());
            GuideHourlyRate.setText(guide.getHourlyRate());
            GuideSpecialities.setText(guide.getSpecialities());
            GuideLanguages.setText(guide.getLangages());

        // Image (avec Glide)
        if (guide.getProfileImageUrl() != null && !guide.getProfileImageUrl().isEmpty()) {
            Glide.with(this)
                    .load(guide.getProfileImageUrl())
                    .placeholder(R.mipmap.ic_launcher) // Image d'attente
                    .centerCrop()
                    .into(imgProfileGuide);
        }

    }


    public void setupButt(Guide guide){
        btnCallGuide.setOnClickListener(v->{
            Toast.makeText(this, "Contacter " + guide.getFirstName(), Toast.LENGTH_SHORT).show();

        });


    }
}

