plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.example.assignment_prm_su25"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.assignment_prm_su25"
        minSdk = 24
        targetSdk = 35
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
    // AndroidX Core
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    
    // Testing
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
    
    // UI Components
    implementation("androidx.cardview:cardview:1.0.0")
    implementation("androidx.recyclerview:recyclerview:1.3.2")
    implementation("com.google.android.material:material:1.11.0")
    
    // Image Loading
    implementation("com.github.bumptech.glide:glide:4.16.0")
    annotationProcessor("com.github.bumptech.glide:compiler:4.16.0")
    
    // Toast Messages
    implementation("com.github.GrenderG:Toasty:1.5.2")
    
    // Rounded ImageView
    implementation("com.makeramen:roundedimageview:2.3.0")
    
    // Circle ImageView
    implementation("de.hdodenhof:circleimageview:3.1.0")
    
    // Shimmer Effect
    implementation("com.facebook.shimmer:shimmer:0.5.0")
    
    // Email Sending
    implementation("com.sun.mail:android-mail:1.6.7")
    implementation("com.sun.mail:android-activation:1.6.7")
}