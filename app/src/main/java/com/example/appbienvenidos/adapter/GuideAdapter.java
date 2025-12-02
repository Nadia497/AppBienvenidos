package com.example.appbienvenidos.adapter;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.appbienvenidos.R;
import com.example.appbienvenidos.model.Guide;

import java.util.ArrayList;
import java.util.List;
public class GuideAdapter extends RecyclerView.Adapter<GuideAdapter.GuideViewHolder>{

    //Liste des guides provenant du VM :
    private List<Guide> guides = new ArrayList<>() ;
    private Context context ;


    //L'activité appelle cette méthode pour donner la nouvelle liste à l'Adapter
    public void setGuide(List<Guide> guides){
        this.guides = guides ;
        notifyDataSetChanged();
    }


}
