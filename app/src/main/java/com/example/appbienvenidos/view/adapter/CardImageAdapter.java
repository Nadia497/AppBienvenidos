package com.example.appbienvenidos.view.adapter;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.example.appbienvenidos.R;
import com.example.appbienvenidos.model.Spot;
import com.bumptech.glide.Glide;
import com.example.appbienvenidos.view.activities.SpotDetailActivity;


import java.util.List;


public class CardImageAdapter extends RecyclerView.Adapter<CardImageAdapter.ImageViewHolder> {

    private List<String> imageUrls;
    private Context context;
    private Spot spot;

    public CardImageAdapter(Context context, List<String> imageUrls,Spot spot ){
        this.context = context;
        this.imageUrls = imageUrls;
        this.spot= spot;
    }
    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
        ImageView imageView = new ImageView(context);
        imageView.setLayoutParams((new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT)));
        imageView.setScaleType((ImageView.ScaleType.CENTER_CROP));
        return new ImageViewHolder(imageView);
    }
    @Override
    public void onBindViewHolder(@NonNull ImageViewHolder holder,int position){
        if(imageUrls != null && position < imageUrls.size()){
            Glide.with(context)
                    .load(imageUrls.get(position))
                    .placeholder(R.color.white_pure)
                    .into((ImageView) holder.itemView);
        }

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, SpotDetailActivity.class);
            intent.putExtra("SPOT_KEY", spot);
            context.startActivity(intent);
        });
    }
    @Override
    public int getItemCount(){
        return imageUrls!= null ?imageUrls.size() : 0;
    }
    public static class ImageViewHolder extends RecyclerView.ViewHolder{
        public ImageViewHolder(@NonNull android.view.View itemView){
            super(itemView);
        }
    }
}
