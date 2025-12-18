package com.example.appbienvenidos.view.activities;

import static android.app.ProgressDialog.show;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDelegate;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.appbienvenidos.R;
import com.example.appbienvenidos.utils.LocaleHelper;
import com.google.android.material.card.MaterialCardView;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Locale;

public class ParametreActivity extends BaseActivity {
    private ImageButton btnback;
    private View info_pers, notifs, theme, language, logout;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parametre);

        btnback = findViewById(R.id.btnBack);

        int nightModeFlags = getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
        //information personnelle
        info_pers = findViewById(R.id.info_pers);
        reglage(info_pers, getString(R.string.info_pers), R.drawable.outline_person_24, "");

        //Notifications
        notifs = findViewById(R.id.notifs);
        reglage(notifs, "Notifications", R.drawable.ic_notifications_n, getString(R.string.activer));

        //language
        language = findViewById(R.id.language);

        String currentLangLabel = getCurrentLanguageLabel();
        reglage(language, getString(R.string.lang),R.drawable.ic_language ,currentLangLabel);
        language.setOnClickListener(v -> {
            showChangeLanguageDialog();
        });

        //theme
        theme = findViewById(R.id.theme);
        String currentThemeTxt = (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES) ? getString(R.string.sombre) : getString(R.string.clair);
        reglage(theme, getString(R.string.theme), R.drawable.ic_theme, currentThemeTxt);

        theme.setOnClickListener(v -> showThemeDialog());
        info_pers.setOnClickListener(v ->{
            Intent intent = new Intent(this, EditProfileActivity.class);
            startActivity(intent);
        });

        btnback.setOnClickListener(v -> finish());

        logout = findViewById(R.id.btnlogout);
        logout.setOnClickListener(v -> {
            new AlertDialog.Builder(this)
                    .setTitle(getString(R.string.decnx))
                    .setMessage(getString(R.string.msg_dcnx))
                    .setNegativeButton(getString(R.string.annuler), null)
                    .setPositiveButton(getString(R.string.log_out), (dialog, which) -> {
                        //la déconnexion de firabase aussi
                        FirebaseAuth.getInstance().signOut();
                        Intent intent = new Intent(this, LoginActivity.class);
                        //pour empêcher l'utilisateur de revenir en arrière avec le btn retour
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        finish();
                    })
                    .show();
        });

    }

    private String getCurrentLanguageLabel(){
        String langCode = java.util.Locale.getDefault().getLanguage();

        if(langCode.equals("en")){
            return getString(R.string.en);
        } else {
            return getString(R.string.fr);
        }
    }
    private void reglage(View view, String title, int image, String value){
        ((TextView)
                view.findViewById(R.id.rowTitle)).setText(title);

        ((ImageView)
                view.findViewById(R.id.rowIcon)).setImageResource(image);

        ((TextView) view.findViewById(R.id.rowValue)).setText(value);
    }

    private void showThemeDialog(){
        final String[] themes = {getString(R.string.clair), getString(R.string.sombre)};

        //on vérifier quel est le mode actuel pour cocher la bonne case
        int checkedItem = 0;
        int currentMode = AppCompatDelegate.getDefaultNightMode();
        if(currentMode == AppCompatDelegate.MODE_NIGHT_YES){
            checkedItem = 1;
        }
        else if (currentMode == AppCompatDelegate.MODE_NIGHT_NO){
            checkedItem = 0;
        }

        new AlertDialog.Builder(this)
                .setTitle(getString(R.string.choix_theme))
                .setSingleChoiceItems(themes, checkedItem, (dialog, which) -> {
                    if(which == 0){
                        //forcer le mode clair

                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                        ((TextView)
                        theme.findViewById(R.id.rowValue)).setText(getString(R.string.clair));
                    }
                    else if(which == 1){
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                        ((TextView)
                                theme.findViewById(R.id.rowValue)).setText(getString(R.string.sombre));
                    }
                    dialog.dismiss();
                })
                .setNegativeButton(getString(R.string.annuler), null)

                .show();
    }

    private void showChangeLanguageDialog(){
        final String[] langs = {getString(R.string.fr) , getString(R.string.en)};

        int checkedLang = 0;
        String lang = getCurrentLanguageLabel();
        if(lang.equals(getString(R.string.en))){
            checkedLang = 1;
        } else {
            checkedLang = 0;
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.lng_choice));

        builder.setSingleChoiceItems(langs, checkedLang, (dialog, i) -> {
            if(i == 0){
                LocaleHelper.setLocale(this,"fr");
                recreate();
                ((TextView)
                        language.findViewById(R.id.rowValue)).setText(getString(R.string.fr));
            } else if (i == 1){
                LocaleHelper.setLocale(this,"en");
                recreate();
                ((TextView)
                        language.findViewById(R.id.rowValue)).setText(getString(R.string.en));
            }

            dialog.dismiss();
            Intent intent = new Intent(this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

}
