package com.example.appbienvenidos.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import java.util.ArrayList;
import java.util.List;

import com.example.appbienvenidos.model.Spot;
import com.example.appbienvenidos.repository.SpotRepository;

public class HomeViewModel extends ViewModel{
    private final SpotRepository repository;
    private List<Spot> allSpotsCache = new ArrayList<>();

    private final MutableLiveData<List<Spot>> bestRtedSpots = new MutableLiveData<>();
    private final MutableLiveData<List<Spot>> newSpots = new MutableLiveData<>();

    public MutableLiveData<List<Spot>> getItineraires() {
        return itinerairesList;
    }
    public MutableLiveData<List<Spot>> getNouveauxSpots() {
        return itinerairesList;
    }
    public HomeViewModel(){
        repository = new SpotRepository();
        loadSpots();
    }
    private void LoadSpots(){
        MutableLiveData<List<Spot>> repoDta = new MutableLiveDta<>();
        repository.getAllSpot(repoData);
        repoData.observeForever(spots-> {
            if(spots != null){
                allSpotsCache = spots;
                applyFilters();
            }
        });
        private void applyFiltres(){
            List<Spot> filtredList = new ArrayList<>();
            for (Spot spot : allSpotsCache){
                String ville =  spot.getAdress() != nul ? spot.getAdress().toLowerCase(): "";
                String cat  =  spot.getCategory() != null ? getCatgeory(): "";
                boolean matchesCity = ville.contains(currentSearchCity.toLowerCase());
                boolean match;

            }
        }
    }

    public void chargerDonneed(){
        List<Spot> tousLesSpots = simulerBaseDeDonnees();

        List<Spot> filtresItiniraires = new ArrayList<>();
        List<Spot> filtresNouveaux = new ArrayList<>();

        for (Spot spot : tousLesSpots){

            if ("ITINAIRAIRE".equals(spot.getType())){
                filtresItiniraires.add(spot);
            }
            else if (spot.isNew()){
                filtresNouveaux.add(spot);
            }
        }
        private List<Spot> simulerBaseDeDonnees(){
            List<Spot> spots = new ArrayList<>();

            spots.add(new Spot(1,"1 Journnee medina", "Guide",R.drawable.koutoubia, ""))
        }
    }
}
