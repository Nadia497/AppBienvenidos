package com.example.appbienvenidos.view.activities;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
public class RedirectionActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();

        if(user != null){
            Intent intent = new Intent(this , MainActivity.class);
            startActivity(intent);
        } else {
            Intent intent = new Intent(this , WelcomeActivity.class);
            startActivity(intent);
        }
        finish();
    }
}
