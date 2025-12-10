package com.example.appbienvenidos.viewmodel; // Ou .viewmodel si tu as créé un package

import android.net.Uri;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.example.appbienvenidos.model.User;
import com.example.appbienvenidos.repository.AuthRepository;

public class SignupViewModel extends ViewModel {

    private final AuthRepository repository;

    // Ces variables seront observées par l'Activity
    public MutableLiveData<Boolean> isSuccess = new MutableLiveData<>();
    public MutableLiveData<String> errorMessage = new MutableLiveData<>();

    // LiveData interne pour recevoir l'URL de Cloudinary
    private MutableLiveData<String> uploadedImageUrl = new MutableLiveData<>();
    public SignupViewModel() {
        repository = new AuthRepository();

        // On observe l'upload de l'image. Dès que l'URL arrive, on lance l'inscription finale
        uploadedImageUrl.observeForever(url -> {
            // Ici 'url' est le lien HTTP de Cloudinary (ou null)
            // On continue l'inscription avec cette URL
            finishRegistration(pendingEmail, pendingPassword, pendingFirst, pendingLast, pendingLoc, pendingRole, url);
        });
    }
    // Variables temporaires pour stocker les infos pendant l'upload
    private String pendingEmail, pendingPassword, pendingFirst, pendingLast, pendingLoc, pendingRole;

    public void signup(String email, String password, String firstName, String lastName, String loc, String role, String photoUrl) {

        // 1. Validation des champs
        if (email.isEmpty() || password.isEmpty() || firstName.isEmpty()) {
            errorMessage.setValue("Veuillez remplir tous les champs !");
            return;
        }

        if (password.length() < 6) {
            errorMessage.setValue("Le mot de passe doit faire au moins 6 caractères.");
            return;
        }

        // 2. Sauvegarde temporaire des infos
        this.pendingEmail = email;
        this.pendingPassword = password;
        this.pendingFirst = firstName;
        this.pendingLast = lastName;
        this.pendingLoc = loc;
        this.pendingRole = role;

        // 3. Si on a une image, on l'envoie sur Cloudinary d'abord
        if (photoUrl != null && !photoUrl.isEmpty()) {
            Uri imageUri = Uri.parse(photoUrl);
            repository.uploadProfileImage(imageUri, uploadedImageUrl, errorMessage);
        } else {
            // Pas d'image, on inscrit directement avec une chaine vide
            finishRegistration(email, password, firstName, lastName, loc, role, "");
        }
}
    private void finishRegistration(String email, String password, String first, String last, String loc, String role, String httpPhotoUrl) {

        // Création de l'utilisateur avec l'URL CLOUDINARY (pas l'URI locale)
        // Vérifie bien l'ordre ici par rapport à ton constructeur User.java !
        User newUser = new User(
                last,       // lastName
                first,      // firstName
                email,      // email
                loc,        // location
                role,       // role
                httpPhotoUrl // photoUrl (lien internet)
        );

        repository.registerUser(email, password, newUser, isSuccess, errorMessage);
    }
}
