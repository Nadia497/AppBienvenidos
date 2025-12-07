package com.example.appbienvenidos.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import java.util.ArrayList;
import java.util.List;

import com.example.appbienvenidos.model.Spot;

public class HomeViewModel extends ViewModel{
    private MutableLiveData<List<Spot>> itinerairesList = new MutableLiveData<>();
    private MutableLiveData<List<Spot>> nouveauxSpotsList = new MutableLiveData<>();
    private MutableLiveData<List<Spot>> itinerairesList

    public MutableLiveData<List<Spot>> getItineraires() {
        return itinerairesList;
    }
    public MutableLiveData<List<Spot>> getNouveauxSpots() {
        return itinerairesList;
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
