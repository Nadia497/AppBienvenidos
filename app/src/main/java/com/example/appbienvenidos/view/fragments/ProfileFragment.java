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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.appbienvenidos.R;
import com.example.appbienvenidos.model.User;
import com.example.appbienvenidos.view.activities.AddSpot;
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
    private Button modifierSpot, btnCreatSpot, btnCreatItinerary;
    private User user;
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;
    private FirebaseStorage storage;
    private String currentUserId = "ja8gSFTt7ueTjeOAw88x";

    public ProfileFragment() {}

        @Nullable
        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup
                container, @Nullable Bundle savedInstanceState){
            return inflater.inflate(R.layout.activity_profile, container, false);
        }

        @Override
        public void onViewCreated (@NonNull View view, @Nullable Bundle saveInstanceState){

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
            btnCreatItinerary = view.findViewById(R.id.btn);

            //initialisation Firebase
            db = FirebaseFirestore.getInstance();
            mAuth = FirebaseAuth.getInstance();
            storage = FirebaseStorage.getInstance();
            // Vérification connexion
            /*if (mAuth.getCurrentUser() != null) {
                currentUserId = mAuth.getCurrentUser().getUid();
            } else {
                // Pour le test, si pas d'auth, attention ça va planter.
                // Idéalement rediriger vers Login.
                Toast.makeText(requireContext(), "Mode déconnecté", Toast.LENGTH_SHORT).show();
                return;
            }*/

            if (currentUserId == null) return;

            db.collection("User").document(currentUserId).get()
                    .addOnSuccessListener(documentSnapshot -> {
                        if (documentSnapshot.exists()) {
                            user = documentSnapshot.toObject(User.class);
                            if (user != null) {
                                profileName.setText(user.getFirst_Name() + " " + user.getLastName());
                                location.setText(user.getLocation());
                                role.setText(user.getRole());

                                // Affichage de l'image avec Glide
                                if (user.getProfile_Picture_URL() != null && !user.getProfile_Picture_URL().isEmpty()) {
                                    Glide.with(this)
                                            .load(user.getProfile_Picture_URL())
                                            .placeholder(R.drawable.ic_launcher_background) // Image par défaut pendant chargement
                                            .circleCrop()
                                            .into(profileImage);
                                }
                            }
                        }
                    }).addOnFailureListener(e -> Toast.makeText(requireContext(), "Erreur: " + e.getMessage(), Toast.LENGTH_SHORT).show());


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
        public void onActivityResult ( int requestCode, int resultCode, Intent data){
            super.onActivityResult(requestCode, resultCode, data);
            // Vérification : Est-ce bien ma demande de photo (100) ? Et est-ce que l'utilisateur
            // a bien choisi quelque chose (RESULT_OK) ?
            if (requestCode == PICK_IMAGE && resultCode == RESULT_OK && data != null) {

                // 1. Récupérer l'adresse de l'image choisie dans le téléphone
                Uri selectedImage = data.getData();
                if (selectedImage != null) {
                    profileImage.setImageURI(selectedImage);
                    uploadImageToFirebase(selectedImage);
                }
            }
        }
        private void uploadImageToFirebase (Uri imageUri){
            if (currentUserId == null) return;

            Toast.makeText(requireContext(), "Sauvegarde de la photo...", Toast.LENGTH_SHORT).show();

            // On enregistre sous le nom : profile_images/ID_DU_USER.jpg
            StorageReference fileRef = storage.getReference().child("profile_images/" + currentUserId + ".jpg");

            fileRef.putFile(imageUri)
                    .addOnSuccessListener(taskSnapshot -> {
                        // Upload réussi, on demande l'URL
                        fileRef.getDownloadUrl().addOnSuccessListener(uri -> {
                            String imageUrl = uri.toString();
                            updateUserImageInFirestore(imageUrl);
                        });
                    })
                    .addOnFailureListener(e -> Toast.makeText(requireContext(), "Echec upload: " + e.getMessage(), Toast.LENGTH_SHORT).show());
        }

        private void updateUserImageInFirestore (String imageUrl){
            db.collection("User").document(currentUserId)
                    .update("Profile_Picture_URL", imageUrl)
                    .addOnSuccessListener(aVoid -> Toast.makeText(requireContext(), "Profil mis à jour !", Toast.LENGTH_SHORT).show())
                    .addOnFailureListener(e -> Toast.makeText(requireContext(), "Erreur mise à jour DB", Toast.LENGTH_SHORT).show());
        }
    }


