plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.example.appbienvenidos"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.example.appbienvenidos"
        minSdk = 24
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

dependencies {

    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    implementation(libs.firebase.analytics)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
    implementation("com.google.android.material:material:1.12.0")
    implementation("com.google.android.gms:play-services-maps:18.1.0")
    implementation("org.osmdroid:osmdroid-android:6.1.14")
    //firebase
    // 1. LE CHEF D'ORCHESTRE (Le BOM)
    // Il gère les versions pour tout le monde. Plus besoin de mettre les numéros en bas.
    implementation(platform("com.google.firebase:firebase-bom:33.1.0"))
    implementation("com.google.firebase:firebase-auth")
    // 3. Pour stocker les Images (Au lieu du Base64)
    implementation("com.google.firebase:firebase-storage")
    // 2. AJOUTEZ ANALYTICS (Ce que vous cherchiez)
    implementation("com.google.firebase:firebase-analytics")
    // 1. Pour la Base de données (Remplacer SQLite)
    implementation("com.google.firebase:firebase-firestore")
    //glide pour afficher les img depuis une URL depuis firebase
    implementation("com.github.bumptech.glide:glide:4.16.0")

    //sdp
    implementation("com.intuit.sdp:sdp-android:1.1.0")
    //circle image view
    implementation("de.hdodenhof:circleimageview:3.1.0")

    // Ajoutez ceci pour le MVVM
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.6.2")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.6.2")
    implementation("androidx.fragment:fragment-ktx:1.6.2")

    //cloudinary
    implementation("com.cloudinary:cloudinary-android:2.5.0")
}

apply(plugin = "com.google.gms.google-services")