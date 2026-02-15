plugins {
    id("cryptofolio.kmp.library")
    id("cryptofolio.compose.multiplatform")
}

android {
    namespace = "com.cryptofolio.core.designsystem"
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(projects.core.common)
        }
    }
}
