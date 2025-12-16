package com.example.appbienvenidos.view.adapter;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.example.appbienvenidos.R;
import com.bumptech.glide.Glide;


import java.util.List;


public class CardImageAdapter extends RecyclerView.Adapter<CardImageAdapter.ImageViewHolder> {

    private List<String> imageUrls;
    private Context context;

    public CardImageAdapter(Context context, List<String> imageUrls){
        this.context = context;
        this.imageUrls = imageUrls;
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
        Glide.with(context)
                .load(imageUrls.get(position))
                .placeholder(R.color.white_pure)
                .into((ImageView) holder.itemView);
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
