package com.example.appbienvenidos.view.fragments;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.GridLayoutManager;

import com.example.appbienvenidos.R;
import com.example.appbienvenidos.view.activities.NotificationActivity;
import com.example.appbienvenidos.view.activities.SpotDetailActivity;
import com.example.appbienvenidos.view.adapter.SpotAdapter;
import com.example.appbienvenidos.viewmodel.HomeViewModel;

import androidx.cardview.widget.CardView;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.widget.EditText;
import android.widget.ImageButton;


public class HomeFragment extends Fragment {
    @Nullable
    private HomeViewModel homeViewModel;
    private SpotAdapter BestRatedAdapter;
    private SpotAdapter newSpotsAdapter;
    private ImageButton notif;

    private EditText searchEditText;
    private View chipTout, chipCafe, chipCulture, chipRestaurant,
            chipShopping, chipPayasage, chipHotel;
    private View layoutNoResults,layoutResults, loadingProgressBar;
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);

    }
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);

        //bestRted

        notif = view.findViewById(R.id.notif);

        com.google.firebase.auth.FirebaseUser user = com.google.firebase.auth.FirebaseAuth.getInstance().getCurrentUser();

        if(user != null){
            notif.setOnClickListener(v -> {
                Intent intent = new Intent(requireContext(), NotificationActivity.class);
                startActivity(intent);
            });
        } else {
            notif.setVisibility(View.GONE);
        }


        homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);
        //Itineraire
        RecyclerView recyclerBest= view.findViewById(R.id.recyclerBestRated);
        recyclerBest.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        BestRatedAdapter = new SpotAdapter(SpotAdapter.TYPE_HOME_CARD);
        recyclerBest.setAdapter(BestRatedAdapter);
        //Newspots
        RecyclerView recyclerNew = view.findViewById(R.id.recyclerNewSpots);
        GridLayoutManager gridLayoutManager= new GridLayoutManager(getContext(), 2);
        recyclerNew.setLayoutManager(gridLayoutManager);
        recyclerNew.setNestedScrollingEnabled(false);
        newSpotsAdapter = new SpotAdapter(SpotAdapter.TYPE_HOME_MINI);
        recyclerNew.setAdapter(newSpotsAdapter);

        homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);
        //Gestion de recherche
        searchEditText = view.findViewById(R.id.searchEditText);
        searchEditText.addTextChangedListener((new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (homeViewModel != null) {
                    homeViewModel.setSearchQuery(s.toString());
                }
            }
        }));
        searchEditText.setOnEditorActionListener((v, actionId, event)->{
            if (actionId == EditorInfo.IME_ACTION_SEARCH){
                homeViewModel.setSearchQuery(searchEditText.getText().toString());

                InputMethodManager imm = (InputMethodManager)requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                if(imm != null){
                    imm.hideSoftInputFromWindow(searchEditText.getWindowToken(),0);
                }
                return true;

            }
            return false;
        });
        chipTout = view.findViewById(R.id.chipTout);
        chipCulture = view.findViewById(R.id.chipCulture);
        chipPayasage = view.findViewById(R.id.chipPayasage);
        chipCafe = view.findViewById(R.id.chipCafe);
        chipRestaurant = view.findViewById(R.id.chipRestaurant);
        chipShopping = view.findViewById(R.id.chipShopping);
        chipHotel = view.findViewById(R.id.chipHotel);

        chipTout.setOnClickListener(v->
                selectCategory(chipTout,"Tout"));
        chipCulture.setOnClickListener(v->
                selectCategory(chipCulture,"NTI14ykiVgCF6a9FdRAA"));
        chipPayasage.setOnClickListener(v->
                        selectCategory(chipPayasage,"HBgPMXEAwwRo0jxkOFBl"));
        chipCafe.setOnClickListener(v->
                        selectCategory(chipCafe,"5FQ5xnOM63VR0jCEaUgx"));
        chipRestaurant.setOnClickListener(v->
                selectCategory(chipRestaurant,"kzQGD3FoEiqb0w4ojEoR"));
        chipShopping.setOnClickListener(v->
                selectCategory(chipShopping,"XBYKfhJM221pCHY9a1oM"));
        chipHotel.setOnClickListener(v->
                selectCategory(chipHotel,"Z3276a3S3WXTurMqmqPO"));
        selectCategory(chipTout, "Tout");

        layoutNoResults = view.findViewById(R.id.layoutNoResults);
        layoutResults = view.findViewById(R.id.layoutResults);
        loadingProgressBar = view.findViewById(R.id.loadingProgressBar);
        homeViewModel.getBestRatedSpots().observe(getViewLifecycleOwner(),spots ->
        {
            if (loadingProgressBar != null) {
                loadingProgressBar.setVisibility(View.GONE);
            }
            if (spots == null || spots.isEmpty()) {
                layoutNoResults.setVisibility(View.VISIBLE);
                layoutResults.setVisibility(View.GONE);

            } else {
                BestRatedAdapter.setSpot(spots);

                layoutNoResults.setVisibility(View.GONE);
                layoutResults.setVisibility(View.VISIBLE);
            }

        });
        homeViewModel.getNewSpots().observe(getViewLifecycleOwner(),spots ->
        {
            if (spots != null) {
                newSpotsAdapter.setSpot(spots);
            }
        });

    }
    private void resetChips() {
        chipTout.setSelected(false);
        chipCafe.setSelected(false);
        chipCulture.setSelected(false);
        chipRestaurant.setSelected(false);
        chipShopping.setSelected(false);
        chipPayasage.setSelected(false);
        chipHotel.setSelected(false);
    }
    private void selectCategory(View chip, String categoryId) {
        resetChips();
        chip.setSelected(true);
        homeViewModel.setCategory(categoryId);
    }

    }

