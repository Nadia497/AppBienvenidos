package com.example.appbienvenidos.view.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.example.appbienvenidos.R;
import com.example.appbienvenidos.model.Spot;
import com.example.appbienvenidos.view.activities.SpotDetailActivity;

import java.util.ArrayList;
import java.util.List;
public class SpotAdapter extends RecyclerView.Adapter<SpotAdapter.SpotViewHolder>{

    private List<Spot> spots = new ArrayList<>() ;
    private Context context ;

    //Méthode appelé par l'activité pour donner la liste reçu par le VM

    public void setSpot(List<Spot> spot) {
        this.spots = spot ;
        notifyDataSetChanged();
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
        holder.PublicationDate.setText(currentSpot.getPublication_Date());
        holder.SpotDescription.setText(currentSpot.getDescription());
        holder.SpotRating.setText(String.valueOf(currentSpot.getAverage_Rating()));

        // Affichage de l'image
        if (currentSpot.getImage_URL() != null && !currentSpot.getImage_URL().isEmpty()) {
            Glide.with(context)
                    .load(currentSpot.getImage_URL().get(0))
                    .centerCrop()
                    .placeholder(R.mipmap.ic_launcher) // Image d'attente
                    .into(holder.SpotImage);
        }

        // Quand on clique sur la carte, on ouvre le détail
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, SpotDetailActivity.class);

            // On envoie l'objet Spot entier
            intent.putExtra("SPOT_KEY", currentSpot);

            context.startActivity(intent);
        });
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


    }
}
