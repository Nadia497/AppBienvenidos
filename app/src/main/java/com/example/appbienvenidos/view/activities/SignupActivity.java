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

import androidx.core.content.ContextCompat;
import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView; // Important pour le bouton photo
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;

import com.cloudinary.android.MediaManager;
import com.example.appbienvenidos.R;
import com.example.appbienvenidos.viewmodel.SignupViewModel;
import com.example.appbienvenidos.repository.AuthRepository;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;


public class SignupActivity extends BaseActivity {

    private static final int PICK_IMAGE_REQUEST = 1;

    // UI Elements
    EditText editTextFirstName, editTextLastName, editTextEmail, editTextLocation, editTextPasswordHash;
    TextView btnRoleTraveler, btnRoleLocal, btnSinscrire;
    TextView alreadyRegistred, seConnecter;
    CardView btnChangePhoto;
    ImageView userPhotoView;
    private SignupViewModel viewModel;

    // Variables logiques
    String selectedRole = getString(R.string.voyageur);
    String imageUriString = "";

    // --- FIREBASE ---
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_signup);

        try {
            Map config = new HashMap();
            config.put("cloud_name", "dyum7o6ry"); // Votre Cloud Name
            config.put("secure", true);
            MediaManager.init(this, config);
        } catch (Exception e) {
            // C'est normal si c'est déjà initialisé, on ne fait rien
        }

        // Initialisation de Firebase

        // Gestion des marges
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        viewModel = new ViewModelProvider(this).get(SignupViewModel.class);
        initViews();
        viewModel.isSuccess.observe(this, success -> {
            if (success){
                Toast.makeText(this, getString(R.string.cmpt_success), Toast.LENGTH_SHORT).show();
                startActivity((new Intent(this, MainActivity.class)));
                finish();
            }
        });
        viewModel.errorMessage.observe(this, message -> {
            if (message != null && !message.isEmpty()) {
                Toast.makeText(this, getString(R.string.erreur)+ ": " + message, Toast.LENGTH_LONG).show();
            }
        });

        // Gestion Photo
        btnChangePhoto.setOnClickListener(v -> pickImage());

        // Gestion Rôles
        btnRoleTraveler.setOnClickListener(v -> {
            selectedRole = getString(R.string.voyageur);
            updateRoleUI();
        });
        btnRoleLocal.setOnClickListener(v -> {
            selectedRole = getString(R.string.local);
            updateRoleUI();
        });

        // Gestion Inscription
        btnSinscrire.setOnClickListener(v -> {
            viewModel.signup(
                    editTextEmail.getText().toString().trim(),        // 1. Email
                    editTextPasswordHash.getText().toString().trim(), // 2. Password (Mot de passe)
                    editTextFirstName.getText().toString().trim(),    // 3. FirstName (Prénom)
                    editTextLastName.getText().toString().trim(),     // 4. LastName (Nom)
                    editTextLocation.getText().toString().trim(),     // 5. Location
                    selectedRole,                                     // 6. Role
                    imageUriString
            );
        });

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
        if (selectedRole.equals(getString(R.string.voyageur))) {
            btnRoleTraveler.setBackgroundResource(R.drawable.bg_role_active);
            btnRoleTraveler.setTextColor(Color.WHITE);
            btnRoleLocal.setBackgroundResource(0);
            btnRoleLocal.setTextColor(ContextCompat.getColor(this, R.color.pink_300));

        } else {
            btnRoleTraveler.setBackgroundResource(0);
            btnRoleTraveler.setTextColor(ContextCompat.getColor(this, R.color.pink_300));
            btnRoleLocal.setBackgroundResource(R.drawable.bg_role_active);
            btnRoleLocal.setTextColor(ContextCompat.getColor(this, R.color.white_pure));
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
            userPhotoView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            userPhotoView.setPadding(0, 0, 0, 0);
            userPhotoView.setColorFilter(null);

        }
    }

}