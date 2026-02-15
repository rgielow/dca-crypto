plugins {
    id("cryptofolio.kmp.feature")
}

android {
    namespace = "com.cryptofolio.feature.settings"
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(projects.core.common)
            implementation(projects.core.ui)
            implementation(projects.core.designsystem)
            implementation(projects.domain.model)
        }
    }
}
