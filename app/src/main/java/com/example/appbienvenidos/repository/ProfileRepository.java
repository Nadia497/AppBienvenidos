package com.example.appbienvenidos.repository;

import android.net.Uri;
import android.util.Log;

import com.example.appbienvenidos.model.User;
import com.google.firebase.firestore.FirebaseFirestore;
import com.cloudinary.android.MediaManager; // Cloudinary
import com.cloudinary.android.callback.ErrorInfo;
import com.cloudinary.android.callback.UploadCallback;

import java.util.Map;
public class ProfileRepository {

    private FirebaseFirestore db = FirebaseFirestore.getInstance();


    // Comme Firebase est lent, on crée ces interfaces pour dire "Appelle-moi ici quand tu as fini"
    //une interface pour quand on demande un user
    public interface UserCallback{
        void onSuccess(User user); //c'est bon, j ai trouver l'utilisateur, le voici
        void onError(String msg); //il y a eu un problème (pas d'internet, pas trouvé...).
    }

    //interface pour quand on upload une image
    public interface UploadCallback{
        void onSuccess(String imgURL); //voilà le lien de l'image
        void onError(String msg);   //échec de l'envoi
    }

    // --- FONCTION 1 : RÉCUPÉRER L'UTILISATEUR ---
    // Elle prend l'ID de l'user et le "callback" (le téléphone pour rappeler)
    public void getUserProfile(String UserId, UserCallback callback){
        Log.d("DEBUG_PROFILE", "Recherche de l'utilisateur ID : "+ UserId);
        // 1. On va dans la collection "User", on cherche le document avec l'ID donné
        db.collection("users").document(UserId).get()
                // 2. Si Firebase réussit à contacter le serveur :
                .addOnSuccessListener(documentSnapshot -> {
                    // documentSnapshot = le dossier papier virtuel reçu de Firebase
                    if(documentSnapshot.exists()) {
                        Log.d("DEBUG_PROFILE", "Document trouvé : "+ documentSnapshot.getData());
                        // 3. Magie ! On transforme le JSON de Firebase directement en objet Java 'User'
                        try{
                            User user = documentSnapshot.toObject(User.class);
                            // 4. On appelle le ViewModel pour dire : "J'ai trouvé, tiens !"
                            // On utilise le callback pour renvoyer l'objet User au ViewModel
                            callback.onSuccess(user);
                        } catch(Exception e){
                            Log.d("DEBUG_PROFILE", "erreur de conversion (mapping) : "+ e.getMessage());
                            callback.onError("Erreur de lecture des données");
                        }
                    } else {
                        Log.d("DEBUG_PROFILE", "Document introuvable dans la collection");
                        // Le document n'existe pas (mauvais ID)
                        callback.onError("Utilisateur non trouvé");
                    }
                })
                // 5. Si Firebase n'arrive même pas à chercher (ex: pas de wifi)
                .addOnFailureListener(e -> {
                    Log.d("DEBUG_PROFILE", "Erreur cnx firebase : "+ e.getMessage());
                    callback.onError(e.getMessage());
                });
    }

    // --- FONCTION 2 : UPLOAD IMAGE ---
    // Elle prend l'ID, le fichier image (Uri), et le callback
    public void uploadProfileImage(String UserId, Uri imageUri, UploadCallback callback) {
        Log.d("DEBUG_PROFILE", "Début upload cloudinary");

        MediaManager.get().upload(imageUri)
                .unsigned("AppBienvenidos")
                .callback(new com.cloudinary.android.callback.UploadCallback() {
                    @Override
                    public void onStart(String requestId) {

                    }

                    @Override
                    public void onProgress(String requestId, long bytes, long totalBytes) {

                    }

                    @Override
                    public void onSuccess(String requestId, Map resultData) {

                        String imageUrl = (String) resultData.get("secure_url");
                        Log.d("DEBUG_PROFILE", "Upload réussi, URL : " + imageUrl);
                        updateFireStoreImage(UserId, imageUrl, callback);
                    }

                    @Override
                    public void onError(String requestId, ErrorInfo error) {

                        Log.e("DEBUG_PROFILE", "Erreur Cloudinary: " + error.getDescription());
                        callback.onError("Erreur Upload: " + error.getDescription());
                    }

                    @Override
                    public void onReschedule(String requestId, ErrorInfo error) {

                    }
                })
                .dispatch();
    }

    // --- FONCTION 3 : MISE À JOUR BASE DE DONNÉES (Privée) ---
    // Sert juste à mettre le lien http://... dans la fiche de l'utilisateur
    public void updateFireStoreImage(String UserId, String imageUrl, UploadCallback callback){
        // 1. On retourne dans la collection "users", sur le document de l'utilisateur
        db.collection("users").document(UserId)

                // 2. On change SEULEMENT le champ "PhotoUrl" avec le nouveau lien
                .update("PhotoUrl", imageUrl)

                // 3. Si ça marche, on prévient enfin le ViewModel : "Tout est fini !"
                .addOnSuccessListener(aVoid -> callback.onSuccess(imageUrl))

                // 4. Si ça rate
                .addOnFailureListener(e -> callback.onError("Erreur MAJ DB: "+ e.getMessage()));
    }
}
