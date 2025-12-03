package com.example.appbienvenidos.repository;

import com.google.firebase.Firebase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import androidx.lifecycle.MutableLiveData;
import com.example.appbienvenidos.model.User;

public class AuthRepository {
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    public AuthRepository(){
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
    }
    //fontion d'inscription
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

