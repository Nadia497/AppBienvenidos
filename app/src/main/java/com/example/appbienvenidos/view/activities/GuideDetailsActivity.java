package com.example.appbienvenidos.view.activities;

import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;

import com.bumptech.glide.Glide;
import com.example.appbienvenidos.R;
import com.example.appbienvenidos.model.Guide;
import com.example.appbienvenidos.view.adapter.SpotAdapter;
import com.example.appbienvenidos.viewmodel.SpotViewModel;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.imageview.ShapeableImageView;

public class GuideDetailsActivity extends AppCompatActivity {

    ShapeableImageView imgProfileGuide;
    TextView GuideName, GuideCity, GuideHourlyRate, GuideSpecialities, GuideLanguages;
    RecyclerView recyclerSpotsGuide;
    MaterialButton btnCallGuide;
    LinearLayout layoutNoData;

    private SpotViewModel spotViewModel;
    private SpotAdapter spotAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.fragment_profile_guide);

        // Initialisation des liens avec le XML
        initViews();

        // Récupération de l'objet Guide envoyé depuis l'activité précédente
        Guide guide = (Guide) getIntent().getSerializableExtra("GUIDE_KEY");

        if (guide != null) {
            // A. Remplir les infos du profil
            setupGuideInfo(guide);

            // B. Configurer le bouton d'action
            setupButtons(guide);

            // C. Charger et afficher les spots du guide
            setupSpotsList(guide);
        } else {
            Toast.makeText(this, "Erreur : Guide introuvable", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private void initViews() {
        imgProfileGuide = findViewById(R.id.imgProfileGuide);
        GuideName = findViewById(R.id.GuideName);
        GuideCity = findViewById(R.id.GuideCity);
        GuideHourlyRate = findViewById(R.id.GuideHourlyRate);
        GuideSpecialities = findViewById(R.id.GuideSpecialities);
        GuideLanguages = findViewById(R.id.GuideLanguages);

        recyclerSpotsGuide = findViewById(R.id.recyclerSpotsGuide);

        btnCallGuide = findViewById(R.id.btnCallGuide);
        layoutNoData = findViewById(R.id.layoutNoData);
    }

    private void setupGuideInfo(Guide guide) {

        GuideName.setText(guide.getFullName());
        GuideCity.setText(guide.getCityServed());
        GuideHourlyRate.setText(guide.getHourlyRate() + "/h");
        GuideSpecialities.setText(guide.getSpecialities());


        if (guide.getLangages() != null) {
            GuideLanguages.setText(guide.getLangages());
        }

        // Image avec Glide
        if (guide.getProfileImageUrl() != null && !guide.getProfileImageUrl().isEmpty()) {
            Glide.with(this)
                    .load(guide.getProfileImageUrl())
                    .placeholder(R.mipmap.ic_launcher)
                    .centerCrop()
                    .into(imgProfileGuide);
        }
    }

    private void setupButtons(Guide guide) {
        btnCallGuide.setOnClickListener(v -> {
            Toast.makeText(this, "Contacter " + guide.getFirstName(), Toast.LENGTH_SHORT).show();
        });
    }

    private void setupSpotsList(Guide guide) {

        // 1. Configurer le RecyclerView
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerSpotsGuide.setLayoutManager(layoutManager);

        // 2. Initialiser l'Adapter
        spotAdapter = new SpotAdapter();
        recyclerSpotsGuide.setAdapter(spotAdapter);

        // 3. Initialiser le ViewModel
        spotViewModel = new ViewModelProvider(this).get(SpotViewModel.class);

        spotViewModel.getSpots().observe(this, spots -> {
            if (spots != null && !spots.isEmpty()) {
                // CAS A : IL Y A DES SPOTS
                recyclerSpotsGuide.setVisibility(View.VISIBLE);
                layoutNoData.setVisibility(View.GONE);

                spotAdapter.setSpot(spots);
            } else {
                // CAS B : C'EST VIDE (ou null)
                recyclerSpotsGuide.setVisibility(View.GONE);
                layoutNoData.setVisibility(View.VISIBLE);
            }
        });

        spotViewModel.loadSpotByPublisher(guide.getUid());
    }
}