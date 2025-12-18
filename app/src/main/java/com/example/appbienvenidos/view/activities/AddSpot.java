package com.example.appbienvenidos.view.activities;

import android.content.Intent;
import java.util.ArrayList;

import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.example.appbienvenidos.viewmodel.AddSpotViewmodel;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.appbienvenidos.R;
import com.google.firebase.auth.FirebaseAuth;

import java.io.IOException;
import java.util.List;
import java.util.Random;

//pour la localisation
import android.location.Address;
import android.location.Geocoder;
import java.util.Locale;

public class AddSpot extends BaseActivity {

    private static final int PICK_IMAGE = 100;

    //UI element
    private Button restaurant, cafe, paysage, culture, shopping, hotel, publier;
    private ImageButton retour;
    private TextView addimage, aideEcrire;
    private EditText nomLieu, localisation, description;

    //pour la localisation
    private double selectedLat = 0.0;
    private double selectedLng = 0.0;
    private boolean isLocationValid = false;
    private LinearLayout imageContainer, Spotimgs;
    private View image_plain;
    private FrameLayout container_photo;

    //variables
    private List<Uri> imagesSelected = new ArrayList<>(); //liste pour stocker les images choisie par le user
    private String selectedCategoryId="kzQGD3FoEiqb0w4ojEoR";
    private String currentUserId;
    private FirebaseAuth mAuth;

    private AddSpotViewmodel viewModel;

    // 1. La liste des 30 phrases
    private final String[] suggestionsDescription = {
            "Ce lieu est absolument magique, une expérience inoubliable !",
            "L'atmosphère ici est très apaisante, parfait pour se détendre.",
            "Un endroit incontournable si vous visitez la région.",
            "La vue est à couper le souffle, surtout au coucher du soleil.",
            "Le service est impeccable et le personnel très accueillant.",
            "C'est un véritable petit coin de paradis caché.",
            "Idéal pour une sortie en famille, les enfants ont adoré.",
            "Les saveurs sont authentiques et les prix très raisonnables.",
            "J'ai adoré l'architecture et l'histoire de ce lieu.",
            "Un spot parfait pour prendre des photos magnifiques.",
            "L'ambiance est festive et chaleureuse, je recommande vivement !",
            "C'est l'endroit rêvé pour les amoureux de la nature.",
            "Une belle découverte, je reviendrai sans hésiter.",
            "Le calme absolu, loin de l'agitation de la ville.",
            "Un lieu chargé d'émotion et de beauté.",
            "Parfait pour une pause café ou un déjeuner rapide.",
            "Les couleurs et les lumières ici sont incroyables.",
            "Une expérience culturelle enrichissante.",
            "Le meilleur endroit de la ville pour se ressourcer.",
            "Tout était parfait, du début à la fin.",
            "Un cadre romantique idéal pour les couples.",
            "C'est un lieu atypique qui vaut le détour.",
            "L'énergie qui se dégage de cet endroit est unique.",
            "Les produits sont frais et de grande qualité.",
            "Un excellent rapport qualité-prix.",
            "C'est mon nouvel endroit préféré !",
            "L'accès est facile et il y a tout ce qu'il faut sur place.",
            "Une architecture époustouflante qui laisse sans voix.",
            "L'endroit est très propre et bien entretenu.",
            "Je recommande ce lieu à 100% pour son authenticité."
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragement_add_spot);

        viewModel = new ViewModelProvider(this).get(AddSpotViewmodel.class);
        //initialisation de la base de données
        mAuth = FirebaseAuth.getInstance();

        // Récupérer l'ID de l'utilisateur connecté
        if (mAuth.getCurrentUser() != null) {
            currentUserId = mAuth.getCurrentUser().getUid();
        }


        // Initialisation des vues
         initViews();

        //gestion de la recherche gratitude
        localisation.setOnTouchListener((v, event) -> {
            final int DRAWABLE_RIGHT =2; //index de l'icône de droite

            if(event.getAction() == MotionEvent.ACTION_UP){
                if (event.getRawX() >= (localisation.getRight() - localisation.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())){
                    String adresseTapee = localisation.getText().toString().trim();
                    if (!adresseTapee.isEmpty()){
                        chercherLocalisation(adresseTapee);
                    } else {
                        Toast.makeText(AddSpot.this, "Entrez une ville ou un lieu", Toast.LENGTH_SHORT).show();
                    }
                    return true;
                }
            }
            return false;
        });

        //Configuration des observers
        setupObservers();

        //gestion des catégories
        selectedCategory();

        //gestion de l'image
        container_photo.setOnClickListener(v -> {
            // ACTION_GET_CONTENT est souvent mieux pour le multi-pick
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            intent.setType("image/*");
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
            startActivityForResult(Intent.createChooser(intent,"Sélectionner des photos"), PICK_IMAGE);
        });

        aideEcrire.setOnClickListener(v -> generateRandomDescription());

        //gestion du btn publier
        publier.setOnClickListener(v -> {
            if(!isLocationValid){
                Toast.makeText(AddSpot.this, "Veuillez cliquer sur la loupe pour valider la localisation", Toast.LENGTH_SHORT).show();
                return;
            }
            startPublishProcess();
        });

        //bouton de retour
        retour.setOnClickListener(v -> finish());
    }
    private void initViews(){
        retour = findViewById(R.id.btnBack);
        restaurant = findViewById(R.id.restaurant);
        cafe = findViewById(R.id.cafe);
        paysage = findViewById(R.id.paysage);
        culture = findViewById(R.id.culture);
        shopping = findViewById(R.id.shopping);
        hotel = findViewById(R.id.hotel);
        publier = findViewById(R.id.btnpublier);

        nomLieu = findViewById(R.id.name);
        localisation = findViewById(R.id.location);
        description = findViewById(R.id.description);

        addimage = findViewById(R.id.addimg);

        imageContainer = findViewById(R.id.imageContainer);

        Spotimgs = findViewById(R.id.image_container);

        image_plain = findViewById(R.id.image_plain);

        container_photo = findViewById(R.id.container_photo);
        aideEcrire = findViewById(R.id.ai);
    }
    private void setupObservers(){
        viewModel.getIsLoading().observe(this, isLoading -> {
            publier.setEnabled(!isLoading); // On désactive le bouton pendant le chargement
            if (isLoading) {
                Toast.makeText(AddSpot.this, "Publication en cours...", Toast.LENGTH_SHORT).show();
            }
        });

        // 2. Gestion des messages (Succès ou Erreur)
        viewModel.getToastMessage().observe(this, message -> {
            Toast.makeText(AddSpot.this, message, Toast.LENGTH_SHORT).show();
        });

        // 3. Gestion de la fin (Fermer l'activité si succès)
        viewModel.getIsPublished().observe(this, isPublished -> {
            if (isPublished) {
                finish(); // On ferme la page et on revient à l'écran précédent
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int responseCode, Intent data){

        super.onActivityResult(requestCode, responseCode, data);

        if(requestCode == PICK_IMAGE && responseCode == RESULT_OK && data != null){

            //cas1 : user a choisi plusieurs images
            if(data.getClipData() != null) {
                int count = data.getClipData().getItemCount();
                for (int i = 0; i < count; i++) {
                    Uri imageUri = data.getClipData().getItemAt(i).getUri();
                    imagesSelected.add(imageUri);
                    afficherImageDansLaListe(imageUri);
                }
            }
            //cas2 : user a choisi une seule image
            else if(data.getData() != null){
                    Uri imageUri = data.getData();
                    imagesSelected.add(imageUri);
                    afficherImageDansLaListe(imageUri);
                }

            if(!imagesSelected.isEmpty()){
                imageContainer.setVisibility(View.GONE);
                image_plain.setVisibility(View.VISIBLE);
            }
            }
        }

    private void afficherImageDansLaListe(Uri uri){
        ImageView imgview = new ImageView(this);

        // Paramètres de l'image (taille carrée 150dp par exemple)
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(400, 400);
        layoutParams.setMargins(10, 0, 10, 0);
        imgview.setLayoutParams(layoutParams);

        imgview.setScaleType(ImageView.ScaleType.CENTER_CROP);
        imgview.setImageURI(uri); // Ou utiliser Glide pour plus de performance

        // Ajouter l'image au conteneur linéaire
        Spotimgs.addView(imgview);
    }
    private void startPublishProcess(){
        String title = nomLieu.getText().toString().trim();
        String map = localisation.getText().toString().trim();
        String desc = description.getText().toString().trim();


        viewModel.publishSpot(title, map, desc,selectedLat, selectedLng, imagesSelected, selectedCategoryId, currentUserId);
    }

    private void selectedCategory(){
        restaurant.setOnClickListener(v -> {
            restaurant.setBackgroundResource(R.drawable.bg_chip_selected_n);
            cafe.setBackgroundResource(R.drawable.bg_chip_unselected_n);
            paysage.setBackgroundResource(R.drawable.bg_chip_unselected_n);
            culture.setBackgroundResource(R.drawable.bg_chip_unselected_n);
            shopping.setBackgroundResource(R.drawable.bg_chip_unselected_n);
            hotel.setBackgroundResource(R.drawable.bg_chip_unselected_n);
            selectedCategoryId = "kzQGD3FoEiqb0w4ojEoR";
        });

        cafe.setOnClickListener(v -> {
            restaurant.setBackgroundResource(R.drawable.bg_chip_unselected_n);
            cafe.setBackgroundResource(R.drawable.bg_chip_selected_n);
            paysage.setBackgroundResource(R.drawable.bg_chip_unselected_n);
            culture.setBackgroundResource(R.drawable.bg_chip_unselected_n);
            shopping.setBackgroundResource(R.drawable.bg_chip_unselected_n);
            hotel.setBackgroundResource(R.drawable.bg_chip_unselected_n);
            selectedCategoryId = "5FQ5xnOM63VR0jCEaUgx";
        });

        paysage.setOnClickListener(v -> {
            restaurant.setBackgroundResource(R.drawable.bg_chip_unselected_n);
            cafe.setBackgroundResource(R.drawable.bg_chip_unselected_n);
            paysage.setBackgroundResource(R.drawable.bg_chip_selected_n);
            culture.setBackgroundResource(R.drawable.bg_chip_unselected_n);
            shopping.setBackgroundResource(R.drawable.bg_chip_unselected_n);
            hotel.setBackgroundResource(R.drawable.bg_chip_unselected_n);
            selectedCategoryId = "HBgPMXEAwwRo0jxkOFBl";
        });

        culture.setOnClickListener(v -> {
            restaurant.setBackgroundResource(R.drawable.bg_chip_unselected_n);
            cafe.setBackgroundResource(R.drawable.bg_chip_unselected_n);
            paysage.setBackgroundResource(R.drawable.bg_chip_unselected_n);
            culture.setBackgroundResource(R.drawable.bg_chip_selected_n);
            shopping.setBackgroundResource(R.drawable.bg_chip_unselected_n);
            hotel.setBackgroundResource(R.drawable.bg_chip_unselected_n);
            selectedCategoryId = "NTI14ykiVgCF6a9FdRAA";
        });

        shopping.setOnClickListener(v -> {
            restaurant.setBackgroundResource(R.drawable.bg_chip_unselected_n);
            cafe.setBackgroundResource(R.drawable.bg_chip_unselected_n);
            paysage.setBackgroundResource(R.drawable.bg_chip_unselected_n);
            culture.setBackgroundResource(R.drawable.bg_chip_unselected_n);
            shopping.setBackgroundResource(R.drawable.bg_chip_selected_n);
            hotel.setBackgroundResource(R.drawable.bg_chip_unselected_n);
            selectedCategoryId = "XBYKfhJM221pCHY9a1oM";
        });

        hotel.setOnClickListener(v -> {
            restaurant.setBackgroundResource(R.drawable.bg_chip_unselected_n);
            cafe.setBackgroundResource(R.drawable.bg_chip_unselected_n);
            paysage.setBackgroundResource(R.drawable.bg_chip_unselected_n);
            culture.setBackgroundResource(R.drawable.bg_chip_unselected_n);
            shopping.setBackgroundResource(R.drawable.bg_chip_unselected_n);
            hotel.setBackgroundResource(R.drawable.bg_chip_selected_n);
            selectedCategoryId = "Z3276a3S3WXTurMqmqPO";
        });
    }

    private void generateRandomDescription(){
        Random random = new Random();
        int i = random.nextInt(suggestionsDescription.length);

        String phraseChoisie = suggestionsDescription[i];

        description.setText(phraseChoisie);
    }

    //fonction pour la gratitude
    private void chercherLocalisation(String adresse){
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());

        new Thread(() -> {
            try{
                List<Address> addresses = geocoder.getFromLocationName(adresse, 1);

                runOnUiThread(() -> {
                    if(addresses != null && !addresses.isEmpty()){
                        Address foundAddress = addresses.get(0);

                        //on sauvegarde les coordonnées
                        selectedLat = foundAddress.getLatitude();
                        selectedLng = foundAddress.getLongitude();
                        isLocationValid = true;

                        // On met à jour le champ texte avec l'adresse complète trouvée par Android
                        // Ex: Si user tape "Majorelle", ça devient "Jardin Majorelle, Rue Yves St Laurent, Marrakech..."
                        String cleanAddress = "";
                        if(foundAddress.getAddressLine(0) != null){
                            cleanAddress = foundAddress.getAddressLine(0);
                        } else {
                            cleanAddress = foundAddress.getLocality() + "," +foundAddress.getCountryName();
                        }

                        localisation.setText(cleanAddress);
                        Toast.makeText(this,"Localisation trouvée ! ✅", Toast.LENGTH_SHORT).show();
                    } else {
                        isLocationValid = false;
                        Toast.makeText(this,"Lieu introuvable ❌ Essayez d'être plus précis (Ajoutez la ville)", Toast.LENGTH_SHORT).show();
                    }
                });
            } catch(IOException e){
                runOnUiThread(() -> {
                    isLocationValid = false;
                    Toast.makeText(this,"Erreur de connexion internet", Toast.LENGTH_SHORT).show();
                });
            }
        }).start();
    }
}
