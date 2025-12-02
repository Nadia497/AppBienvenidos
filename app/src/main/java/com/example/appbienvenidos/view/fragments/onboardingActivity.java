package com.example.appbienvenidos.view.fragments;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import com.example.appbienvenidos.R;
import com.example.appbienvenidos.view.activities.MainActivity;
import com.example.appbienvenidos.view.activities.SignupActivity ;
import com.example.appbienvenidos.view.activities.LoginActivity;

import androidx.viewpager2.widget.ViewPager2;
import android.content.Intent;

public class onboardingActivity extends AppCompatActivity {
    private Button continuer, passer;
    private String destinationfinale;
    private ViewPager2 v2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_onboarding);

        continuer = findViewById(R.id.btn_continue);
        passer = findViewById(R.id.btn_passer);
        v2 = findViewById(R.id.viewPager);

        v2.setAdapter(new onboardingadapter(this));

        // 1. On récupère l'information envoyée depuis l'écran précédent
        if(getIntent().hasExtra("DESTINATION")){
            destinationfinale = getIntent().getStringExtra("DESTINATION");
        }

        continuer.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v){
                if(v2.getCurrentItem() < 2){
                    v2.setCurrentItem(v2.getCurrentItem()+1);
                } else{
                    if("HOME".equals(destinationfinale)){
                        startActivity(new Intent(onboardingActivity.this, MainActivity.class));
                    }
                    else{
                        startActivity(new Intent(onboardingActivity.this, SignupActivity.class));
                    }
                }
            }
        });

        passer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if("HOME".equals(destinationfinale)){
                    startActivity(new Intent(onboardingActivity.this, MainActivity.class));
                }
                else{
                    startActivity(new Intent(onboardingActivity.this, SignupActivity.class));
                }

            }
        });
    }
}