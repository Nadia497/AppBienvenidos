package com.example.appbienvenidos.repository;

import android.net.Uri;

import com.cloudinary.android.callback.ErrorInfo;
import com.cloudinary.android.callback.UploadCallback;
import com.example.appbienvenidos.model.Spot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.cloudinary.android.MediaManager;
import java.util.ArrayList;
import java.util.Map;
import java.util.UUID;
import java.util.List;
public class AddSpotRepository {
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    public interface SpotCallback{
        void onSuccess(String result);
        void onError(Exception e);
    }
    public interface ImagesCallback{
        void onSuccess(List<String> urls);
        void onError(String error);
    }

    public void UploadImages(List<Uri> imageUris, ImagesCallback callback){

        List<String> uploadedUrls = new ArrayList<>();
        uploadRecursive(0, imageUris, uploadedUrls, callback);
    }

    private void uploadRecursive(int i, List<Uri> uris, List<String> resultUrls, ImagesCallback callback){
        //condition d'arrêt si on a tout traité
        if(i >= uris.size()){
            callback.onSuccess(resultUrls);
            return;
        }

        Uri currentImage = uris.get(i);
        MediaManager.get().upload(currentImage)
                .unsigned("AppBienvenidos")
                .callback(new UploadCallback() {
                    @Override
                    public void onStart(String requestId) {

                    }

                    @Override
                    public void onProgress(String requestId, long bytes, long totalBytes) {

                    }

                    @Override
                    public void onSuccess(String requestId, Map resultData) {

                        // Cloudinary renvoie l'URL sécurisée (https)
                        String secureUrl = (String) resultData.get("secure_url");
                        resultUrls.add(secureUrl);

                        // On passe à l'image suivante
                        uploadRecursive(i + 1, uris, resultUrls, callback);
                    }

                    @Override
                    public void onError(String requestId, ErrorInfo error) {

                        // Erreur
                        callback.onError(error.getDescription());
                    }

                    @Override
                    public void onReschedule(String requestId, ErrorInfo error) {

                    }
                }).dispatch();
    }
    public void addSpot(Spot spot, SpotCallback callback){
        db.collection("Spot").add(spot)
                .addOnSuccessListener(documentReference -> {
                    callback.onSuccess(documentReference.getId());
                })
                .addOnFailureListener(callback::onError);
    }

    public void updateSpot(String spotId, String title, String address, String description, double lat, double lng, String categoryId, List<String> newImageUrls, SpotCallback callback) {

        // 1. On prépare les données à changer
        java.util.Map<String, Object> updates = new java.util.HashMap<>();
        updates.put("title", title);
        updates.put("adress", address);
        updates.put("description", description);
        updates.put("category_id", categoryId);

        // On ne met à jour la position que si elle est valide (non nulle)
        if (lat != 0.0 && lng != 0.0) {
            updates.put("latitude", lat);
            updates.put("longitude", lng);
        }

        // On ne met à jour les images que si on en a reçu des nouvelles
        if (newImageUrls != null && !newImageUrls.isEmpty()) {
            updates.put("image_URL", newImageUrls);
        }

        // 2. Appel Firebase (UPDATE)
        com.google.firebase.firestore.FirebaseFirestore.getInstance()
                .collection("Spot")
                .document(spotId)
                .update(updates)
                .addOnSuccessListener(aVoid -> callback.onSuccess("Success"))
                .addOnFailureListener(callback::onError);
    }

}
