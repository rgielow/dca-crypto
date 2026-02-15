plugins {
    id("cryptofolio.kmp.library")
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "com.cryptofolio.domain.model"
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            api(projects.core.common)
            implementation(libs.findLibrary("kotlinx-serialization-json").get())
            implementation(libs.findLibrary("kotlinx-datetime").get())
        }
    }
}
