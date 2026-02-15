plugins {
    id("cryptofolio.kmp.feature")
}

android {
    namespace = "com.cryptofolio.feature.portfolio"
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
            implementation(libs.findLibrary("kotlinx-datetime").get())
        }
    }
}
