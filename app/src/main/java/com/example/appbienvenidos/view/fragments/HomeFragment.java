package com.example.appbienvenidos.view.fragments;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appbienvenidos.R;
import com.example.appbienvenidos.view.activities.SpotDetailActivity;
import com.example.appbienvenidos.view.adapter.SpotAdapter;
import com.example.appbienvenidos.viewmodel.HomeViewModel;

import androidx.cardview.widget.CardView;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.widget.EditText;


public class HomeFragment extends Fragment {
    @Nullable
    private HomeViewModel homeViewModel;
    private SpotAdapter itineraryAdapter;
    private SpotAdapter newSpotsAdapter;

    private EditText searchEditText;

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);

    }
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);

        homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);
        //Itineraire
        RecyclerView recyclerItin= view.findViewById(R.id.recyclerItineraries);
        recyclerItin.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        itineraryAdapter = new SpotAdapter(SpotAdapter.TYPE_HOME_CARD);
        recyclerItin.setAdapter(itineraryAdapter);
        //Newspots
        RecyclerView recyclerNew = view.findViewById(R.id.recyclerNewSpots);
        recyclerNew.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        newSpotsAdapter = new SpotAdapter(SpotAdapter.TYPE_HOME_CARD);
        recyclerNew.setAdapter(newSpotsAdapter);
        //Gestion de recherche
        searchEditText = view.findViewById(R.id.searchEditText);
        searchEditText.addTextChangedListener((new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                homeViewModel.setSearchQuery(s.toString());

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }
        }));
        view.findViewById(R.id.chipCulture).setOnClickListener(v->
                homeViewModel.setCategory("Gastronomie"));
        view.findViewById(R.id.chipNature).setOnClickListener(v->
                        homeViewModel.setCategory("Gastronomie"));
        view.findViewById(R.id.chipTout).setOnClickListener(v->
                        homeViewModel.setCategory("Gastronomie"));
        homeViewModel.getBestRatedSpots().observe(getViewLifecycleOwner(),spots ->
        {
            if (spots != null) {
                itineraryAdapter.setSpot(spots);
            }
        });
        homeViewModel.getNewSpots().observe(getViewLifecycleOwner(),spots ->
        {
            if (spots != null) {
                newSpotsAdapter.setSpot(spots);
            }
        });

    }

    }

