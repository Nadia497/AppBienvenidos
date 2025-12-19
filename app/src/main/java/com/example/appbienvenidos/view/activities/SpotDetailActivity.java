package com.example.appbienvenidos.view.activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.example.appbienvenidos.R;
import com.example.appbienvenidos.model.Spot;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.imageview.ShapeableImageView;
import com.example.appbienvenidos.viewmodel.SpotViewModel ;

import org.osmdroid.config.Configuration;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class SpotDetailActivity extends BaseActivity {

    // --- 1. Variables UI ---
    LinearLayout LayoutImage;
    MaterialButton AskUser, Share;
    ShapeableImageView FirstImage, SecondImage, ThirdImage;
    TextView NomDuSpot, UserPublicationDate, textviewaction,
            textviewDescription, Description, SpotAdress, txtAverageScore,
            infoAdresse;
    RatingBar ratingbar;
    MapView map;
    SpotViewModel spotViewModel ;


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
            Toast.makeText(this, getString(R.string.no_spot), Toast.LENGTH_SHORT).show();
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

        txtAverageScore = findViewById(R.id.txtAverageScore);
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

        // --- GESTION DU RATING ---

        // 1. Initialiser le ViewModel
        spotViewModel = new androidx.lifecycle.ViewModelProvider(this).get(com.example.appbienvenidos.viewmodel.SpotViewModel.class);

        // 2. Afficher la moyenne actuelle
        float moyenne = (float) currentSpot.getAverage_Rating();
        ratingbar.setRating(moyenne);
        ratingbar.setIsIndicator(false); // Important : on autorise le clic !

        if(txtAverageScore != null) {
            txtAverageScore.setText(String.format("%.1f", moyenne));
        }

        // 3. Écouter le clic sur les étoiles
        ratingbar.setOnRatingBarChangeListener((ratingBar, userRating, fromUser) -> {
            if (fromUser) {
                // On bloque la barre pour ne pas voter 2 fois
                ratingbar.setIsIndicator(true);

                // Calcul mathématique de la nouvelle moyenne (Simulation visuelle immédiate)
                double oldAvg = currentSpot.getAverage_Rating();
                double oldCount = currentSpot.getTotal_Rating();
                double newCount = oldCount + 1;
                double newAvg = ((oldAvg * oldCount) + userRating) / newCount;

                // Mise à jour visuelle
                if(txtAverageScore != null) {
                    txtAverageScore.setText(String.format("%.1f", newAvg));
                }
                Toast.makeText(this,  getString(R.string.rate) + userRating + "/5", Toast.LENGTH_SHORT).show();

                // Envoi à Firebase
                spotViewModel.rateSpot(currentSpot.getId(), userRating, oldAvg, oldCount);
            }
        });
    }

    private void setupImages() {
        List<String> listePhotos = currentSpot.getImage_URL();

        // 1. On cache tout le monde au début
        FirstImage.setVisibility(View.GONE);
        SecondImage.setVisibility(View.GONE);
        ThirdImage.setVisibility(View.GONE);

        if (listePhotos == null || listePhotos.isEmpty()) {
            LayoutImage.setVisibility(View.GONE);
            return;
        }
        LayoutImage.setVisibility(View.VISIBLE);

        int nombre = listePhotos.size();

        // 2. On affiche et configure seulement celles qui existent
        if (nombre >= 1) {
            configureImage(FirstImage, listePhotos.get(0), 0, listePhotos);
        }
        if (nombre >= 2) {
            configureImage(SecondImage, listePhotos.get(1), 1, listePhotos);
        }
        if (nombre >= 3) {
            configureImage(ThirdImage, listePhotos.get(2), 2, listePhotos);
        }
    }

    private void configureImage(ShapeableImageView imageView, String url, int position, List<String> allPhotos) {
        imageView.setVisibility(View.VISIBLE);

        // --- LA PARTIE IMPORTANTE POUR LA TAILLE ---
        // On récupère les règles de mise en page de l'image
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) imageView.getLayoutParams();

        // On dit : "Ta largeur est flexible (0dp)"
        params.width = 0;

        // On dit : "Tu pèses 1 unité".
        // Si tu es seule, tu prends 100% de la place.
        // Si vous êtes deux, vous prenez 50% chacune.
        params.weight = 1;

        // On applique les nouvelles règles
        imageView.setLayoutParams(params);
        // -------------------------------------------

        // Chargement avec Glide
        Glide.with(this)
                .load(url)
                .centerCrop()
                .placeholder(R.drawable.ic_launcher_background)
                .into(imageView);

        // Clic pour agrandir
        imageView.setOnClickListener(v -> {
            Intent intent = new Intent(SpotDetailActivity.this, FullScreenActivity.class);
            intent.putStringArrayListExtra("IMAGES_LIST", new ArrayList<>(allPhotos));
            intent.putExtra("SELECTED_POSITION", position);
            startActivity(intent);
        });
    }
    private void setupMap() {
        if (map == null) return;

        // On bloque les mouvements sur la petite carte pour éviter les conflits avec le scroll de la page
        map.setMultiTouchControls(false);

        double lat = currentSpot.getLatitude();
        double lon = currentSpot.getLongitude();


        GeoPoint spotPoint = new GeoPoint(lat, lon);
        map.getController().setZoom(16.0);
        map.getController().setCenter(spotPoint);

        Marker startMarker = new Marker(map);
        startMarker.setPosition(spotPoint);
        startMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
        startMarker.setTitle(currentSpot.getTitle());

        try {
            // 1. On charge ton image originale
            Drawable logoOriginal = ContextCompat.getDrawable(this, R.drawable.logomap);

            // 2. On la convertit en Bitmap pour pouvoir la modifier
            Bitmap bitmapOriginal = ((BitmapDrawable) logoOriginal).getBitmap();

            // 3. On la redimensionne
            Drawable petitLogo = new BitmapDrawable(getResources(),
                    Bitmap.createScaledBitmap(bitmapOriginal, 30, 30, true));

            startMarker.setIcon(petitLogo);

        } catch (Exception e) {
            e.printStackTrace();
        }


        map.getOverlays().clear();
        map.getOverlays().add(startMarker);
        map.invalidate();

        View mapOverlay = findViewById(R.id.mapOverlay);

        mapOverlay.setOnClickListener(v -> {
            Intent intent = new Intent(SpotDetailActivity.this, FullMapActivity.class);
            intent.putExtra("LAT", currentSpot.getLatitude());
            intent.putExtra("LON", currentSpot.getLongitude());
            intent.putExtra("TITLE", currentSpot.getTitle());
            startActivity(intent);
        });


    }
    private void setupButtons() {
        AskUser.setOnClickListener(v -> {
            String idCreateur = currentSpot.getPublisher_id();

            com.google.firebase.firestore.FirebaseFirestore.getInstance()
                    .collection("Guide")
                    .document(idCreateur)
                    .get()
                    .addOnSuccessListener(document -> {
                        if (document.exists()) {
                            // On récupère les infos
                            String email = document.getString("email");
                            String telephone = document.getString("phoneNumber");

                            // 2. On affiche la fenêtre de choix
                            showContactChoiceDialog(email, telephone);

                        } else {
                            Toast.makeText(this, "Utilisateur introuvable", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(e -> Toast.makeText(this, "Erreur connexion", Toast.LENGTH_SHORT).show());
            Toast.makeText(SpotDetailActivity.this, getString(R.string.open_chat), Toast.LENGTH_SHORT).show();
        });

        Share.setOnClickListener(v -> {
            try {
                String nom = currentSpot.getTitle();
                String message = getString(R.string.partage_msg) + nom;

                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, message);
                sendIntent.setType("text/plain");

                Intent shareIntent = Intent.createChooser(sendIntent, getString(R.string.send));
                startActivity(shareIntent);

                //notification interne
                creerNotificationInterne("a partager votre spot");

            } catch (Exception e) {
                Toast.makeText(SpotDetailActivity.this, getString(R.string.send_error), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showContactChoiceDialog(String email, String telephone) {

        // On prépare la liste des options disponibles
        List<String> options = new ArrayList<>();
        final List<String> actions = new ArrayList<>();

        // Option Email
        if (email != null && !email.isEmpty()) {
            options.add("📧 Envoyer un Email");
            actions.add("EMAIL");
        }

        // Option SMS
        if (telephone != null && !telephone.isEmpty()) {
            options.add("💬 Envoyer un SMS");
            actions.add("SMS");
        }

        if (options.isEmpty()) {
            Toast.makeText(this, "Aucun moyen de contact disponible", Toast.LENGTH_SHORT).show();
            return;
        }

        // Conversion en tableau pour le Dialog
        String[] optionsArray = options.toArray(new String[0]);

        // Création de la fenêtre
        new android.app.AlertDialog.Builder(this)
                .setTitle("Contacter le créateur")
                .setItems(optionsArray, (dialog, which) -> {

                    String actionChoisie = actions.get(which);

                    if (actionChoisie.equals("EMAIL")) {
                        sendEmail(email);
                    } else if (actionChoisie.equals("SMS")) {
                        sendSMS(telephone);
                    }
                })
                .show();
    }

    // --- ACTION EMAIL ---
    private void sendEmail(String email) {
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(android.net.Uri.parse("mailto:"));
        intent.putExtra(Intent.EXTRA_EMAIL, new String[]{email});
        intent.putExtra(Intent.EXTRA_SUBJECT, "Question sur : " + currentSpot.getTitle());
        try {
            startActivity(intent);
        } catch (Exception e) {
            Toast.makeText(this, "Pas d'appli mail trouvée", Toast.LENGTH_SHORT).show();
        }
    }

    // --- ACTION SMS ---
    private void sendSMS(String telephone) {
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(android.net.Uri.parse("smsto:" + telephone));
        intent.putExtra("sms_body", "Bonjour, j'ai une question sur votre spot : " + currentSpot.getTitle());
        try {
            startActivity(intent);
        } catch (Exception e) {
            Toast.makeText(this, "Pas d'appli SMS trouvée", Toast.LENGTH_SHORT).show();
        }
    }

    private void creerNotificationInterne(String actions){
        String publisherId = currentSpot.getPublisher_id();
        String currentUserId = com.google.firebase.auth.FirebaseAuth.getInstance().getCurrentUser().getUid();

        if(publisherId != null && !publisherId.equals(currentUserId)){
            com.google.firebase.firestore.FirebaseFirestore db =
                com.google.firebase.firestore.FirebaseFirestore.getInstance();

            db.collection("users").document(currentUserId).get().addOnSuccessListener(doc -> {
                String name = "";
                if(doc.exists() && doc.getString("firstName") != null){
                    name = doc.getString("firstName") + " " + doc.getString("lastName");
                }

                com.example.appbienvenidos.model.Notifications notif = new com.example.appbienvenidos.model.Notifications(
                        publisherId,
                        name,
                        actions,
                        currentSpot.getTitle()
                );

                db.collection("Notifications").add(notif);
            });
        }
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