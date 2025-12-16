package com.example.appbienvenidos.view.activities;

import android.os.Bundle;
import android.widget.ImageButton;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;
import com.example.appbienvenidos.R;
import com.example.appbienvenidos.view.adapter.ImageSliderAdapter;
import java.util.ArrayList;

public class FullScreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_screen);

        // Récupérer la liste des photos et la position cliquée
        ArrayList<String> images = getIntent().getStringArrayListExtra("IMAGES_LIST");
        int position = getIntent().getIntExtra("SELECTED_POSITION", 0);

        ViewPager2 viewPager = findViewById(R.id.viewPagerImages);
        ImageButton btnClose = findViewById(R.id.btnClose);

        // Configurer l'adapter
        if (images != null) {
            ImageSliderAdapter adapter = new ImageSliderAdapter(this, images);
            viewPager.setAdapter(adapter);
            // Aller directement à la photo sur laquelle on a cliqué
            viewPager.setCurrentItem(position, false);
        }

        // Fermer l'activité
        btnClose.setOnClickListener(v -> finish());
    }
}