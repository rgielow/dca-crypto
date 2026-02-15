plugins {
    id("cryptofolio.kmp.library")
    id("cryptofolio.compose.multiplatform")
    id("cryptofolio.koin")
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "com.cryptofolio.app"
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(projects.core.common)
            implementation(projects.core.ui)
            implementation(projects.core.designsystem)
            implementation(projects.core.network)
            implementation(projects.core.database)
            implementation(projects.domain.model)
            implementation(projects.domain.repository)
            implementation(projects.feature.transaction)
            implementation(projects.feature.portfolio)
            implementation(projects.feature.assetDetail)
            implementation(projects.feature.settings)
            implementation(libs.findLibrary("navigation-compose").get())
            implementation(libs.findLibrary("kotlinx-serialization-json").get())
            implementation(libs.findLibrary("androidx-lifecycle-viewmodel").get())
            implementation(libs.findLibrary("androidx-lifecycle-runtime-compose").get())
        }
        androidMain.dependencies {
            implementation(libs.findLibrary("koin-android").get())
        }
    }
}
