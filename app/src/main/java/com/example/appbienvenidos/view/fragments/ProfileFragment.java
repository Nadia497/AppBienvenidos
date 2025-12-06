package com.example.appbienvenidos.view.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import android.net.Uri;
import android.content.Intent;
import android.provider.MediaStore;
import android.widget.Toast;
import androidx.lifecycle.ViewModelProvider;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.appbienvenidos.R;
import com.example.appbienvenidos.model.User;
import com.example.appbienvenidos.view.activities.AddSpot;
import com.example.appbienvenidos.viewmodel.ProfileViewModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import static android.app.Activity.RESULT_OK;

public class ProfileFragment extends Fragment {
    private static final int PICK_IMAGE = 100;
    //C'est un code secret (un numéro) que vous choisissez. Il sert à reconnaître cette demande précise quand la galerie se refermera
    private ImageView profileImage;
    private TextView profileName, role, location, spotNbr, itinNbr, starsValue;
    private Button modifierSpot;
    private com.google.android.material.floatingactionbutton.FloatingActionButton btnCreatSpot;
    private User user;
    private ProfileViewModel viewModel;
    private String currentUserId;
    private FirebaseAuth mAuth;

    public ProfileFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup
            container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle saveInstanceState) {

        // 1. Initialiser FirebaseAuth
        mAuth = FirebaseAuth.getInstance();

        // 2. Vérifier si l'utilisateur est connecté et récupérer son ID
        if (mAuth.getCurrentUser() != null) {
            currentUserId = mAuth.getCurrentUser().getUid();
        } else {
            // Si pas connecté, on arrête tout pour éviter le crash
            Toast.makeText(requireContext(), "Erreur : Utilisateur non connecté", Toast.LENGTH_SHORT).show();
            return;
        }

        super.onViewCreated(view, saveInstanceState);
        // Associer les vues (findViewById)
        profileImage = view.findViewById(R.id.imageprofile);
        profileName = view.findViewById(R.id.name);
        role = view.findViewById(R.id.role);
        location = view.findViewById(R.id.location);
        spotNbr = view.findViewById(R.id.spot_nbr);
        itinNbr = view.findViewById(R.id.iten_nbr);
        starsValue = view.findViewById(R.id.nbr_stars);
        modifierSpot = view.findViewById(R.id.modifier_spot);
        btnCreatSpot = view.findViewById(R.id.btnCreateSpot);

        //initialisation du viewmodel
        viewModel = new ViewModelProvider(this).get(ProfileViewModel.class);

        //observer les données
        viewModel.getUser().observe(getViewLifecycleOwner(), user -> {
            if (user != null) {
                profileName.setText(user.getFirstName() + " " + user.getLastName());
                location.setText(user.getLocation());
                role.setText(user.getRole());

                if (user.getPhotoUrl() != null && !user.getPhotoUrl().isEmpty()) {
                    Glide.with(this)
                            .load(user.getPhotoUrl())
                            .placeholder(R.drawable.ic_launcher_background)
                            .circleCrop()
                            .into(profileImage);
                }
            }
        });

        //observer les messages (Toast)
        viewModel.getToastMessage().observe(getViewLifecycleOwner(), message -> {
            Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show();
        });

        //charger les données
        viewModel.loadUserProfile(currentUserId);


        // Quand on clique sur l'image -> ouvrir la galrie
        profileImage.setOnClickListener(v -> {
            // 1. On crée une intention : "Je veux choisir (ACTION_PICK) une donnée externe"
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            // 2. On lance la galerie et on attend un résultat avec le code 100 (PICK_IMAGE)
            startActivityForResult(intent, PICK_IMAGE);
        });

        //Ajouter un spot
        btnCreatSpot.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), AddSpot.class);
            startActivity(intent);
        });
    }

    //Récupérer l'image choisie
    //appelée automatiquement quand l'utilisateur a choisi une photo (ou annulé)
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Vérification : Est-ce bien ma demande de photo (100) ? Et est-ce que l'utilisateur
        // a bien choisi quelque chose (RESULT_OK) ?
        if (requestCode == PICK_IMAGE && resultCode == RESULT_OK && data != null) {

            // 1. Récupérer l'adresse de l'image choisie dans le téléphone
            Uri selectedImage = data.getData();
            if (selectedImage != null) {
                profileImage.setImageURI(selectedImage);
                viewModel.uploadImage(currentUserId, selectedImage);
            }
        }
    }
}
