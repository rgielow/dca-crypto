plugins {
    id("cryptofolio.kmp.library")
    alias(libs.plugins.ksp)
    alias(libs.plugins.room)
}

android {
    namespace = "com.cryptofolio.core.database"
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(projects.core.common)
            implementation(projects.domain.model)
            implementation(libs.findLibrary("room-runtime").get())
            implementation(libs.findLibrary("sqlite-bundled").get())
            implementation(libs.findLibrary("koin-core").get())
            implementation(libs.findLibrary("kotlinx-datetime").get())
        }
    }
}

room {
    schemaDirectory("$projectDir/schemas")
}

dependencies {
    listOf("kspAndroid", "kspIosX64", "kspIosArm64", "kspIosSimulatorArm64").forEach {
        add(it, libs.findLibrary("room-compiler").get())
    }
}
