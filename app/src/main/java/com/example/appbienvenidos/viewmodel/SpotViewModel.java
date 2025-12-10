package com.example.appbienvenidos.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.appbienvenidos.model.Spot;
import com.example.appbienvenidos.repository.SpotRepository;

import java.util.List;
public class SpotViewModel extends ViewModel {

    //Lien vers le repo
    private final SpotRepository repository;
    //Création du conteneur des spots
    private final MutableLiveData<List<Spot>> spots = new MutableLiveData<>() ;
    private final MutableLiveData<Boolean> isLoading = new MutableLiveData<>();

    public SpotViewModel () {
        repository = new SpotRepository() ;
    }

    //Getters
    public LiveData<List<Spot>> getSpots() {return spots;}
    public LiveData<Boolean> getIsLoading() {return isLoading ;}

    public void loadAllSpot(){
        isLoading.setValue(true);
        repository.getAllSpot(spots);
    }

    public void loadSpotByPublisher(String guideId){
        isLoading.setValue(true);
        repository.getSpotByPublisher(guideId ,spots);
    }


}
