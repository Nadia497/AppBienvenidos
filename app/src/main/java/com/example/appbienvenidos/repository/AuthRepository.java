package com.example.appbienvenidos.repository;

import com.google.firebase.Firebase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import androidx.lifecycle.MutableLiveData;
import com.example.appbienvenidos.model.User;
import android.net.Uri;
import com.cloudinary.android.MediaManager;
import com.cloudinary.android.callback.ErrorInfo;
import com.cloudinary.android.callback.UploadCallback;

import java.util.Map;

public class AuthRepository {
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    public AuthRepository(){
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
    }
    //fontions d'inscription

    public void uploadProfileImage(Uri imageUri, MutableLiveData<String> urlResult, MutableLiveData<String> errorLive){
        if(imageUri == null){
            urlResult.postValue(null); //pas d'image en renvoie null
            return;
        }

        MediaManager.get().upload(imageUri)
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
                        String secureUrl = (String) resultData.get("secure_url");
                        urlResult.postValue(secureUrl);
                    }

                    @Override
                    public void onError(String requestId, ErrorInfo error) {
                        errorLive.postValue("Erreur image: "+error.getDescription());
                    }

                    @Override
                    public void onReschedule(String requestId, ErrorInfo error) {

                    }
                })
                .dispatch();
    }
    public void registerUser(String email, String password, User user,
                             MutableLiveData<Boolean> successLive,
                             MutableLiveData<String> errorLive){
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnSuccessListener(authResult -> {
                    if (authResult.getUser() != null){
                            String uid = authResult.getUser().getUid();
                            db.collection("users").document(uid).set(user)
                                    .addOnSuccessListener(aVoid ->{
                                            successLive.postValue(true);
                        })
                                .addOnFailureListener(e -> {
                            errorLive.postValue("Erreur Firestore: "+ e.getMessage());
                        });
                }
                    })

                    .addOnFailureListener(e->{
                        errorLive.postValue("Erreur Aut: "+ e.getMessage());
                    });
}
}

