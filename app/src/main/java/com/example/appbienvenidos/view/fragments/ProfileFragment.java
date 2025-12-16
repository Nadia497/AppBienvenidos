package com.example.appbienvenidos.view.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import android.net.Uri;
import android.content.Intent;
import android.provider.MediaStore;
import android.widget.Toast;
import androidx.lifecycle.ViewModelProvider;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.appbienvenidos.R;
import com.example.appbienvenidos.model.User;
import com.example.appbienvenidos.view.activities.AddSpot;
import com.example.appbienvenidos.view.activities.ParametreActivity;
import com.example.appbienvenidos.view.adapter.SpotAdapter;
import com.example.appbienvenidos.viewmodel.ProfileViewModel;
import com.example.appbienvenidos.viewmodel.SpotViewModel;
import com.google.firebase.auth.FirebaseAuth;

import static android.app.Activity.RESULT_OK;

public class ProfileFragment extends Fragment {
    private static final int PICK_IMAGE = 100;
    //C'est un code secret (un numéro) que vous choisissez. Il sert à reconnaître cette demande précise quand la galerie se refermera
    private ImageView profileImage;
    private TextView profileName, role, location, spotNbr, total_likes, starsValue;
    private Button modifierSpot;

    private RecyclerView recyclerSpotsLocal;
    private LinearLayout nodata;
    private SpotAdapter spotAdapter;

    private ImageButton parametre;
    private  com.google.android.material.button.MaterialButton btnCreatSpot;
    private User user;
    private ProfileViewModel viewModel;
    private SpotViewModel spotViewModel;

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
        total_likes = view.findViewById(R.id.total_rate);
        starsValue = view.findViewById(R.id.nbr_stars);
        modifierSpot = view.findViewById(R.id.modifier_spot);
        btnCreatSpot = view.findViewById(R.id.btnCreateSpot);
        parametre = view.findViewById(R.id.parametre);
        recyclerSpotsLocal = view.findViewById(R.id.recyclerSpotsLocal);
        nodata = view.findViewById(R.id.layoutNoData);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerSpotsLocal.setLayoutManager(layoutManager);

        spotAdapter = new SpotAdapter();
        recyclerSpotsLocal.setAdapter(spotAdapter);


        //initialisation du viewmodel
        viewModel = new ViewModelProvider(this).get(ProfileViewModel.class);

        spotViewModel = new ViewModelProvider(this).get(SpotViewModel.class);
        spotViewModel.getSpots().observe(getViewLifecycleOwner(), spots -> {
            if (spots != null && !spots.isEmpty()) {
                recyclerSpotsLocal.setVisibility(View.VISIBLE);
                nodata.setVisibility(View.GONE);
                spotAdapter.setSpot(spots);
                int nbrSpot = spots.size();
                spotNbr.setText(String.valueOf(nbrSpot));

                double sumAvrgRate = 0;
                int totalRate = 0;

                for(com.example.appbienvenidos.model.Spot spot : spots){
                    sumAvrgRate += spot.getAverage_Rating();
                    totalRate += spot.getTotal_Rating();
                }

                double moyRate = sumAvrgRate / nbrSpot;
                String c = String.valueOf(moyRate);

                total_likes.setText(String.valueOf(totalRate));
                starsValue.setText(c + " ⭐");
            } else {
                recyclerSpotsLocal.setVisibility(View.GONE);
                nodata.setVisibility(View.VISIBLE);
                spotNbr.setText("0");
                total_likes.setText("0");
                starsValue.setText("0.0  ⭐");
            }
        });
        spotViewModel.loadSpotByPublisher(currentUserId);

        //observer les données
        viewModel.getUser().observe(getViewLifecycleOwner(), user -> {
            if (user != null) {
                profileName.setText(user.getFullName());
                location.setText(user.getLocation());
                role.setText(user.getRole());

                String urlPhoto = user.getPhotoUrl();
                //Affichage de l'url de l'image dans le logcat
                Log.d("DEBUG_IMAGE", "URL reçue : "+urlPhoto);

                if (urlPhoto != null && !urlPhoto.isEmpty()) {
                    Glide.with(this)
                            .load(urlPhoto)
                            .placeholder(R.drawable.ic_launcher_background)
                            .error(R.drawable.no_profile)
                            .circleCrop()
                            .into(profileImage);
                }
                else {
                    profileImage.setImageResource(R.drawable.no_profile);
                }
            }
        });

        //observer les messages (Toast)
        viewModel.getToastMessage().observe(getViewLifecycleOwner(), message -> {
            Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show();
        });

        //charger les données
        viewModel.loadUserProfile(currentUserId);

        // les paramètres
        parametre.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), ParametreActivity.class);
            startActivity(intent);
        });
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
