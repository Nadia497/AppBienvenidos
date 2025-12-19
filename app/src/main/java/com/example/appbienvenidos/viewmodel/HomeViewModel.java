package com.example.appbienvenidos.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import com.example.appbienvenidos.model.Spot;
import com.example.appbienvenidos.model.Category;
import com.example.appbienvenidos.repository.SpotRepository;

public class HomeViewModel extends ViewModel{
    private final SpotRepository repository;
    private List<Spot> allSpotsCache = new ArrayList<>();

    private Map<String, String> categoryMap= new HashMap<>();
    private final MutableLiveData<List<Spot>> bestRatedSpots = new MutableLiveData<>();
    private final MutableLiveData<List<Spot>> newSpots = new MutableLiveData<>();

    private String currentSearchCity ="";
    private String currentCategory = "Tout";
    public MutableLiveData<List<Spot>> getItineraires() {
        return bestRatedSpots;
    }
    public MutableLiveData<List<Spot>> getNouveauxSpots() {
        return newSpots;
    }
    public HomeViewModel(){
        repository = new SpotRepository();
        loadData();
    }
    public void loadData(){
        MutableLiveData<List<Category>> catData =new MutableLiveData<>();
        repository.getAllCategories(catData);
        catData.observeForever(categories->{
            if(categories !=null) {
                categoryMap.clear();
                for(Category cat : categories) {
                    categoryMap.put(cat.getId(), cat.getName());
                }
                loadSpots();
            }
        });

        }
    private void loadSpots(){
        MutableLiveData<List<Spot>> repoData = new MutableLiveData<>();
        repository.getAllSpot(repoData);
        repoData.observeForever(spots-> {
            if(spots != null){
                allSpotsCache = spots;
                for(Spot spot : allSpotsCache){
                    String id = spot.getCategory_id();
                    if (categoryMap.containsKey(id)){
                        spot.setCategoryNameDisplay(categoryMap.get(id));
                    }else{
                        spot.setCategoryNameDisplay("Autre");
                    }
                }
                applyFilters();
            }
        });

    }
    private void applyFilters(){
        List<Spot> filtredList = new ArrayList<>();
        for (Spot spot : allSpotsCache) {
            String ville = spot.getAdress() != null ? spot.getAdress().toLowerCase() : "";
            String catName = spot.getCategoryNameDisplay() != null ? spot.getCategoryNameDisplay() : "";
            boolean matchesSpotName=catName.contains((currentSearchCity).toLowerCase());
            boolean matchesCity = ville.contains(currentSearchCity.toLowerCase());
            boolean matchesCategory ;
            String catId =  spot.getCategory_id();

            if(currentCategory.equals("Tout")){
                matchesCategory=true;
            }else{
                matchesCategory= (catId != null && catId.equals(currentCategory));
            }

            if (    matchesCity && matchesCategory) {
                filtredList.add(spot);
            }
        }
        List<Spot> bestRated = new ArrayList(filtredList);
        Collections.sort(bestRated, (s1,s2)->
            Double.compare(s2.getAverage_Rating(), s1.getAverage_Rating()));
        bestRatedSpots.setValue(bestRated);
        List<Spot> newest = new ArrayList(filtredList);
        Collections.reverse(newest);
        newSpots.setValue(newest);


    }
    public void setSearchQuery(String query){
        this.currentSearchCity = query;
        applyFilters();
    }
    public void setCategory(String categoryId){
        this.currentCategory = categoryId;
        applyFilters();
    }
    public LiveData<List<Spot>> getBestRatedSpots(){
        return bestRatedSpots;
    }
    public LiveData<List<Spot>> getNewSpots(){
        return newSpots;
    }





}
