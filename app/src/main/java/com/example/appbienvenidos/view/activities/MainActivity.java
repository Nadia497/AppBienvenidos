package com.example.appbienvenidos.view.activities;

import android.annotation.SuppressLint;
import android.os.Bundle;

import com.cloudinary.android.MediaManager;
import java.util.HashMap;
import java.util.Map;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import android.content.Intent;
import androidx.appcompat.app.AlertDialog;
import com.google.firebase.auth.FirebaseAuth;
import com.example.appbienvenidos.R;
import com.example.appbienvenidos.view.fragments.ChatFragment;
import com.example.appbienvenidos.view.fragments.HomeFragment;
import com.example.appbienvenidos.view.fragments.GuideFragment;
import com.example.appbienvenidos.view.fragments.ProfileFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends BaseActivity{

    private BottomNavigationView bottomNavigationView;
    private FirebaseUser currentUser;

    @SuppressLint("CutPasteId")
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        currentUser = FirebaseAuth.getInstance().getCurrentUser();

        try {
            Map config = new HashMap();
            config.put("cloud_name", "dyum7o6ry"); // Trouve-le sur ton dashboard
            MediaManager.init(this, config);
        } catch (Exception e) {
            // Déjà initialisé, on ignore
        }

        BottomNavigationView bottomNav = findViewById(R.id.navigation_bar);

        bottomNav.setOnItemSelectedListener(item ->{
            Fragment selectedFragment=null;
            int itemId=item.getItemId();

            if (currentUser == null) {
                if (itemId == R.id.nav_chat || itemId == R.id.nav_profile || itemId == R.id.nav_guide) {
                    showGuestAlert(); // On affiche l'alerte
                    return false;     // On refuse le changement de page
                }
            }

            if (itemId == R.id.nav_home){
                selectedFragment = new HomeFragment();
            }else if (itemId == R.id.nav_chat){
                selectedFragment = new ChatFragment();
            }else if(itemId == R.id.nav_profile){
                selectedFragment = new ProfileFragment();
            }else if(itemId == R.id.nav_guide){
                selectedFragment = new GuideFragment();
            }
            if(selectedFragment !=null){
                loadFragment(selectedFragment);
                return true;
            }
            return false;
        });
        if (savedInstanceState == null){
            loadFragment(new HomeFragment());
        }
    }
    private void loadFragment(Fragment fragment){
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container,fragment)
                .commit();
    }

    private void showGuestAlert() {
        new AlertDialog.Builder(this)
                .setTitle(getString(R.string.attention)) // Assurez-vous d'avoir ce string ou mettez "Attention"
                .setMessage(getString(R.string.guest_message)) // "Veuillez vous connecter..."
                .setPositiveButton(getString(R.string.se_connecter), (dialog, which) -> {
                    // Redirection vers le Login
                    Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish(); // Ferme MainActivity pour ne pas revenir en arrière
                })
                .setNegativeButton(getString(R.string.annuler), null)
                .show();
    }
}
