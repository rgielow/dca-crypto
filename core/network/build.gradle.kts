plugins {
    id("cryptofolio.kmp.library")
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "com.cryptofolio.core.network"
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(projects.core.common)
            implementation(projects.domain.model)
            implementation(libs.findLibrary("ktor-client-core").get())
            implementation(libs.findLibrary("ktor-client-content-negotiation").get())
            implementation(libs.findLibrary("ktor-serialization-json").get())
            implementation(libs.findLibrary("ktor-client-logging").get())
            implementation(libs.findLibrary("kotlinx-serialization-json").get())
            implementation(libs.findLibrary("koin-core").get())
        }
        androidMain.dependencies {
            implementation(libs.findLibrary("ktor-client-okhttp").get())
        }
        iosMain.dependencies {
            implementation(libs.findLibrary("ktor-client-darwin").get())
        }
    }
}
