package com.example.appbienvenidos.view.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.example.appbienvenidos.R;
import com.example.appbienvenidos.model.Spot;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.imageview.ShapeableImageView;

import org.osmdroid.config.Configuration;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;

import java.util.List;

public class SpotDetailActivity extends AppCompatActivity {

    // --- 1. Variables UI ---
    LinearLayout LayoutImage;
    MaterialButton AskUser, Share;
    ShapeableImageView FirstImage, SecondImage, ThirdImage;
    TextView NomDuSpot, UserPublicationDate, textviewaction,
            textviewDescription, Description, SpotAdress,
            infoAdresse;
    RatingBar ratingbar;
    MapView map;


    private Spot currentSpot;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Configuration OSMDroid (Carte)
        Context ctx = getApplicationContext();
        Configuration.getInstance().load(ctx, PreferenceManager.getDefaultSharedPreferences(ctx));
        Configuration.getInstance().setUserAgentValue(getPackageName());

        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_spot);

        // Marges système
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.Spot), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // A. Initialisation des liens XML
        initViews();


        currentSpot = (Spot) getIntent().getSerializableExtra("SPOT_KEY");

        if (currentSpot != null) {
            // C. Remplissage des textes
            fillData();

            // D. Affichage des images
            setupImages();

            // E. Configuration de la carte
            setupMap();

            // F. Configuration des boutons
            setupButtons();

        } else {
            Toast.makeText(this, "Erreur : Spot introuvable", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private void initViews() {
        LayoutImage = findViewById(R.id.LayoutImage);

        AskUser = findViewById(R.id.AskUser);
        Share = findViewById(R.id.Share);

        FirstImage = findViewById(R.id.FirstImage);
        SecondImage = findViewById(R.id.SecondImage);
        ThirdImage = findViewById(R.id.ThirdImage);

        Description = findViewById(R.id.Description);
        ratingbar = findViewById(R.id.ratingbar);


        textviewaction = findViewById(R.id.textviewaction);
        textviewDescription = findViewById(R.id.textviewDescription);

        map = findViewById(R.id.map);
        NomDuSpot = findViewById(R.id.NomDuSpot);
        SpotAdress = findViewById(R.id.SpotAdress);
        UserPublicationDate = findViewById(R.id.UserPublicationDate);
    }

    private void fillData() {

        NomDuSpot.setText(currentSpot.getTitle());
        Description.setText(currentSpot.getDescription());
        UserPublicationDate.setText(currentSpot.getPublication_Date());
        SpotAdress.setText(currentSpot.getAdress());

        if (infoAdresse != null) {
            infoAdresse.setText(currentSpot.getAdress());
        }

        ratingbar.setRating((float) currentSpot.getAverage_Rating());
    }

    private void setupImages() {

        List<String> listePhotos = currentSpot.getImage_URL();

        FirstImage.setVisibility(View.GONE);
        SecondImage.setVisibility(View.GONE);
        ThirdImage.setVisibility(View.GONE);

        if (listePhotos == null || listePhotos.isEmpty()) {
            return;
        }

        int nombrePhoto = listePhotos.size();

        if (nombrePhoto >= 1) {
            FirstImage.setVisibility(View.VISIBLE);
            Glide.with(this).load(listePhotos.get(0)).centerCrop().into(FirstImage);
        }

        if (nombrePhoto >= 2) {
            SecondImage.setVisibility(View.VISIBLE);
            Glide.with(this).load(listePhotos.get(1)).centerCrop().into(SecondImage);
        }

        // Photo 3
        if (nombrePhoto >= 3) {
            ThirdImage.setVisibility(View.VISIBLE);
            Glide.with(this).load(listePhotos.get(2)).centerCrop().into(ThirdImage);
        }
    }

    private void setupMap() {
        if (map == null) return;

        map.setMultiTouchControls(true);

        double lat = 31.6295; // Marrakech par défaut
        double lon = -7.9811;

        GeoPoint startPoint = new GeoPoint(lat, lon);
        map.getController().setZoom(15.0);
        map.getController().setCenter(startPoint);
    }

    private void setupButtons() {
        AskUser.setOnClickListener(v -> {
            Toast.makeText(SpotDetailActivity.this, "Ouvrir le chat avec le créateur...", Toast.LENGTH_SHORT).show();
        });

        Share.setOnClickListener(v -> {
            try {
                String nom = currentSpot.getTitle();
                String message = "Regarde ce spot incroyable sur Bienvenidos : " + nom;

                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, message);
                sendIntent.setType("text/plain");

                Intent shareIntent = Intent.createChooser(sendIntent, "Partager via");
                startActivity(shareIntent);

            } catch (Exception e) {
                Toast.makeText(SpotDetailActivity.this, "Erreur de partage", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        if (map != null) map.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        if (map != null) map.onPause();
    }
}