plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("kotlin-kapt")
}

android {

    signingConfigs {
        create("release") {
            val keystoreFile = file("keystore/lotto_keystore.jks")
            if (keystoreFile.exists()) {
                storeFile = keystoreFile
                storePassword = project.findProperty("LOTTO_KEYSTORE_PASSWORD") as String? ?: "Lottosmart108821!!"
                keyAlias = project.findProperty("LOTTO_KEY_ALIAS") as String? ?: "ENGYX"
                keyPassword = project.findProperty("LOTTO_KEY_PASSWORD") as String? ?: "Lottosmart108821!!"
            }
        }
    }
    
    namespace = "com.example.lottoarchive"
    compileSdk = 34

    defaultConfig {
        buildConfigField("String", "BUILD_TIME", "\"DEV\"")
        buildConfigField("String", "KEY_ALIAS", "\"ENGYX\"")
        buildConfigField("String", "KEY_OWNER", "\"Angelo Mancarella, Siracusa Italia\"")
    
        applicationId = "com.example.lottoarchive"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        release {
            signingConfig = signingConfigs.getByName("release")
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
        debug {
            signingConfig = signingConfigs.getByName("release")
            isMinifyEnabled = false
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
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.11"
    }
    packaging {
        resources.excludes.add("/META-INF/{AL2.0,LGPL2.1}")
    }
}

dependencies {
    val composeBom = platform("androidx.compose:compose-bom:2024.09.03")
    implementation(composeBom)
    androidTestImplementation(composeBom)

    implementation("androidx.core:core-ktx:1.13.1")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.8.6")
    implementation("androidx.activity:activity-compose:1.9.2")
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-tooling-preview")
    debugImplementation("androidx.compose.ui:ui-tooling")
    implementation("androidx.compose.material3:material3:1.3.0")
    implementation("androidx.compose.material:material-icons-extended:1.7.3")
    implementation("androidx.navigation:navigation-compose:2.8.2")

    // Room
    implementation("androidx.room:room-runtime:2.6.1")
    implementation("androidx.room:room-ktx:2.6.1")
    kapt("androidx.room:room-compiler:2.6.1")

    // Rhino JS engine
    implementation("org.mozilla:rhino:1.7.14")

    // CSV
    implementation("com.opencsv:opencsv:5.9")
}
