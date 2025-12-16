package com.example.appbienvenidos.view.activities;

import android.content.Context;
import android.os.Bundle;
import android.preference.PreferenceManager;
import androidx.appcompat.app.AppCompatActivity;
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
        map.getOverlays().add(marker);

    }
}