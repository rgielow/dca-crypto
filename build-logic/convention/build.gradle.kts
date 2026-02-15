plugins {
    `kotlin-dsl`
}

dependencies {
    compileOnly(libs.android.gradlePlugin)
    compileOnly(libs.kotlin.gradlePlugin)
    compileOnly(libs.compose.gradlePlugin)
    compileOnly(libs.composeCompiler.gradlePlugin)
    compileOnly(libs.room.gradlePlugin)
}

gradlePlugin {
    plugins {
        register("kmpLibrary") {
            id = "cryptofolio.kmp.library"
            implementationClass = "KMPLibraryConventionPlugin"
        }
        register("kmpFeature") {
            id = "cryptofolio.kmp.feature"
            implementationClass = "KMPFeatureConventionPlugin"
        }
        register("androidApplication") {
            id = "cryptofolio.android.application"
            implementationClass = "AndroidApplicationConventionPlugin"
        }
        register("androidLibrary") {
            id = "cryptofolio.android.library"
            implementationClass = "AndroidLibraryConventionPlugin"
        }
        register("composeMultiplatform") {
            id = "cryptofolio.compose.multiplatform"
            implementationClass = "ComposeMultiplatformConventionPlugin"
        }
        register("koin") {
            id = "cryptofolio.koin"
            implementationClass = "KoinConventionPlugin"
        }
    }
}
