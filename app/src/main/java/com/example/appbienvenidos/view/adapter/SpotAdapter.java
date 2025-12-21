package com.example.appbienvenidos.view.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;
import com.bumptech.glide.Glide;
import com.example.appbienvenidos.R;
import com.example.appbienvenidos.model.Spot;
import com.example.appbienvenidos.view.activities.SpotDetailActivity;
import com.example.appbienvenidos.viewmodel.SpotViewModel;

import java.util.ArrayList;
import java.util.List;
public class SpotAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    public static final int TYPE_PROFILE_GUIDE=0;
    public static final int TYPE_HOME_CARD=1;//bestrated
    public static final int TYPE_HOME_MINI=2;//newSpot
    private List<Spot> spots = new ArrayList<>() ;
    private Context context ;
    private int displayMode =TYPE_PROFILE_GUIDE;

    public SpotAdapter(){
        this.displayMode = TYPE_PROFILE_GUIDE;
    }
    public SpotAdapter(int mode){
        this.displayMode = mode;
    }

    //Méthode appelé par l'activité pour donner la liste reçu par le VM

    public void setSpot(List<Spot> spot) {
        this.spots = spot ;
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position){
        return displayMode;
    }
    // Création de la vue
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
        this.context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        if(viewType == TYPE_HOME_CARD) {
            View view =  inflater.inflate(R.layout.item_spot_home1,parent,false);
            return new HomeViewHolder(view);
        }else if(viewType == TYPE_HOME_MINI){
            View view = inflater.inflate(R.layout.item_spot_home2, parent,false);
            return new MiniViewHolder(view);
        }else {
            View view = inflater.inflate(R.layout.item_spot_profile_guide, parent, false);
            return new SpotViewHolder(view);
        }
    }

    // Nombre d'éléments
    @Override
    public int getItemCount() {
        return spots.size();
    }

    // Remplissage des données (Binding)
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Spot currentSpot = spots.get(position);

        if(holder instanceof HomeViewHolder){
            ((HomeViewHolder) holder).bind(currentSpot, context);
        }else if(holder instanceof SpotViewHolder){
            ((SpotViewHolder) holder).bind(currentSpot, context);
        }else if(holder instanceof MiniViewHolder){
            ((MiniViewHolder) holder).bind(currentSpot, context);
        }
    }

    static class SpotViewHolder extends RecyclerView.ViewHolder {
        TextView SpotName , PublicationDate,SpotRating,SpotDescription;
        ImageView SpotImage;

        public SpotViewHolder(@NonNull View itemView) {
            super(itemView);

            SpotName = itemView.findViewById(R.id.SpotName);
            PublicationDate = itemView.findViewById(R.id.PublicationDate);
            SpotRating = itemView.findViewById(R.id.SpotRating);
            SpotDescription = itemView.findViewById(R.id.SpotDescription);
            SpotImage = itemView.findViewById(R.id.SpotImage);
        }
        public void bind(Spot currentSpot, Context context){
            SpotName.setText(currentSpot.getTitle());
            PublicationDate.setText(currentSpot.getPublication_Date());
            SpotDescription.setText(currentSpot.getDescription());
            SpotRating.setText(String.format("%.1f", currentSpot.getAverage_Rating()));

            if (currentSpot.getImage_URL()!= null && !currentSpot.getImage_URL().isEmpty()){
                Glide.with(context)
                        .load(currentSpot.getImage_URL().get(0))
                        .centerCrop()
                        .placeholder(R.mipmap.ic_launcher)
                        .into(SpotImage);
            }
            // Quand on clique sur la carte, on ouvre le détail

            itemView.setOnClickListener(v -> {
                Intent intent = new Intent(context, SpotDetailActivity.class);

                // On envoie l'objet Spot entier
                intent.putExtra("SPOT_KEY", currentSpot);

                context.startActivity(intent);
            });
        }


        }
    static class HomeViewHolder extends RecyclerView.ViewHolder {
        TextView SpotName, SpotCity, SpotRating, SpotCategory;
        ImageView SpotImage;
        ViewPager2 viewPagerCard;
        private Handler sliderHandler = new Handler(Looper.getMainLooper());
        private Runnable sliderRunnable;

        View textContainer; // Ajoute cette variable

        public HomeViewHolder(@Nullable View itemView) {
            super(itemView);
            textContainer = itemView.findViewById(R.id.cardTextContainer); // Récupère le conteneur de texte

            SpotName = itemView.findViewById(R.id.cardSpotTitle);
            SpotCity = itemView.findViewById(R.id.cardSpotCity);
            SpotRating = itemView.findViewById(R.id.cardSpotRating);
            SpotImage = itemView.findViewById(R.id.SpotImage);
            //SpotCategory = itemView.findViewById(R.id.cardSpotCategory);
            viewPagerCard = itemView.findViewById(R.id.cardViewPager);
        }

        public void bind(Spot currentSpot, Context context) {
            SpotName.setText(currentSpot.getTitle());
            SpotCity.setText(currentSpot.getAdress());
            SpotRating.setText(String.format("%.1f", currentSpot.getAverage_Rating()));
            if (SpotName != null){
                SpotName.setText(currentSpot.getTitle());
            }
            if (SpotRating != null){
                SpotRating.setText(String.format("%.1f", currentSpot.getAverage_Rating()));            }

            if (SpotCategory != null) {
                String cat =  currentSpot.getCategoryNameDisplay();

                    SpotCategory.setText(cat);
            }
            if (currentSpot.getImage_URL() != null && !currentSpot.getImage_URL().isEmpty()) {
                for (String url : currentSpot.getImage_URL()) {
                    Log.d("SpotAdapter", "Image URL: " + url);
                }
                CardImageAdapter imgAdapter = new CardImageAdapter(context, currentSpot.getImage_URL(), currentSpot);
                viewPagerCard.setAdapter(imgAdapter);

                startAutoScroll(currentSpot.getImage_URL().size());

            } else {
                stopAutoScroll();
            }
            itemView.setOnClickListener(v -> openDetail(context, currentSpot));

        }

        // Petite méthode pour éviter de copier-coller le code de l'Intent
        private void openDetail(Context context, Spot spot) {
            Intent intent = new Intent(context, SpotDetailActivity.class);
            intent.putExtra("SPOT_KEY", spot);
            context.startActivity(intent);
        }
        public void startAutoScroll(int totalImages) {
            stopAutoScroll();
            if (totalImages <= 1) return;
            sliderRunnable = new Runnable() {
                @Override
                public void run() {
                    int current = viewPagerCard.getCurrentItem();
                    int next = (current == totalImages - 1) ? 0 : current + 1;
                    viewPagerCard.setCurrentItem(next, true);
                    sliderHandler.postDelayed(this, 4000);
                }
            };
            sliderHandler.postDelayed(sliderRunnable, 4000);
        }

        public void stopAutoScroll() {
            if (sliderRunnable != null)
                sliderHandler.removeCallbacks(sliderRunnable);
        }


    }
    static class MiniViewHolder extends RecyclerView.ViewHolder{
        TextView SpotName,SpotCity;
        ImageView SpotImage;
        public MiniViewHolder(@Nullable View itemView){
            super(itemView);
            SpotName = itemView.findViewById(R.id.cardSpotTitle);
            SpotCity = itemView.findViewById(R.id.cardSpotCity);
            SpotImage = itemView.findViewById(R.id.SpotImage);
            //SpotCategory = itemView.findViewById(R.id.cardSpotCategory);
        }
        public void bind(Spot currentSpot, Context context){
            if(SpotName != null) SpotName.setText(currentSpot.getTitle());
            if(SpotCity != null) SpotCity.setText(currentSpot.getAdress());

            if(currentSpot.getImage_URL() != null && !currentSpot.getImage_URL().isEmpty()){
                Glide.with(context)
                        .load(currentSpot.getImage_URL().get(0))
                        .centerCrop()
                        .placeholder(R.color.white_pure)
                        .into(SpotImage);
            }
            itemView.setOnClickListener(v->{
                Intent intent = new Intent(context, SpotDetailActivity.class);
                intent.putExtra("SPOT_KEY", currentSpot);
                context.startActivity(intent);
            });
        }
    }

    }




