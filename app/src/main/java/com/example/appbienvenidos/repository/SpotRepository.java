package com.example.appbienvenidos.repository;

import android.util.Log;
import androidx.lifecycle.MutableLiveData;

import com.example.appbienvenidos.model.Guide;
import com.example.appbienvenidos.model.Spot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;
public class SpotRepository {

    // 1. Initialisation de la connexion
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();

    public void getSpotByPublisher(String guideId, MutableLiveData<List<Spot>> LiveData) {
        db.collection("Spot")
                .whereEqualTo("publisher_id", guideId)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    List<Spot> spotListe = new ArrayList<>();
                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        try {
                            Spot spot = document.toObject(Spot.class);
                            spotListe.add(spot);
                        } catch (Exception e) {
                            Log.e("SpotRepo", "Erreur conversion : " + e.getMessage());
                        }
                    }
                    LiveData.setValue(spotListe);
                })
                .addOnFailureListener(e -> {
                    Log.e("SpotRepo", "Erreur réseau : " + e.getMessage());
                    LiveData.setValue(null);
                });

    }

    public void getAllSpot(MutableLiveData<List<Spot>> LiveData) {
        db.collection("Spot")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    List<Spot> spotListe = new ArrayList<>();
                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        try {
                            Spot spot = document.toObject(Spot.class);
                            spotListe.add(spot);
                        } catch (Exception e) {
                            Log.e("SpotRepo", "Erreur conversion : " + e.getMessage());
                        }
                    }
                    LiveData.setValue(spotListe);
                })
                .addOnFailureListener(e -> {
                    Log.e("SpotRepo", "Erreur réseau : " + e.getMessage());
                    LiveData.setValue(null);
                });
    }

}
