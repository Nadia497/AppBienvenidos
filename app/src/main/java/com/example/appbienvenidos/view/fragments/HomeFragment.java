package com.example.appbienvenidos.view.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.example.appbienvenidos.R;
import com.example.appbienvenidos.view.activities.SpotDetailActivity;

import androidx.cardview.widget.CardView;
import android.content.Intent;


public class HomeFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        // 2. On récupère la CardView grâce à son ID
        CardView cardMarrakech = view.findViewById(R.id.cardItineraryMarrakech);

        // 3. On définit l'action au clic
        cardMarrakech.setOnClickListener(v -> {
            // Création de l'Intent : "Je suis dans ce Fragment (getActivity), je veux aller vers SpotDetailActivity"
            Intent intent = new Intent(getActivity(), SpotDetailActivity.class);

            // Démarrage de l'activité
            startActivity(intent);
        });

        // 4. On retourne la vue préparée
        return view;
    }

}