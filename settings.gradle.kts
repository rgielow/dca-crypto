enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

pluginManagement {
    includeBuild("build-logic")
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}
plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0"
}

dependencyResolutionManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
    }
}

rootProject.name = "CryptoFolio"

include(":androidApp")
include(":composeApp")
include(":core:common")
include(":core:network")
include(":core:database")
include(":core:designsystem")
include(":core:ui")
include(":domain:model")
include(":domain:repository")
include(":feature:transaction")
include(":feature:portfolio")
include(":feature:asset-detail")
include(":feature:settings")
