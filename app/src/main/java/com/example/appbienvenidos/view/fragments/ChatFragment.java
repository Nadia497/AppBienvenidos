package com.example.appbienvenidos.view.fragments;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.example.appbienvenidos.R;
import com.google.firebase.auth.FirebaseAuth;

import com.example.appbienvenidos.model.User;
import com.google.firebase.firestore.FirebaseFirestore;

public class ChatFragment extends Fragment {
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private String currentUser;
    private TextView userName;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.chat_fragment, container, false);
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle saveInstanceState){
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        if(mAuth.getCurrentUser() != null){
            currentUser = mAuth.getCurrentUser().getUid();
        }else {
            // Si pas connecté, on arrête tout pour éviter le crash
            Toast.makeText(requireContext(), "Erreur : Utilisateur non connecté", Toast.LENGTH_SHORT).show();
            return;
        }

        userName = view.findViewById(R.id.username);
        db.collection("users").document(currentUser).get()
                        .addOnSuccessListener(documentSnapshot -> {
                            if(documentSnapshot.exists()){
                                User user = documentSnapshot.toObject(User.class);
                                if(user != null){
                                    userName.setText(user.getFullName());
                                } else {
                                    Toast.makeText(requireContext(),"Profile introuvable", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }).addOnFailureListener(e ->{
                            Toast.makeText(requireContext(), "Erreur : "+ e.getMessage(), Toast.LENGTH_SHORT).show();
                });

    }
}