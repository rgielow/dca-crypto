plugins {
    id("cryptofolio.kmp.library")
}

android {
    namespace = "com.cryptofolio.core.testing"
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            api(libs.findLibrary("kotlinx-coroutines-test").get())
        }
    }
}
