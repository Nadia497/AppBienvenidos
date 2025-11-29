package com.example.appbienvenidos.view.activities;

import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.content.Intent;


import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.appbienvenidos.Data.AppDataBase;
import com.example.appbienvenidos.Data.DAO.UserDAO;
import com.example.appbienvenidos.Data.Entities.User;
import com.example.appbienvenidos.R;

public class SignupActivity extends AppCompatActivity {
   /* EditText editTextFirstName, editTextLastName, editTextEmail, editTextLocation, editTextPasswordHash ;
    Button btnRoleTraveler, btnRoleLocal, btnSinscrire;
    TextView alreadyRegistred, seConnecter;
    ImageButton btnAddPhoto;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_signup);

        // Ajustement automatique des marges système
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        editTextFirstName =findViewById(R.id.editTextFirstName);
        editTextLastName =findViewById(R.id.editTextLastName);
        editTextEmail =findViewById(R.id.editTextEmail);
        editTextLocation =findViewById(R.id.editTextLocation);
        editTextPasswordHash =findViewById(R.id.editTextPasswordHash);
        //
        btnRoleTraveler =findViewById(R.id.roleTraveler);
        btnRoleLocal =findViewById(R.id.roleLocal);
        btnSinscrire =findViewById(R.id.Sinscrire);
        //
        alreadyRegistred =findViewById(R.id.alreadyRegistred);
        seConnecter =findViewById(R.id.SeConnecter);

        btnAddPhoto =findViewById(R.id.add_photo);

        btnAddPhoto.setOnClickListener((v -> pickImage()));

        btnSinscrire.setOnClickListener(v-> {
            String FirstName= editTextFirstName.getText().toString();
            String LastName= editTextLastName.getText().toString();
            String Email= editTextEmail.getText().toString();
            String Location= editTextLocation.getText().toString();
            String PasswordHash= editTextPasswordHash.getText().toString();
            String ProfilePicture;
            //String FirstName= editTextFirstName.getText().toString();

            User user = new User(FirstName, LastName, Email, ProfilePicture, Location, PasswordHash);
            AppDataBase db= AppDataBase.getDatabase(this);
            UserDAO UserDAO=db.userDAO();
            new Thread(()->{
                userDAO.insertUser(user);
                runOnUiThread(()->{

                });
            }).start();
        });

    }
    private void pickImage(){
        Intent intent= new Intent(MediaStore.ACTION_PICK_IMAGES);
        intent.setType("image/*");
        startActivityForResult(intent,PICK_IMAGE);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data){

    }*/
}
