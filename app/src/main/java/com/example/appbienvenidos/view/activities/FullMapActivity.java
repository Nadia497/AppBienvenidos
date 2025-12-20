package com.example.appbienvenidos.view.activities;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import androidx.lifecycle.ViewModelProvider;

import org.osmdroid.config.Configuration;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;
import com.example.appbienvenidos.R;
import com.example.appbienvenidos.model.Spot;
import com.example.appbienvenidos.viewmodel.SpotViewModel;

public class FullMapActivity extends AppCompatActivity {

    private MapView map;
    private SpotViewModel spotViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_map);


        // Config OSM
        Context ctx = getApplicationContext();
        Configuration.getInstance().load(ctx, PreferenceManager.getDefaultSharedPreferences(ctx));

        map = findViewById(R.id.fullMap);
        map.setMultiTouchControls(true); // ICI on autorise le zoom et le déplacement !

        // Récupérer les coordonnées envoyées
        double lat = getIntent().getDoubleExtra("LAT", 0);
        double lon = getIntent().getDoubleExtra("LON", 0);
        String title = getIntent().getStringExtra("TITLE");
        String filterUserId = getIntent().getStringExtra("Filter_par_user_id");

        GeoPoint point;
        if (lat != 0 && lon != 0) {
            point = new GeoPoint(lat, lon);
            map.getController().setZoom(18.0);
            map.getController().setCenter(point);

            addMarker(point, title, null);
        } else {
            spotViewModel = new ViewModelProvider(this).get(SpotViewModel.class);

            map.getController().setZoom(5.0);
            map.getController().setCenter(new GeoPoint(31.7917, -7.0926));

            spotViewModel.getSpots().observe(this, spots -> {
                if (spots != null) {
                    map.getOverlays().clear();
                    boolean isCameraSet = false;

                    for (Spot spot : spots) {
                        boolean afficher = false;

                        if (filterUserId != null) {
                            if (spot.getPublisher_id() != null && spot.getPublisher_id().equals(filterUserId)) {
                                afficher = true;
                            }
                        } else {
                            afficher = true;
                        }

                        if (afficher) {
                            GeoPoint p = new GeoPoint(spot.getLatitude(), spot.getLongitude());
                            addMarker(p, spot.getTitle(), spot.getAdress());

                            // Centrer sur le premier spot trouvé
                            if (!isCameraSet) {
                                map.getController().setZoom(12.0);
                                map.getController().setCenter(p);
                                isCameraSet = true;
                            }
                        }
                    }
                    map.invalidate();
                }
            });

            spotViewModel.loadAllSpot();
        }
    }

        private void addMarker (GeoPoint point, String title, String snippet){
            Marker marker = new Marker(map);
            marker.setPosition(point);
            marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
            marker.setTitle(title);
            if (snippet != null) marker.setSnippet(snippet);
            marker.setIcon(androidx.core.content.ContextCompat.getDrawable(this, R.drawable.ic_location_n)); // Ton icône
            map.getOverlays().add(marker);
        }
}