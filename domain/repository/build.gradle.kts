plugins {
    id("cryptofolio.kmp.library")
}

android {
    namespace = "com.cryptofolio.domain.repository"
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            api(projects.domain.model)
            api(projects.core.common)
        }
    }
}
