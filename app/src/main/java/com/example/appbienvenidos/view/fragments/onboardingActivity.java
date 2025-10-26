package com.example.appbienvenidos.view.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import androidx.fragment.app.Fragment;
import com.example.appbienvenidos.R;
import android.content.Intent;

public class DiscoverFragment extends Fragment {

    private Button continuer, passer;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_discover, container, false);
        super.onCreate(savedInstanceState);

        continuer = view.findViewById(R.id.btn_continue);
        passer = view.findViewById(R.id.btn_passer);

        continuer.setOnClickListener( v-> {

                Intent intent = new Intent(getActivity(), DiscoverFragment2.class);
                startActivity(intent);
        });
        return view;
    }
}