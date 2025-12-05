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
import com.example.appbienvenidos.view.activities.GuideDetailsActivity;
import com.bumptech.glide.Glide;
import com.example.appbienvenidos.R;
import com.example.appbienvenidos.model.Guide;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;
import java.util.List;

public class GuideAdapter extends RecyclerView.Adapter<GuideAdapter.GuideViewHolder> {

    // Liste des données
    private List<Guide> guides = new ArrayList<>();
    private Context context;

    // Méthode appelée par l'activité pour donner la liste
    public void setGuides(List<Guide> guides) {
        this.guides = guides;
        notifyDataSetChanged();
    }

    // Création de la vue
    @NonNull
    @Override
    public GuideViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        this.context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(R.layout.item_guide, parent, false);

        return new GuideViewHolder(view);
    }

    // Remplissage des données (Binding)
    @Override

    //holder est l objet créer et renvoyé avec onCreateViewHolder
    public void onBindViewHolder(@NonNull GuideViewHolder holder, int position) {
        Guide currentGuide = guides.get(position);

        // On remplit les textes avec les données du Guide
        holder.txtName.setText(currentGuide.getFullName());
        holder.txtCity.setText(currentGuide.getCityServed());
        holder.ShowGuideProfile.setOnClickListener(v ->{
            Intent intent = new Intent(context, GuideDetailsActivity.class);
            intent.putExtra("GUIDE_KEY", currentGuide);
            context.startActivity(intent);


        });

        // Chargement de l'image avec Glide
        if (currentGuide.getProfileImageUrl() != null && !currentGuide.getProfileImageUrl().isEmpty()) {
            Glide.with(context)
                    .load(currentGuide.getProfileImageUrl())
                    .centerCrop()
                    .into(holder.imgPhoto);
        }
    }

    // Nombre d'éléments
    @Override
    public int getItemCount() {
        return guides.size();
    }

    // Classe interne pour récupérer les IDs du XML
    static class GuideViewHolder extends RecyclerView.ViewHolder {

        TextView txtName, txtCity;
        ImageView imgPhoto;
        MaterialButton ShowGuideProfile ;

        public GuideViewHolder(@NonNull View itemView) {
            super(itemView);

            txtName = itemView.findViewById(R.id.GuideName);
            txtCity = itemView.findViewById(R.id.GuideCity);
            imgPhoto = itemView.findViewById(R.id.GuidePhoto);
            ShowGuideProfile = itemView.findViewById(R.id.ShowGuideProfile);
        }
    }
}