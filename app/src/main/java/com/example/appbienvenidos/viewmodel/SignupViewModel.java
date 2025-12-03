package com.example.appbienvenidos.viewmodel; // Ou .viewmodel si tu as créé un package

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.example.appbienvenidos.model.User;
import com.example.appbienvenidos.repository.AuthRepository;

public class SignupViewModel extends ViewModel {

    private final AuthRepository repository;

    // Ces variables seront observées par l'Activity
    public MutableLiveData<Boolean> isSuccess = new MutableLiveData<>();
    public MutableLiveData<String> errorMessage = new MutableLiveData<>();

    public SignupViewModel() {
        repository = new AuthRepository();
    }

    public void signup(String email, String password, String fName, String lName, String loc, String role, String photoUri) {

        // 1. Validation des champs
        if (email.isEmpty() || password.isEmpty() || fName.isEmpty()) {
            errorMessage.setValue("Veuillez remplir tous les champs !");
            return;
        }

        if (password.length() < 6) {
            errorMessage.setValue("Le mot de passe doit faire au moins 6 caractères.");
            return;
        }

        // 2. Création de l'objet User
        User newUser = new User(lName, fName, email, loc, role, photoUri);

        // 3. Appel au Repository
        repository.registerUser(email, password, newUser, isSuccess, errorMessage);
    }
}