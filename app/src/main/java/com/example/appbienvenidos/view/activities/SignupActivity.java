package com.example.appbienvenidos.view.activities;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView; // Important pour le bouton photo
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.appbienvenidos.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class SignupActivity extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;

    // UI Elements
    EditText editTextFirstName, editTextLastName, editTextEmail, editTextLocation, editTextPasswordHash;
    TextView btnRoleTraveler, btnRoleLocal, btnSinscrire;
    TextView alreadyRegistred, seConnecter;
    CardView btnChangePhoto;
    ImageView userPhotoView;

    // Variables logiques
    String selectedRole = "Voyageur";
    String imageUriString = "";

    // --- FIREBASE ---
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_signup);

        // Initialisation de Firebase
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        // Gestion des marges
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        initViews();

        // Gestion Photo
        btnChangePhoto.setOnClickListener(v -> pickImage());

        // Gestion Rôles
        btnRoleTraveler.setOnClickListener(v -> {
            selectedRole = "Voyageur";
            updateRoleUI();
        });
        btnRoleLocal.setOnClickListener(v -> {
            selectedRole = "Local";
            updateRoleUI();
        });

        // Gestion Inscription
        btnSinscrire.setOnClickListener(v -> registerUserWithFirebase());

        // Navigation vers Login
        seConnecter.setOnClickListener(v -> {
            startActivity(new Intent(SignupActivity.this, LoginActivity.class));
            finish();
        });
    }

    private void initViews() {
        editTextFirstName = findViewById(R.id.editTextFirstName);
        editTextLastName = findViewById(R.id.editTextLastName);
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextLocation = findViewById(R.id.editTextLocation);
        editTextPasswordHash = findViewById(R.id.editTextPasswordHash);

        btnRoleTraveler = findViewById(R.id.roleTraveler);
        btnRoleLocal = findViewById(R.id.roleLocal);
        btnSinscrire = findViewById(R.id.Sinscrire);

        alreadyRegistred = findViewById(R.id.alreadyRegistred);
        seConnecter = findViewById(R.id.SeConnecter);

        btnChangePhoto = findViewById(R.id.btnChangePhoto);
        userPhotoView = findViewById(R.id.user_photo);
    }

    private void updateRoleUI() {
        if (selectedRole.equals("Voyageur")) {
            btnRoleTraveler.setBackgroundResource(R.drawable.bg_role_active);
            btnRoleTraveler.setTextColor(Color.WHITE);
            btnRoleLocal.setBackgroundResource(0);
            btnRoleLocal.setTextColor(Color.parseColor("#8D6E63"));

        } else {
            btnRoleTraveler.setBackgroundResource(0);
            btnRoleTraveler.setTextColor(Color.parseColor("#8D6E63"));
            btnRoleLocal.setBackgroundResource(R.drawable.bg_role_active);
            btnRoleLocal.setTextColor(Color.WHITE);
        }
    }

    private void pickImage() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri imageUri = data.getData();
            imageUriString = imageUri.toString();
            userPhotoView.setImageURI(imageUri);
            userPhotoView.setColorFilter(null);
            userPhotoView.setPadding(0, 0, 0, 0);
            userPhotoView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            userPhotoView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        }
    }

    // --- C'EST ICI QUE TOUT SE PASSE ---
    private void registerUserWithFirebase() {
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPasswordHash.getText().toString().trim();
        String firstName = editTextFirstName.getText().toString().trim();
        String lastName = editTextLastName.getText().toString().trim();
        String location = editTextLocation.getText().toString().trim();

        // 1. Validation basique
        if (email.isEmpty() || password.isEmpty() || firstName.isEmpty()) {
            Toast.makeText(this, "Remplissez tous les champs !", Toast.LENGTH_SHORT).show();
            return;
        }

        if (password.length() < 6) {
            Toast.makeText(this, "Le mot de passe doit faire 6 caractères min.", Toast.LENGTH_SHORT).show();
            return;
        }

        // 2. Création du compte AUTH (Email/Password)
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnSuccessListener(authResult -> {
                    // SUCCÈS : Le compte existe, on a un ID (uid)
                    FirebaseUser user = mAuth.getCurrentUser();
                    String uid = user.getUid();

                    // 3. Préparation des données pour FIRESTORE
                    // On utilise une Map (Clé -> Valeur) c'est plus simple que l'objet User pour commencer
                    Map<String, Object> userMap = new HashMap<>();
                    userMap.put("firstName", firstName);
                    userMap.put("lastName", lastName);
                    userMap.put("email", email);
                    userMap.put("role", selectedRole);
                    userMap.put("location", location);
                    userMap.put("photoUrl", imageUriString); // Note: Idéalement il faut uploader l'image dans Storage

                    // 4. Enregistrement dans la collection "users"
                    db.collection("users").document(uid).set(userMap)
                            .addOnSuccessListener(aVoid -> {
                                // TOUT EST BON !
                                Toast.makeText(SignupActivity.this, "Compte créé avec succès !", Toast.LENGTH_SHORT).show();

                                // On redirige vers l'accueil ou le login
                                startActivity(new Intent(SignupActivity.this, MainActivity.class)); // ou LoginActivity
                                finish();
                            })
                            .addOnFailureListener(e -> {
                                Toast.makeText(SignupActivity.this, "Erreur sauvegarde données : " + e.getMessage(), Toast.LENGTH_LONG).show();
                            });

                })
                .addOnFailureListener(e -> {
                    // ERREUR (Email déjà pris, pas internet, etc.)
                    Toast.makeText(SignupActivity.this, "Erreur : " + e.getMessage(), Toast.LENGTH_LONG).show();
                });
    }
}