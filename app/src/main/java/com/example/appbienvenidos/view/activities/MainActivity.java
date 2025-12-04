package com.example.appbienvenidos.view.activities;

import android.os.Bundle;

import com.cloudinary.android.MediaManager;
import java.util.HashMap;
import java.util.Map;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.appbienvenidos.R;
import com.example.appbienvenidos.view.fragments.ChatFragment;
import com.example.appbienvenidos.view.fragments.HomeFragment;
import com.example.appbienvenidos.view.fragments.GuideFragment;
import com.example.appbienvenidos.view.fragments.ProfileFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
public class MainActivity extends AppCompatActivity{
    @Override
    protected void onCreate(Bundle savedInstanceState){
       super.onCreate(savedInstanceState);
       setContentView(R.layout.activity_main);

        try {
            Map config = new HashMap();
            config.put("cloud_name", "dyum7o6ry"); // Trouve-le sur ton dashboard
            MediaManager.init(this, config);
        } catch (Exception e) {
            // Déjà initialisé, on ignore
        }

       BottomNavigationView bottomNav = findViewById(R.id.navigation_bar);

       bottomNav.setOnItemSelectedListener(item ->{
           Fragment selectedFragment=null;
           int itemId=item.getItemId();
           if (itemId == R.id.nav_home){
               selectedFragment = new HomeFragment();
           }else if (itemId == R.id.nav_chat){
               selectedFragment = new ChatFragment();
           }else if(itemId == R.id.nav_profile){
               selectedFragment = new ProfileFragment();
           }else if(itemId == R.id.nav_guide){
            selectedFragment = new GuideFragment();
        }
           if(selectedFragment !=null){
               loadFragment(selectedFragment);
               return true;
           }
           return false;
       });
       if (savedInstanceState == null){
           loadFragment(new HomeFragment());
       }
    }
    private void loadFragment(Fragment fragment){
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container,fragment)
                .commit();
    }

}
