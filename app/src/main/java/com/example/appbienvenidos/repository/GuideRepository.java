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

    public interface GuideCallback{
        void onSuccess();
        void onError(String msg);
    }
    public interface OneGuideCallback{
        void onSuccess(Guide guide );
        void onError(String msg);
    }

    public void addGuide(Guide guide, GuideCallback callback){
        db.collection("Guide").document(guide.getUid()).set(guide)
                .addOnSuccessListener(aVoid -> callback.onSuccess())
                .addOnFailureListener(e -> callback.onError(e.getMessage()));
    }

    public void getGuide(String userId, OneGuideCallback callback){
        Log.d("DEBUG_GUIDE", "Tentative de récupération du guide pour ID: " + userId);
        db.collection("Guide").document(userId).get()
                .addOnSuccessListener(documentSnapshot -> {
                    if(documentSnapshot.exists()){
                        Log.d("DEBUG_GUIDE", "Document trouvé : "+ documentSnapshot.getData());
                        try{
                            Guide guide = documentSnapshot.toObject(Guide.class);
                            callback.onSuccess(guide);
                        } catch (Exception e){
                            Log.d("DEBUG_GUIDE", "Erreur de conversion mapping : "+ e.getMessage());
                            callback.onError("Utilisateur non trouvé");

                        }
                    }
                    else {
                        Log.d("DEBUG_GUIDE", "guide n'est pas trouvé ");
                        callback.onError("Aucun guide trouvé");
                    }
                })
                .addOnFailureListener(e -> {
                    Log.d("DEBUG_GUIDE", "erreur connexion firebase : "+e.getMessage());
                    callback.onError(e.getMessage());
                });
    }
    // 2. Les Méthodes Publiques (L'API)
    public void getAllGuides(MutableLiveData<List<Guide>> liveData) {

        db.collection("Guide")
                .whereEqualTo("available", true)
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
        db.collection("Guide") // On cible la table "guides"
                .whereEqualTo("cityServed", city) // Filtre SQL
                .whereEqualTo("available", true) // Filtre SQL

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