plugins {
    id("org.jetbrains.kotlin.android")
    id("com.android.library")
}

android {
    namespace = "com.kl3jvi.insightoid_api"
    compileSdk = 31

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
    // gson
    implementation(libs.gson)
    implementation(libs.androidx.work.runtime.ktx)
}
