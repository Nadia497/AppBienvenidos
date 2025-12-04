package com.example.appbienvenidos.repository;

import android.util.Log;
import androidx.lifecycle.MutableLiveData;

import com.example.appbienvenidos.model.Guide;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class GuideRepository {

    // 1. Initialisation de la connexion
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();

    // 2. Les Méthodes Publiques (L'API)
    public void getAllGuides(MutableLiveData<List<Guide>> liveData) {

        db.collection("guides")
                .whereEqualTo("isAvailable", true)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    List<Guide> guideList = new ArrayList<>();
                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        try {
                            Guide guide = document.toObject(Guide.class);
                            guideList.add(guide);
                        } catch (Exception e) {
                            Log.e("GuideRepo", "Erreur conversion : " + e.getMessage());
                        }
                    }
                    liveData.setValue(guideList);
                })
                .addOnFailureListener(e -> {
                    Log.e("GuideRepo", "Erreur réseau : " + e.getMessage());
                    liveData.setValue(null);
                });
    }
    public void getGuidesByCity(String city, MutableLiveData<List<Guide>> liveData) {

        // 3. Construction de la Requête
        db.collection("guides") // On cible la table "guides"
                .whereEqualTo("cityServed", city) // Filtre SQL
                .whereEqualTo("isAvailable", true) // Filtre SQL

                // 4. Exécution Asynchrone
                // .get() envoie la demande à internet
                .get()

                // 5. Gestion du Succès (Callback)
                // Ce bloc s'exécute SEULEMENT quand Firebase répond avec des données.
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    List<Guide> guideList = new ArrayList<>();

                    // On parcourt chaque "ligne" (document) trouvée
                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        try {
                            // 6. Mapping (Conversion JSON -> Java)
                            // Firebase regarde le fichier Guide.java
                            // et remplit les variables automatiquement.
                            Guide guide = document.toObject(Guide.class);


                            guideList.add(guide);
                        } catch (Exception e) {
                            Log.e("GuideRepo", "Erreur de conversion : " + e.getMessage());
                        }
                    }

                    // 7. Livraison des données
                    liveData.setValue(guideList);
                })

                // 8. Gestion de l'Erreur
                .addOnFailureListener(e -> {
                    Log.e("GuideRepo", "Erreur réseau : " + e.getMessage());
                    // On renvoie null pour signaler au ViewModel que ça a échoué
                    liveData.setValue(null);
                });
    }
}