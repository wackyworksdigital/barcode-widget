import java.io.FileInputStream
import java.util.Properties

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
}

// Release signing config is read from keystore.properties (gitignored). When the
// file is absent (e.g. CI or web sessions), release builds stay unsigned so debug
// builds still work without the secret.
val keystorePropsFile = rootProject.file("keystore.properties")
val hasReleaseSigning = keystorePropsFile.exists()
val keystoreProps = Properties().apply {
    if (hasReleaseSigning) load(FileInputStream(keystorePropsFile))
}

android {
    namespace = "digital.wackyworks.barcodewidget"
    compileSdk = 35

    defaultConfig {
        applicationId = "digital.wackyworks.barcodewidget"
        minSdk = 26
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"
    }

    signingConfigs {
        if (hasReleaseSigning) {
            create("release") {
                storeFile = rootProject.file(keystoreProps.getProperty("storeFile"))
                storePassword = keystoreProps.getProperty("storePassword")
                keyAlias = keystoreProps.getProperty("keyAlias")
                keyPassword = keystoreProps.getProperty("keyPassword")
            }
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            if (hasReleaseSigning) {
                signingConfig = signingConfigs.getByName("release")
            }
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions {
        jvmTarget = "17"
    }
}

dependencies {
    implementation("com.google.zxing:core:3.5.3")
    testImplementation("junit:junit:4.13.2")
}
