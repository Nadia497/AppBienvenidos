package com.example.appbienvenidos.view.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;
import com.bumptech.glide.Glide;
import com.example.appbienvenidos.R;
import com.example.appbienvenidos.model.Spot;
import com.example.appbienvenidos.view.activities.SpotDetailActivity;

import java.util.ArrayList;
import java.util.List;
public class SpotAdapter extends RecyclerView.Adapter<SpotAdapter.SpotViewHolder>{

    public static final int TYPE_PROFILE_GUIDE=0;
    public static final int TYPE_HOME_CARD=1;
    private List<Spot> spots = new ArrayList<>() ;
    private Context context ;
    private int displayMode =TYPE_PROFILE_GUIDE;

    private Handler sliderHandler = new Handler(Looper.getMainLooper());
    private Runnable sliderRunnable;
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
    public SpotViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
        this.context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(R.layout.item_spot_profile_guide ,parent ,false);
        return new SpotViewHolder(view);
    }

    // Nombre d'éléments
    @Override
    public int getItemCount() {
        return spots.size();
    }

    // Remplissage des données (Binding)
    @Override
    public void onBindViewHolder(@NonNull SpotAdapter.SpotViewHolder holder, int position) {
        Spot currentSpot = spots.get(position);

        // Affichage des textes
        holder.SpotName.setText(currentSpot.getTitle());
        holder.SpotCity.setText(currentSpot.getAdress());
        holder.SpotDescription.setText(currentSpot.getDescription());
        holder.SpotRating.setText(String.valueOf(currentSpot.getAverage_Rating()));

        if(getItemViewType(position) == TYPE_HOME_CARD){
            if (currentSpot.getImage_URL()!= null && !currentSpot.getImage_URL().isEmpty){
                Glide.with(context)
                        .Load(currentSpot.getImage_URL().get(0))
                        .centerCrop()
                        .placeholder(R.mipmap.ic_launcher)
                        .into(holder.SpotImage);
            }
            holder.itemView.setOnClickListener(v -> {
                Intent intent = new Intent(context, SpotDetail)
            });
        }
        // Affichage de l'image
        if (currentSpot.getImage_URL() != null && !currentSpot.getImage_URL().isEmpty()) {
            CardImageAdapter imgAdapter = new CardImageAdapter(context, currentSpot.getImage_URL());
            holder.viewPagerCard.setAdapter(imgAdapter);

            holder.startAutoScroll(currentSpot.getImage_URL().size());

        }else{
            holder.stopAutoScroll();
        }
        holder.itemView.setOnClickListener(v -> {
            Intent intent =  new Intent(context, SpotDetailActivity.class);
            intent.putExtra("SPOT_KEY", currentSpot);
            context.startActivity(intent);
        });

        // Quand on clique sur la carte, on ouvre le détail
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, SpotDetailActivity.class);

            // On envoie l'objet Spot entier
            intent.putExtra("SPOT_KEY", currentSpot);

            context.startActivity(intent);
        });
    }

    static class SpotViewHolder extends RecyclerView.ViewHolder {
        TextView SpotName , SpotCity,SpotRating,SpotDescription;
        ImageView SpotImage;

        public SpotViewHolder(@NonNull View itemView) {
            super(itemView);

            SpotName = itemView.findViewById(R.id.SpotName);
            SpotCity = itemView.findViewById(R.id.SpotCity);
            SpotRating = itemView.findViewById(R.id.SpotRating);
            SpotDescription = itemView.findViewById(R.id.SpotDescription);
            SpotImage = itemView.findViewById(R.id.SpotImage);
        }


    }
    public void startAutoScroll(int totalImages){
        spotAutoScroll();
        if (totalImages <= 1) return;
        sliderRunnable = new Runnable(){
            @Override
            public void run(){
                int current = viewPagerCard.getCurrentItem();
                int next = (current == totalImages-1)? 0: current+1;
                viewPagerCard.setCurrentItem(next, true);
                sliderHandler.postDelayed(this, 3000);
            }
        };
        sliderHandler.postDelayed(sliderRunnable, 3000);
    }
    public void stopAutoScroll(){
        if (sliderRunnable !=null)
            sliderHandler.removeCallBacks(sliderRunnable);
    }
}
