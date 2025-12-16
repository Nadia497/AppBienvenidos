package com.example.appbienvenidos.view.activities;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.appbienvenidos.view.fragments.ProfileFragment;
import com.example.appbienvenidos.viewmodel.GuideViewModel;
import com.google.android.material.switchmaterial.SwitchMaterial;
import com.google.firebase.auth.FirebaseAuth;

import com.example.appbienvenidos.model.User;
import com.example.appbienvenidos.model.Guide;
import androidx.appcompat.app.AlertDialog;
import android.view.LayoutInflater;
import com.example.appbienvenidos.viewmodel.ProfileViewModel;
import com.example.appbienvenidos.repository.GuideRepository;
import android.net.Uri;
import android.content.Intent;
import android.provider.MediaStore;

import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;
import com.bumptech.glide.Glide;

import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseUser;
import com.google.android.material.textfield.TextInputEditText;

import com.example.appbienvenidos.R;
import com.google.firebase.auth.TotpSecret;

import static android.app.Activity.RESULT_OK;

public class EditProfileActivity extends AppCompatActivity {
    private static final int PICK_IMAGE = 100;
    private ImageButton btnback;
    private ImageView user_photo, btn_add_img;
    private EditText lastname, firstname, location, specialite, cityServed, languages, hourlyRate, phoneNumber;
    private TextView email, role_local, role_guide, role_travler;
    private LinearLayout password, roleswitch, infos_guide;
    private Button save;
    private SwitchMaterial available;
    private FirebaseAuth mAuth;
    private String currrentUserId, selectedRole = "", urlPhoto = "";
    private ProfileViewModel viewModel;
    private GuideViewModel guideViewModel;
    private GuideRepository guidereRepository;
    private Guide guide = null;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_profile_activity);

        mAuth = FirebaseAuth.getInstance();
        if(mAuth.getCurrentUser() != null){
            currrentUserId = mAuth.getCurrentUser().getUid();
        } else {
            Toast.makeText(this,"Erreur : Utilisateur non connecté", Toast.LENGTH_SHORT).show();
            return;
        }

        guidereRepository = new GuideRepository();
        initialisation();

        viewModel = new ViewModelProvider(this).get(ProfileViewModel.class);
        guideViewModel = new ViewModelProvider(this).get(GuideViewModel.class);
        //observer les données
        viewModel.getUser().observe(this, user -> {
            if (user != null) {
                lastname.setText(user.getLastName());
                firstname.setText(user.getFirstName());
                location.setText(user.getLocation());
                email.setText(user.getEmail());

                urlPhoto = user.getPhotoUrl();
                //Affichage de l'url de l'image dans le logcat
                Log.d("DEBUG_IMAGE", "URL reçue : "+urlPhoto);

                if (urlPhoto != null && !urlPhoto.isEmpty()) {
                    Glide.with(this)
                            .load(urlPhoto.trim())
                            .diskCacheStrategy(com.bumptech.glide.load.engine.DiskCacheStrategy.NONE) // Cache intelligent
                            .skipMemoryCache(true)
                            .placeholder(R.drawable.ic_launcher_background)
                            .error(R.drawable.no_profile)
                            .circleCrop()
                            .into(user_photo);
                }
                else {
                    user_photo.setImageResource(R.drawable.no_profile);
                }

                String userRole = user.getRole().trim();

                Log.e("TEST_DEBUG", "1. Role brut reçu de la BDD: '" + userRole + "'");

                // Dans EditProfileActivity.java, bloc "Guide"

                Log.e("TEST_ULTIME", "Je lance la recherche manuelle pour l'ID: " + currrentUserId);

                com.google.firebase.firestore.FirebaseFirestore.getInstance()
                        .collection("Guide") // <--- Vérifiez ce nom
                        .document(currrentUserId)
                        .get()
                        .addOnSuccessListener(documentSnapshot -> {
                            if (documentSnapshot.exists()) {
                                Log.e("TEST_ULTIME", "TROUVÉ ! Données brutes : " + documentSnapshot.getData());
                            } else {
                                Log.e("TEST_ULTIME", "PAS TROUVÉ. Le document avec l'ID " + currrentUserId + " n'existe pas dans la collection 'Guide'.");
                            }
                        })
                        .addOnFailureListener(e -> {
                            Log.e("TEST_ULTIME", "ERREUR CRITIQUE : " + e.getMessage());
                        });

                if (userRole == null) userRole = "Local";

                selectedRole = userRole;

                if("Voyageur".equalsIgnoreCase(userRole)){
                    infos_guide.setVisibility(View.GONE);
                    roleswitch.setVisibility(View.GONE);
                    role_travler.setVisibility(View.VISIBLE);
                    role_travler.setText("Voyageur");
                }
                else if("Guide".equalsIgnoreCase(userRole)) {
                    roleswitch.setVisibility(View.GONE);
                    role_travler.setVisibility(View.VISIBLE);
                    role_travler.setText("Guide");
                    infos_guide.setVisibility(View.VISIBLE);

                    guideViewModel.loadGuideProfile(currrentUserId);

                    guideViewModel.getGuide().observe(this, guideRecuperer -> {
                        if(guideRecuperer != null){
                            guide = guideRecuperer;
                            specialite.setText(guideRecuperer.getSpecialities());
                            languages.setText(guideRecuperer.getLangages());
                            cityServed.setText(guideRecuperer.getCityServed());
                            hourlyRate.setText(guideRecuperer.getHourlyRate());
                            phoneNumber.setText(guideRecuperer.getPhoneNumber());

                            if(available != null) available.setChecked(guideRecuperer.isAvailable());
                        }
                    });

                }
                else{
                    roleswitch.setVisibility(View.VISIBLE);
                    role_travler.setVisibility(View.GONE);

                    if("Guide".equalsIgnoreCase(selectedRole)){
                        infos_guide.setVisibility(View.VISIBLE);
                    }else {
                        infos_guide.setVisibility(View.GONE);
                    }

                    updateRoleUI();
                }

            }
        });

        //observer les messages (Toast)
        viewModel.getToastMessage().observe(this, message -> {
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        });

        //charger les données
        viewModel.loadUserProfile(currrentUserId);

        // Quand on clique sur l'image -> ouvrir la galrie
        user_photo.setOnClickListener(v -> {
            // 1. On crée une intention : "Je veux choisir (ACTION_PICK) une donnée externe"
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            // 2. On lance la galerie et on attend un résultat avec le code 100 (PICK_IMAGE)
            startActivityForResult(intent, PICK_IMAGE);
        });

        role_local.setOnClickListener(v -> {
            selectedRole = "Local";
            updateRoleUI();
        });

        role_guide.setOnClickListener(v -> {
            if("Guide".equals(selectedRole)){
                return;
            }
            User currentUser = viewModel.getUser().getValue();
            if (currentUser != null && "Guide".equals(currentUser.getRole())) {
                // L'utilisateur est déjà guide en BDD, on change juste l'onglet visuellement
                selectedRole = "Guide";
                updateRoleUI();
                infos_guide.setVisibility(View.VISIBLE);
            } else {
                // C'est un vrai nouveau guide -> on affiche le formulaire
                showBecomGuideDialog();
            }
        });

        save.setOnClickListener(v -> {
            //les champs commun entre tous les users
            String updateFirstName = firstname.getText().toString().trim();
            String updateLastName = lastname.getText().toString().trim();
            String updatedLocation = location.getText().toString().trim();

            //les champs des guides
            String updateSpec = specialite.getText().toString().trim();
            String updateLang = languages.getText().toString().trim();
            String updateCity = cityServed.getText().toString().trim();
            String updateRate = hourlyRate.getText().toString().trim();
            String updateTele = phoneNumber.getText().toString().trim();

            if(updatedLocation.isEmpty() || updateFirstName.isEmpty() || updateLastName.isEmpty()){
                Toast.makeText(this, "Veuillez remplir tous les champs s'il vous plaiez!", Toast.LENGTH_SHORT).show();
                return;
            }

            if("Guide".equalsIgnoreCase(selectedRole)){
                if(updateSpec.isEmpty() || updateLang.isEmpty() || updateCity.isEmpty() || updateRate.isEmpty() || updateTele.isEmpty()){
                    Toast.makeText(this, "Veuillez remplir les informations du guide", Toast.LENGTH_SHORT).show();
                    return;
                }
            }

            //mise à jour les information dans la collection "users"
            viewModel.updateTextOnly(currrentUserId, updateLastName, updateFirstName, updatedLocation, selectedRole);

            //si l'utilisateur devenir un guide on lui ajoute à la collection "guide"
            if("Guide".equalsIgnoreCase(selectedRole)){

                if (guide == null) {
                    // On récupère les infos minimales pour la mise à jour
                    guide = new Guide();
                    guide.setUid(currrentUserId);
                }
                else {
                    guide.setUid(currrentUserId);
                }
                //c'est trés important de modifier les infos du guide aussi pour correspondre aux informations modifiers du user
                guide.setFirstName(updateFirstName);
                guide.setLastName(updateLastName);
                guide.setProfileImageUrl(urlPhoto);

                guide.setSpecialities(updateSpec);
                guide.setLangages(updateLang);
                guide.setCityServed(updateCity);
                guide.setPhoneNumber(updateTele);
                guide.setHourlyRate(updateRate);

                if(available != null){
                    guide.setAvailable(available.isChecked());
                }

                guidereRepository.addGuide(guide, new GuideRepository.GuideCallback() {
                    @Override
                    public void onSuccess() {
                        Toast.makeText(EditProfileActivity.this, "Profil et Guide mis à jour avec succès !", Toast.LENGTH_SHORT).show();
                        finish();
                    }

                    @Override
                    public void onError(String msg) {
                        Toast.makeText(EditProfileActivity.this, "Erreur : "+ msg, Toast.LENGTH_SHORT).show();
                    }
                });
            } else {
                Toast.makeText(EditProfileActivity.this, "Profil mis à jour avec succès !", Toast.LENGTH_SHORT).show();
                finish();
            }
        });

        password.setOnClickListener(v -> {
            showChangePasswordDialog();
        });

        btnback.setOnClickListener(v -> finish());
    }

    public void initialisation(){
        user_photo = findViewById(R.id.user_photo);
        btn_add_img = findViewById(R.id.btn_add_img);
        lastname = findViewById(R.id.editTextLastName);
        firstname = findViewById(R.id.editTextFirstName);
        location = findViewById(R.id.editTextLocation);
        email = findViewById(R.id.editTextEmail);
        role_local = findViewById(R.id.roleLocal);
        role_guide = findViewById(R.id.roleGuide);
        password = findViewById(R.id.pwd);
        save = findViewById(R.id.enregistrer);
        btnback = findViewById(R.id.btnBack);
        role_travler = findViewById(R.id.roleTraveler);
        roleswitch = findViewById(R.id.role);
        infos_guide = findViewById(R.id.info_guide);
        specialite = findViewById(R.id.editTextSpecilite);
        cityServed = findViewById(R.id.editTextCityserved);
        languages = findViewById(R.id.editTextLanguages);
        hourlyRate = findViewById(R.id.editTextRate);
        phoneNumber = findViewById(R.id.editTextTele);
        available = findViewById(R.id.switchAvailability);
    }
    //Récupérer l'image choisie
    //appelée automatiquement quand l'utilisateur a choisi une photo (ou annulé)
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Vérification : Est-ce bien ma demande de photo (100) ? Et est-ce que l'utilisateur
        // a bien choisi quelque chose (RESULT_OK) ?
        if (requestCode == PICK_IMAGE && resultCode == RESULT_OK && data != null) {

            // 1. Récupérer l'adresse de l'image choisie dans le téléphone
            Uri selectedImage = data.getData();
            if (selectedImage != null) {
                user_photo.setImageURI(selectedImage);
                viewModel.uploadImage(currrentUserId, selectedImage);
            }
        }
    }

    private void updateRoleUI() {
        if (selectedRole.equalsIgnoreCase("Guide")) {
            role_guide.setBackgroundResource(R.drawable.bg_role_active);
            role_guide.setTextColor(Color.WHITE);
            role_local.setBackgroundResource(0);
            role_local.setTextColor(ContextCompat.getColor(this, R.color.pink_300));

            if(infos_guide != null) infos_guide.setVisibility(View.VISIBLE);
        } else {
            role_guide.setBackgroundResource(0);
            role_guide.setTextColor(ContextCompat.getColor(this, R.color.pink_300));
            role_local.setBackgroundResource(R.drawable.bg_role_active);
            role_local.setTextColor(ContextCompat.getColor(this, R.color.white_pure));

            if(infos_guide != null) infos_guide.setVisibility(View.GONE);
        }
    }

    public void showBecomGuideDialog(){
        AlertDialog.Builder build = new AlertDialog.Builder(this);

        View view = LayoutInflater.from(this).inflate(R.layout.become_guide, null);
        build.setView(view);

        EditText spec = view.findViewById(R.id.specialities);
        EditText lang = view.findViewById(R.id.language);
        EditText city = view.findViewById(R.id.city);
        EditText rate = view.findViewById(R.id.Rate);
        EditText tele = view.findViewById(R.id.tele);

        build.setPositiveButton("Valider", null);
        build.setNegativeButton("Annuler", (dialog, which) -> {
            guide = null;
            selectedRole = "Local";
            updateRoleUI();
            dialog.dismiss();
        });

        AlertDialog dialog = build.create();
        dialog.show();

        //gestion du bouton "valider manuellement pour empêcher la fermeture si erreurs
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(v -> {
            String specialities = spec.getText().toString().trim();
            String langue = lang.getText().toString().trim();
            String ville = city.getText().toString().trim();
            String hourlyRate = rate.getText().toString().trim();
            String telephone = tele.getText().toString().trim();

            if(specialities.isEmpty() || langue.isEmpty() || ville.isEmpty() || hourlyRate.isEmpty() || telephone.isEmpty()){
                Toast.makeText(this, "Veuillez tout remplir" , Toast.LENGTH_SHORT).show();
                return;
            }

            String firstName = firstname.getText().toString();
            String lastName = lastname.getText().toString();

            guide = new Guide(currrentUserId, firstName, lastName, urlPhoto, ville, hourlyRate, specialities, langue, telephone , true);
            selectedRole  = "Guide";
            updateRoleUI();
            dialog.dismiss();
        });

    }

    private void showChangePasswordDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_change_password, null);
        builder.setView(view);

        TextInputEditText etOldPass = view.findViewById(R.id.etOldPassword);
        TextInputEditText etNewPass = view.findViewById(R.id.etNewPassword);
        TextInputEditText etConfirmPass = view.findViewById(R.id.etConfirmNewPassword);

        builder.setPositiveButton("Modifier", null); // On met null ici pour gérer le clic nous-mêmes plus bas
        builder.setNegativeButton("Annuler", (dialog, which) -> dialog.dismiss());

        AlertDialog dialog = builder.create();
        dialog.show();

        // On gère le clic sur "Modifier" ici pour empêcher la fermeture si erreur
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(v -> {
            String oldPass = etOldPass.getText().toString().trim();
            String newPass = etNewPass.getText().toString().trim();
            String confirmPass = etConfirmPass.getText().toString().trim();

            // 1. Vérifications de base
            if (oldPass.isEmpty() || newPass.isEmpty() || confirmPass.isEmpty()) {
                Toast.makeText(this, "Veuillez remplir tous les champs", Toast.LENGTH_SHORT).show();
                return;
            }
            if (!newPass.equals(confirmPass)) {
                Toast.makeText(this, "Les nouveaux mots de passe ne correspondent pas", Toast.LENGTH_SHORT).show();
                return;
            }
            if (newPass.length() < 6) {
                Toast.makeText(this, "Le mot de passe doit contenir au moins 6 caractères", Toast.LENGTH_SHORT).show();
                return;
            }

            // 2. Logique Firebase : Ré-authentification
            FirebaseUser user = mAuth.getCurrentUser();
            if (user != null && user.getEmail() != null) {
                // Créer les identifiants pour vérifier l'ancien mot de passe
                AuthCredential credential = EmailAuthProvider.getCredential(user.getEmail(), oldPass);

                // Tenter de se reconnecter
                user.reauthenticate(credential).addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        // L'ancien mot de passe est correct, on peut mettre à jour
                        user.updatePassword(newPass).addOnCompleteListener(taskUpdate -> {
                            if (taskUpdate.isSuccessful()) {
                                Toast.makeText(this, "Mot de passe modifié avec succès !", Toast.LENGTH_SHORT).show();
                                dialog.dismiss();
                            } else {
                                Toast.makeText(this, "Erreur mise à jour : " + taskUpdate.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    } else {
                        // L'ancien mot de passe est incorrect
                        Toast.makeText(this, "L'ancien mot de passe est incorrect", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }
}
