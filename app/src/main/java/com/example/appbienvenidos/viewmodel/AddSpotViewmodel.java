package com.example.appbienvenidos.viewmodel;

import android.net.Uri;
import androidx.lifecycle.LiveData;// Boîte en lecture seule (sécurité)
import androidx.lifecycle.MutableLiveData;// Boîte qu'on peut modifier (lecture/écriture)
import androidx.lifecycle.ViewModel; // Classe de base d'Android
import com.example.appbienvenidos.model.Spot;
import com.example.appbienvenidos.repository.AddSpotRepository;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.List;

public class AddSpotViewmodel extends ViewModel{

    private final AddSpotRepository repository;
    private final MutableLiveData<Boolean> isPublished = new MutableLiveData<>(false);
    private final MutableLiveData<String> toastMessage = new MutableLiveData<>();

    public AddSpotViewmodel(){
        repository = new AddSpotRepository();
    }

    private final MutableLiveData<Boolean> isLoading = new MutableLiveData<>(false);
    public LiveData<Boolean> getIsPublished() {return isPublished;}
    public LiveData<String> getToastMessage() { return toastMessage; }
    public LiveData<Boolean> getIsLoading() { return isLoading; }

    public void publishSpot(String title, String address, String description, double Lat, double Lng, List<Uri> imageUris, String categoryId, String userId){

        if(title.isEmpty() || address.isEmpty() || description.isEmpty()){
            toastMessage.setValue("Veuillez remplir tous les champs !");
            return;
        }
        if(imageUris == null || imageUris.isEmpty()){
            toastMessage.setValue("Veuillez choisir une image !");
            return;
        }

        isLoading.setValue(true);

        repository.UploadImages(imageUris, new AddSpotRepository.ImagesCallback(){
            @Override
            public void onSuccess(List<String> imageUrls) {
                creatSpotInFirestore(title, address, description, Lat, Lng, imageUrls, categoryId, userId);
            }

            @Override
            public void onError(String error) {
                isLoading.setValue(false);
                toastMessage.setValue(error);
            }
        });
    }

    public void creatSpotInFirestore(String title, String address, String description, double lat, double lng, List<String> imageUrls, String categoryId, String userId){
        String currentDate = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(new Date());

        Spot spot = new Spot(
                userId,
                title,
                description,
                address,
                lat,
                lng,
                categoryId,
                imageUrls,
                0.0,
                0,
                currentDate
        );

        repository.addSpot(spot, new AddSpotRepository.SpotCallback() {
            @Override
            public void onSuccess(String result) {
                isLoading.setValue(false);
                toastMessage.setValue("Spot publié avec succès !");
                isPublished.setValue(true);
            }

            @Override
            public void onError(Exception e) {
                isLoading.setValue(false);
                toastMessage.setValue("Erreur sauvegarde : "+ e.getMessage());

            }
        });
    }
}
