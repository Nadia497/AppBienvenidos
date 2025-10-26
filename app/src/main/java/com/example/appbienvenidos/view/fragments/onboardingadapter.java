package com.example.appbienvenidos.view.fragments;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.view.LayoutInflater;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appbienvenidos.R;

public class onboardingadapter extends  RecyclerView.Adapter<onboardingadapter.ViewHolder>{
    private int[] layouts = {
            R.layout.fragment_discover,
            R.layout.fragment_discover2,
            R.layout.fragment_discover3
    };
    private Context context;

    public onboardingadapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(layouts[viewType], parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {}//faire la mise à jour du contenu selon la position

    @Override
    public int getItemCount() {
        return layouts.length;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
