plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)

    id("kotlin-android")
    id("com.google.devtools.ksp")
}

android {
    namespace = "com.example.bangkitevent"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.bangkitevent"
        minSdk = 30
        //noinspection OldTargetApi,OldTargetApi
        targetSdk = 34
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
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
    buildFeatures {
        viewBinding = true
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.lifecycle.livedata.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    // retrofit
    implementation(libs.retrofit)
    implementation(libs.converter.gson)

    // error handling
    implementation(libs.logging.interceptor)

    // image
    implementation (libs.github.glide)

    // searchbar
    implementation (libs.material)
    implementation (libs.androidx.constraintlayout)

    // submission 2
    // room
    implementation(libs.androidx.room.runtime)
    ksp(libs.room.compiler)

    // coroutine support
    implementation(libs.androidx.room.ktx)
}