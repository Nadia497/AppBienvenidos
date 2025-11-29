package com.example.appbienvenidos.view.activities;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager; // Important pour la map
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import com.example.appbienvenidos.R;
import com.google.android.material.button.MaterialButton;
// Imports pour la carte (OSMDroid)
import org.osmdroid.config.Configuration;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
public class SpotDetailActivity extends AppCompatActivity {
    // Déclaration des variables
    LinearLayout LayoutImage, actions_ask_or_share, layoutInfosMap;
    MaterialButton AskUser, Share;

    ImageView FirstImage, SecondImage, ThirdImage;
    TextView NomDuSpot, SmallDescription, UserPublicationDate, textviewaction,
            textviewDescriptionwithdetails, Descriptionwithdetails,
            Informationspratique, infoAdresse, infoHeures, infoContact;

    RatingBar ratingbar;

    MapView map;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 1. Configuration d'OSMDroid (
        // Cela permet à la carte de charger les tuiles (images de la carte)
        Context ctx = getApplicationContext();
        Configuration.getInstance().load(ctx, PreferenceManager.getDefaultSharedPreferences(ctx));
        Configuration.getInstance().setUserAgentValue(getPackageName());

        EdgeToEdge.enable(this);
        setContentView(R.layout.item_spot);

        // Gestion des marges pour les barres système
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.Spot), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // 2. Initialisation des Vues (Liaison avec le XML)
        initViews();

        // 3. Récupération des données envoyées depuis l'activité précédente
        getAndSetData();

        // 4. Configuration de la carte (Zoom, point de départ)
        setupMap();

        // 5. Gestion des clics sur les boutons
        setupButtons();
    }

    private void initViews() {
        LayoutImage = findViewById(R.id.LayoutImage);
        textviewaction = findViewById(R.id.textviewaction);
        //layoutInfosMap = findViewById(R.id.layoutInfosMap);

        AskUser = findViewById(R.id.AskUser);
        Share = findViewById(R.id.Share);

        FirstImage = findViewById(R.id.FirstImage);
        SecondImage = findViewById(R.id.SecondImage);
        ThirdImage = findViewById(R.id.ThirdImage);


        Informationspratique = findViewById(R.id.Informationspratique);
        Descriptionwithdetails = findViewById(R.id.Descriptionwithdetails);

        ratingbar = findViewById(R.id.ratingbar);

        textviewaction = findViewById(R.id.textviewaction);
        textviewDescriptionwithdetails = findViewById(R.id.textviewDescriptionwithdetails);

        map = findViewById(R.id.map);
        NomDuSpot = findViewById(R.id.NomDuSpot);
        UserPublicationDate = findViewById(R.id.UserPublicationDate);
    }

    private void getAndSetData() {
        // On récupère l'intent
        Intent intent = getIntent();

        if (intent != null) {
            // Textes
            String nom = intent.getStringExtra("NomDuSpot_key");
            String descCourt = intent.getStringExtra("SmallDescription_key");
            String descLong = intent.getStringExtra("Descriptionwithdetails_key");
            String datePub = intent.getStringExtra("UserPublicationDate_key");
            String adresse = intent.getStringExtra("Adresse_key");
            String heures = intent.getStringExtra("Heures_key");
            String prix = intent.getStringExtra("Prix_key");

            // Note (Rating)
            float note = intent.getFloatExtra("Rating_key", 0f);

            // Assignation aux
            if (nom != null) NomDuSpot.setText(nom);
            if (descCourt != null) SmallDescription.setText(descCourt);
            if (descLong != null) Descriptionwithdetails.setText(descLong);
            if (datePub != null) UserPublicationDate.setText(datePub);

            // Infos du spot
            if (adresse != null) infoAdresse.setText("Adresse : " + adresse);
            if (heures != null) infoHeures.setText("Horaires : " + heures);
            if (prix != null) infoContact.setText("Prix : " + prix);

            // Mise à jour de la barre d'étoiles
            ratingbar.setRating(note);
        }
    }

    private void setupMap() {
        map.setMultiTouchControls(true);

        double lat = getIntent().getDoubleExtra("Lat_key", 48.8583);
        double lon = getIntent().getDoubleExtra("Lon_key", 2.2944);

        GeoPoint startPoint = new GeoPoint(lat, lon);
        map.getController().setZoom(15.0);
        map.getController().setCenter(startPoint);
    }

    private void setupButtons() {
        AskUser.setOnClickListener(v -> {

            Toast.makeText(SpotDetailActivity.this, "Ouvrir le chat...", Toast.LENGTH_SHORT).show();

        });

        Share.setOnClickListener(v -> {
            try {
                // --- TEST : On met un texte FIXE. N'utilise pas NomDuSpot ici ---
                String message = "Ceci est un test de partage";
                // ---------------------------------------------------------------

                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, message);
                sendIntent.setType("text/plain");

                Intent shareIntent = Intent.createChooser(sendIntent, "Partager via");
                startActivity(shareIntent);

            } catch (Exception e) {
                Toast.makeText(SpotDetailActivity.this, "Erreur: " + e.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    // Gérer le cycle de vie de la Map pour économiser la batterie
    @Override
    public void onResume() {
        super.onResume();
        map.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        map.onPause();
    }
}