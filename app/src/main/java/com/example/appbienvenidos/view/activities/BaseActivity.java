package com.example.appbienvenidos.view.activities;

import android.content.Context;
import androidx.appcompat.app.AppCompatActivity;
import com.example.appbienvenidos.utils.LocaleHelper; // Importez votre Helper

public class BaseActivity extends AppCompatActivity {

    // C'est cette méthode magique qui applique la langue avant même que l'écran s'allume
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(LocaleHelper.onAttach(newBase));
    }
}