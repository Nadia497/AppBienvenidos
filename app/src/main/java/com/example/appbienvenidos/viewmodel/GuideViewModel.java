package com.example.appbienvenidos.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.appbienvenidos.model.Guide;
import com.example.appbienvenidos.repository.GuideRepository;

import java.util.List;

public class GuideViewModel extends ViewModel {

    // Lien vers repo
    private final GuideRepository repository;

    // Le conteneur des guides
    private final MutableLiveData<List<Guide>> guides = new MutableLiveData<>();

    // Le conteneur de l'état de chargement (loading)
    private final MutableLiveData<Boolean> isLoading = new MutableLiveData<>();

    public GuideViewModel() {
        repository = new GuideRepository();
    }

    // Getters
    public LiveData<List<Guide>> getGuides() {
        return guides;
    }

    public LiveData<Boolean> getIsLoading() {
        return isLoading;
    }

    public void loadGuides(String city) {
        // 1. On signale que le chargement commence
        isLoading.setValue(true);

        // 2. On choisit la bonne méthode du repository
        if (city == null || city.trim().isEmpty()) {
            // Si pas de ville précisée -> On charge tout
            repository.getAllGuides(guides);
        } else {
            // Si une ville est précisée -> On filtre
            repository.getGuidesByCity(city, guides);
        }
    }
}