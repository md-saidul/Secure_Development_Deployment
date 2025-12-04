plugins { // Gradle plugins applied to this module
    alias(libs.plugins.android.application) // Use the Android Application plugin via the version catalogue alias
}
android { // Android-specific build configuration for the app module
    namespace = "com.example.hospimanagementapp" // Package namespace used for R classes & manifest
    compileSdk = 36 // Compile against Android API level 36 (must match an installed SDK)
    defaultConfig { // Default settings for all build variants
        applicationId = "com.example.hospimanagementapp" // Unique app ID used on devices/Play Store
        minSdk = 26 // Minimum Android version the app supports (Android 7.0)
        targetSdk = 36 // Target behaviour for API level 36 (opt-in to latest platform changes)
        versionCode = 1 // Internal version (integer) used for updates
        versionName = "1.0" // Human-readable version shown to users
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner" // Runner for instrumented (device) tests
    }

    buildTypes { // Definitions of build variants like debug/release
        release { // Settings for the release (shipping) build
            isMinifyEnabled = false // Disable code shrinking/obfuscation (set true for production usually)
            proguardFiles( // ProGuard/R8 rules used when shrinking/optimising
                getDefaultProguardFile("proguard-android-optimize.txt"), // Standard optimised rules supplied by the Android plugin
                "proguard-rules.pro" // Your custom rules file
            )
        }
    }

    buildFeatures { // Toggle optional Android build features
        viewBinding = true // Generate binding classes for views to avoid findViewById
    }

    compileOptions { // Java language level the compiler targets/accepts
        sourceCompatibility = JavaVersion.VERSION_11 // Allow Java 11 language features in source
        targetCompatibility = JavaVersion.VERSION_11 // Compile bytecode compatible with Java 11
    }
}
dependencies { // Libraries this module depends on
    implementation ("androidx.appcompat:appcompat:1.7.0")
    implementation ("com.google.android.material:material:1.12.0")
    implementation ("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation ("androidx.recyclerview:recyclerview:1.3.2")
    implementation ("androidx.annotation:annotation:1.9.1")


    // Room
    implementation ("androidx.room:room-runtime:2.6.1")
    implementation(libs.activity)
    annotationProcessor ("androidx.room:room-compiler:2.6.1")

    // Lifecycle / ViewModel
    implementation ("androidx.lifecycle:lifecycle-runtime:2.8.6")
    implementation ("androidx.lifecycle:lifecycle-livedata:2.8.6")
    implementation ("androidx.lifecycle:lifecycle-viewmodel:2.8.6")

    // Retrofit / OkHttp (mock + logging)
    implementation ("com.squareup.retrofit2:retrofit:2.11.0")
    implementation ("com.squareup.retrofit2:converter-gson:2.11.0")
    implementation ("com.squareup.okhttp3:okhttp:4.12.0")
    implementation ("com.squareup.okhttp3:logging-interceptor:4.12.0")

    // Biometric
    implementation ("androidx.biometric:biometric:1.2.0-alpha05")

    // Paging
    implementation ("androidx.paging:paging-runtime:3.3.2")

    // Workmanager for offline vitals
    implementation ("androidx.work:work-runtime:2.9.1")

    // ZXing
    implementation ("com.journeyapps:zxing-android-embedded:4.3.0")
    implementation ("com.google.zxing:core:3.5.3")

    testImplementation ("junit:junit:4.13.2")
    androidTestImplementation ("androidx.test.ext:junit:1.2.1")
    androidTestImplementation ("androidx.test.espresso:espresso-core:3.6.1")
}