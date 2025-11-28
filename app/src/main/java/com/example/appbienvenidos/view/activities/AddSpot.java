package com.example.appbienvenidos.view.activities;

import static androidx.core.content.ContentProviderCompat.requireContext;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.appbienvenidos.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

public class AddSpot extends AppCompatActivity {

    private static final int PICK_IMAGE = 100;

    //UI element
    private Button restaurant, cafe, paysage, culture, shopping, hotel, publier;
    private ImageButton retour;
    private TextView addimage;
    private EditText nomLieu, localisation, description;
    private LinearLayout imageContainer;
    private ImageView image_vide, image_plain;
    private FrameLayout container_photo;

    //variables
    private Uri imageSelected;
    private int selectedCategoryId=1;
    private String currentUserId;
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;
    private FirebaseStorage storage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragement_add_spot);

        //initialisation de la base de données
        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        storage = FirebaseStorage.getInstance();

        // Récupérer l'ID de l'utilisateur connecté
        if (mAuth.getCurrentUser() != null) {
            currentUserId = mAuth.getCurrentUser().getUid();
        }


        // Initialisation des vues
         initViews();

        //gestion des catégories
        selectedCategory();

        //gestion de l'image
        container_photo.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intent, PICK_IMAGE);
        });

        //gestion du btn publier
        publier.setOnClickListener(v -> startPublishProcess());

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

        image_vide = findViewById(R.id.image_vide);
        image_plain = findViewById(R.id.image_plain);

        container_photo = findViewById(R.id.container_photo);
    }
    @Override
    protected void onActivityResult(int requestCode, int responseCode, Intent data){

        super.onActivityResult(requestCode, responseCode, data);

        if(requestCode == PICK_IMAGE && responseCode == RESULT_OK && data != null){

            imageSelected = data.getData();
            if(imageSelected != null){
                image_plain.setImageURI(imageSelected);
                image_plain.setVisibility(View.VISIBLE);

                imageContainer.setVisibility(View.GONE);
            }
        }
    }
    private void startPublishProcess(){
        String title = nomLieu.getText().toString().trim();
        String map = localisation.getText().toString().trim();
        String desc = description.getText().toString().trim();

        if(title.isEmpty() || map.isEmpty() || desc.isEmpty()){
            Toast.makeText(this,"Veuillez remplir tous les champs s'il vous plaiez !", Toast.LENGTH_SHORT).show();
            return;
        }

        if(imageSelected == null){
            Toast.makeText(this,"Veuillez choisir une image !", Toast.LENGTH_SHORT).show();
            return;
        }

        //désactiver le bouton "publier" pour éviter les doubles clics
        publier.setEnabled(false);
        Toast.makeText(this, "Publication en cours...", Toast.LENGTH_SHORT).show();

        uploadImageToStorage(title, map, desc);
    }

    private void uploadImageToStorage(String title, String map, String desc){

        // On enregistre sous le nom : profile_images/ID_DU_USER.jpg
        StorageReference fileRef = storage.getReference().child("spots_images/"+ UUID.randomUUID().toString()+".jpg");

        fileRef.putFile(imageSelected)
                .addOnSuccessListener(taskSnapshot -> {
                    // Upload réussi, on demande l'URL
                    fileRef.getDownloadUrl().addOnSuccessListener(uri -> {
                        String imageUrl = uri.toString();
                        saveSpotToFirestore(title, map, desc, imageUrl);
                    });
                })
                .addOnFailureListener(e -> Toast.makeText(this, "Echec upload: " + e.getMessage(), Toast.LENGTH_SHORT).show());
        publier.setEnabled(true);
    }

    private void saveSpotToFirestore(String title, String map, String desc, String imageUrl){
        String currentDate = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(new Date());

        //création de l'objet Map pour Firestore
        Map<String, Object> spotData = new HashMap<>();
        spotData.put("title", title);
        spotData.put("description", desc);
        spotData.put("address", map);
        spotData.put("categoryId", selectedCategoryId);
        spotData.put("publisherId", currentUserId);
        spotData.put("imageUrl", imageUrl);
        spotData.put("rating", 0.0);
        spotData.put("publicationDate", currentDate);

        //envoie dans la collection spot
        db.collection("Spot").add(spotData)
                .addOnSuccessListener(documentReference -> {
                    Toast.makeText(this, "Spot publié avec succès !", Toast.LENGTH_LONG).show();
                    finish(); // Ferme la page
                }).addOnFailureListener(e -> {
                    Toast.makeText(this, "Erreur sauvegarde : "+ e.getMessage(), Toast.LENGTH_LONG).show();
                    publier.setEnabled(true);
                });
    }
    private void selectedCategory(){
        restaurant.setOnClickListener(v -> {
            restaurant.setBackgroundResource(R.drawable.bg_chip_selected_n);
            cafe.setBackgroundResource(R.drawable.bg_chip_unselected_n);
            paysage.setBackgroundResource(R.drawable.bg_chip_unselected_n);
            culture.setBackgroundResource(R.drawable.bg_chip_unselected_n);
            shopping.setBackgroundResource(R.drawable.bg_chip_unselected_n);
            hotel.setBackgroundResource(R.drawable.bg_chip_unselected_n);
            selectedCategoryId = 1;
        });

        cafe.setOnClickListener(v -> {
            restaurant.setBackgroundResource(R.drawable.bg_chip_unselected_n);
            cafe.setBackgroundResource(R.drawable.bg_chip_selected_n);
            paysage.setBackgroundResource(R.drawable.bg_chip_unselected_n);
            culture.setBackgroundResource(R.drawable.bg_chip_unselected_n);
            shopping.setBackgroundResource(R.drawable.bg_chip_unselected_n);
            hotel.setBackgroundResource(R.drawable.bg_chip_unselected_n);
            selectedCategoryId = 2;
        });

        paysage.setOnClickListener(v -> {
            restaurant.setBackgroundResource(R.drawable.bg_chip_unselected_n);
            cafe.setBackgroundResource(R.drawable.bg_chip_unselected_n);
            paysage.setBackgroundResource(R.drawable.bg_chip_selected_n);
            culture.setBackgroundResource(R.drawable.bg_chip_unselected_n);
            shopping.setBackgroundResource(R.drawable.bg_chip_unselected_n);
            hotel.setBackgroundResource(R.drawable.bg_chip_unselected_n);
            selectedCategoryId = 3;
        });

        culture.setOnClickListener(v -> {
            restaurant.setBackgroundResource(R.drawable.bg_chip_unselected_n);
            cafe.setBackgroundResource(R.drawable.bg_chip_unselected_n);
            paysage.setBackgroundResource(R.drawable.bg_chip_unselected_n);
            culture.setBackgroundResource(R.drawable.bg_chip_selected_n);
            shopping.setBackgroundResource(R.drawable.bg_chip_unselected_n);
            hotel.setBackgroundResource(R.drawable.bg_chip_unselected_n);
            selectedCategoryId = 4;
        });

        shopping.setOnClickListener(v -> {
            restaurant.setBackgroundResource(R.drawable.bg_chip_unselected_n);
            cafe.setBackgroundResource(R.drawable.bg_chip_unselected_n);
            paysage.setBackgroundResource(R.drawable.bg_chip_unselected_n);
            culture.setBackgroundResource(R.drawable.bg_chip_unselected_n);
            shopping.setBackgroundResource(R.drawable.bg_chip_selected_n);
            hotel.setBackgroundResource(R.drawable.bg_chip_unselected_n);
            selectedCategoryId = 5;
        });

        hotel.setOnClickListener(v -> {
            restaurant.setBackgroundResource(R.drawable.bg_chip_unselected_n);
            cafe.setBackgroundResource(R.drawable.bg_chip_unselected_n);
            paysage.setBackgroundResource(R.drawable.bg_chip_unselected_n);
            culture.setBackgroundResource(R.drawable.bg_chip_unselected_n);
            shopping.setBackgroundResource(R.drawable.bg_chip_unselected_n);
            hotel.setBackgroundResource(R.drawable.bg_chip_selected_n);
            selectedCategoryId = 6;
        });
    }
}
