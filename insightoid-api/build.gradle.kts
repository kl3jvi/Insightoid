import com.vanniktech.maven.publish.SonatypeHost

plugins {
    id("org.jetbrains.kotlin.android")
    id("com.android.library")
    id("com.vanniktech.maven.publish") version "0.25.3"
}


android {
    namespace = "com.kl3jvi.insightoid_api"
    compileSdk = 34
    defaultConfig {
        minSdk = 21
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dependencies {
    // koin for dependency injection
    implementation(libs.koin.android)
    implementation(libs.retrofit)
    implementation(libs.arrow.core)
    implementation(libs.gson)
    implementation(libs.converter.gson)
    implementation(libs.androidx.work.runtime.ktx)
}
