plugins {
    id("cryptofolio.android.application")
    alias(libs.plugins.compose.compiler)
}

android {
    namespace = "com.cryptofolio"

    defaultConfig {
        applicationId = "com.cryptofolio"
    }

    buildFeatures {
        compose = true
    }
}

dependencies {
    implementation(projects.composeApp)
    implementation(libs.findLibrary("androidx-activity-compose").get())
    implementation(libs.findLibrary("koin-android").get())
}
