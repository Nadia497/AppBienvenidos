package com.example.appbienvenidos.view.activities;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import org.osmdroid.config.Configuration;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;
import com.example.appbienvenidos.R;

public class FullMapActivity extends AppCompatActivity {

    private MapView map;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Config OSM
        Context ctx = getApplicationContext();
        Configuration.getInstance().load(ctx, PreferenceManager.getDefaultSharedPreferences(ctx));

        setContentView(R.layout.activity_full_map);

        map = findViewById(R.id.fullMap);
        map.setMultiTouchControls(true); // ICI on autorise le zoom et le déplacement !

        // Récupérer les coordonnées envoyées
        double lat = getIntent().getDoubleExtra("LAT", 0);
        double lon = getIntent().getDoubleExtra("LON", 0);
        String title = getIntent().getStringExtra("TITLE");

        GeoPoint point = new GeoPoint(lat, lon);
        map.getController().setZoom(18.0);
        map.getController().setCenter(point);

        // Ajouter le marqueur
        Marker marker = new Marker(map);
        marker.setPosition(point);
        marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
        marker.setTitle(title);
        // marker.setIcon(...) // Tu peux remettre ton icône personnalisée ici aussi
        try {
            // 1. On reprend ton image
            Drawable logoOriginal = ContextCompat.getDrawable(this, R.drawable.logomap);

            // 2. On convertit en Bitmap
            Bitmap bitmapOriginal = ((BitmapDrawable) logoOriginal).getBitmap();

            // 3. On redimensionne (Même taille que l'autre : 100x100)
            Drawable petitLogo = new BitmapDrawable(getResources(),
                    Bitmap.createScaledBitmap(bitmapOriginal, 30, 30, true));

            // 4. On l'applique
            marker.setIcon(petitLogo);

        } catch (Exception e) {
            e.printStackTrace();
        }
        map.getOverlays().add(marker);

    }
}