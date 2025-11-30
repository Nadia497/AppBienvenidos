package com.example.appbienvenidos.view.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.appbienvenidos.R;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    // Déclaration des variables
    private EditText editEmail, editPassword;
    private Button btnSeConnecter;
    private TextView textGoToRegister, forgotPassword;

    // Firebase
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);

        // Gestion des marges système
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // 1. Initialiser Firebase
        mAuth = FirebaseAuth.getInstance();

        // 2. Initialiser les vues
        initViews();

        // 3. Configurer les boutons
        setupListeners();
    }

    private void initViews() {
        editEmail = findViewById(R.id.editTextEmailLogin);
        editPassword = findViewById(R.id.editTextPasswordLogin);
        btnSeConnecter = findViewById(R.id.btnSeConnecter);
        textGoToRegister = findViewById(R.id.textGoToRegister);
        forgotPassword = findViewById(R.id.forgotPassword);
    }

    private void setupListeners() {
        // --- BOUTON CONNEXION ---
        btnSeConnecter.setOnClickListener(v -> {
            String email = editEmail.getText().toString().trim();
            String password = editPassword.getText().toString().trim();

            if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
                Toast.makeText(LoginActivity.this, "Veuillez remplir tous les champs", Toast.LENGTH_SHORT).show();
            } else {
                loginUser(email, password);
            }
        });

        // --- LIEN VERS INSCRIPTION ---
        textGoToRegister.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, SignupActivity.class);
            startActivity(intent);
            finish(); // On ferme le login pour ne pas empiler les pages
        });

        // --- MOT DE PASSE OUBLIÉ ---
        forgotPassword.setOnClickListener(v -> {
            String email = editEmail.getText().toString().trim();
            if (TextUtils.isEmpty(email)) {
                Toast.makeText(this, "Entrez votre email dans la case ci-dessus pour réinitialiser.", Toast.LENGTH_LONG).show();
            } else {
                resetPassword(email);
            }
        });
    }

    private void loginUser(String email, String password) {
        // Connexion avec Firebase
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnSuccessListener(authResult -> {
                    // Succès
                    Toast.makeText(LoginActivity.this, "Connexion réussie !", Toast.LENGTH_SHORT).show();

                    // Redirection vers l'Accueil (MainActivity)
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    // Ces flags empêchent de revenir au login en faisant "Retour"
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
                })
                .addOnFailureListener(e -> {
                    // Erreur (Mauvais mot de passe, pas de compte, etc.)
                    Toast.makeText(LoginActivity.this, "Erreur : " + e.getMessage(), Toast.LENGTH_LONG).show();
                });
    }

    private void resetPassword(String email) {
        mAuth.sendPasswordResetEmail(email)
                .addOnSuccessListener(unused -> {
                    Toast.makeText(this, "Email de réinitialisation envoyé !", Toast.LENGTH_LONG).show();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Erreur : " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }
}