package com.example.appbienvenidos.view.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;

import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appbienvenidos.R;
import com.example.appbienvenidos.view.adapter.GuideAdapter;
import com.example.appbienvenidos.viewmodel.GuideViewModel;
import com.google.android.material.button.MaterialButton;


public class GuideFragment extends Fragment {
    private RecyclerView recyclerViewGuides;

    private MaterialButton ShowGuideProfile ;
    private EditText SearchField;
    private ProgressBar progressBar;
    private GuideAdapter GuideAdapter;
    private GuideViewModel GuideViewModel;

    //Constructeur vide
    public GuideFragment() {
    }

    //Crétaion de la vue :
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.liste_guides, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initView(view);
        setupRecyclerView();
        setupViewModel();
        setupSearch();

        GuideViewModel.loadGuides(""); // Charge tout au début
    }


    private void initView(View view) {
        recyclerViewGuides = view.findViewById(R.id.recyclerViewGuides);
        progressBar = view.findViewById(R.id.progressBar);
        SearchField = view.findViewById(R.id.searchField);

    }

    private void setupRecyclerView() {
        recyclerViewGuides.setLayoutManager(new LinearLayoutManager(requireContext()));
        GuideAdapter = new GuideAdapter();
        recyclerViewGuides.setAdapter(GuideAdapter);
    }

    private void setupSearch() {
        SearchField.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                String city = SearchField.getText().toString().trim();
                GuideViewModel.loadGuides(city);
                return true;
            }
            return false;
        });
    }

    private void setupViewModel() {
        GuideViewModel = new ViewModelProvider(this).get(GuideViewModel.class);

        GuideViewModel.getGuides().observe(getViewLifecycleOwner(), guides -> {
            if (guides != null) {
                GuideAdapter.setGuides(guides);
            } else {
                Toast.makeText(requireContext(), getString(R.string.loading_error), Toast.LENGTH_SHORT).show();
            }
            progressBar.setVisibility(View.GONE);
        });

        GuideViewModel.getIsLoading().observe(getViewLifecycleOwner(), isLoading -> {
            progressBar.setVisibility(isLoading ? View.VISIBLE : View.GONE);
        });
    }
}
