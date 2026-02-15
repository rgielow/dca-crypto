plugins {
    id("cryptofolio.kmp.library")
}

android {
    namespace = "com.cryptofolio.core.common"
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(libs.findLibrary("kotlinx-datetime").get())
        }
    }
}
