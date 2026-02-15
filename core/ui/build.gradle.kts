plugins {
    id("cryptofolio.kmp.library")
    id("cryptofolio.compose.multiplatform")
}

android {
    namespace = "com.cryptofolio.core.ui"
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(projects.core.common)
            implementation(projects.core.designsystem)
            implementation(projects.domain.model)
            implementation(libs.findLibrary("androidx-lifecycle-viewmodel").get())
            implementation(libs.findLibrary("androidx-lifecycle-runtime-compose").get())
            implementation(libs.findLibrary("kotlinx-datetime").get())
        }
    }
}
