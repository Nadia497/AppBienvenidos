package com.example.appbienvenidos.viewmodel;

import android.net.Uri;
import android.util.Log;

import androidx.lifecycle.LiveData;// Boîte en lecture seule (sécurité)
import androidx.lifecycle.MutableLiveData;// Boîte qu'on peut modifier (lecture/écriture)
import androidx.lifecycle.ViewModel; // Classe de base d'Android
import com.example.appbienvenidos.model.User;
import com.example.appbienvenidos.repository.ProfileRepository;

// On hérite de ViewModel pour survivre aux rotations d'écran
public class ProfileViewModel extends ViewModel{

    private ProfileRepository repository;// Référence vers le Cuisinier
    private MutableLiveData<User> userLiveData;  //pour stocker les données de l'utilisateur
    private MutableLiveData<String> toastMessage; //pour afficher les messages d'erreurs et de succès

    // Le Constructeur (appelé quand le ViewModel est créé)
    public ProfileViewModel(){
        repository = new ProfileRepository();// On embauche le cuisinier
        userLiveData = new MutableLiveData<>();// On prépare la boîte vide
        toastMessage = new MutableLiveData<>();// On prépare la boîte vide
    }

    // Getters
    // Le Fragment utilise ça pour "regarder" les boîtes, mais il ne peut pas les modifier (c'est du LiveData, pas Mutable)
    public LiveData<User> getUser() {return userLiveData;}
    public LiveData<String> getToastMessage() {return toastMessage;}

    //Action: charger le profile
    public void loadUserProfile(String userId){
        repository.getUserProfile(userId, new ProfileRepository.UserCallback() {
            @Override
            public void onSuccess(User user) {
                userLiveData.setValue(user); //met à jour l'UI
            }

            @Override
            public void onError(String msg) {
                toastMessage.setValue(msg);
            }
        });
    }

    //Action: changer la photo
    public void uploadImage(String userId, Uri imageUri){
        toastMessage.setValue("Sauvegarde de la photo...");

        repository.uploadProfileImage(userId, imageUri, new ProfileRepository.UploadCallback() {
            @Override
            public void onSuccess(String imgURL) {
                toastMessage.setValue("Profile mis à jour !");
                Log.d("DEBUG_IMAGE", "URL viewModel reçue : "+imgURL);


                User currentUser = userLiveData.getValue();
                if (currentUser != null) {
                    currentUser.setPhotoUrl(imgURL);
                    userLiveData.setValue(currentUser);
                }
            }

            @Override
            public void onError(String msg) {
                toastMessage.setValue(msg);
            }
        });
    }

    public void saveChanges(String userId, String nom, String prenom, String ville, String role, Uri imageUri){
        if(imageUri != null){
            repository.uploadProfileImage(userId, imageUri, new ProfileRepository.UploadCallback() {
                @Override
                public void onSuccess(String imgURL) {
                    //updateFireStoreImage a déjà mes l'URL dans la base donc on passe maintenant au texte

                    updateTextOnly(userId, nom, prenom, ville, role);
                }

                @Override
                public void onError(String msg) {

                }
            });
        }
        else{
            updateTextOnly(userId, nom, prenom, ville, role);
        }
    }

    public void updateTextOnly(String userId, String lastName, String firstName, String location, String role){
        repository.updateUser(userId, lastName, firstName, location, role, new ProfileRepository.ActionCallBack() {
            @Override
            public void onSuccess() {

            }

            @Override
            public void onError(String msg) {

            }
        });
    }
}
