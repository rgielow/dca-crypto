import org.gradle.api.Plugin
import org.gradle.api.Project
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.gradle.kotlin.dsl.configure

class KMPFeatureConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) = with(target) {
        with(pluginManager) {
            apply("cryptofolio.kmp.library")
            apply("cryptofolio.compose.multiplatform")
            apply("cryptofolio.koin")
            apply(libs.plugin("kotlin-serialization"))
        }

        extensions.configure<KotlinMultiplatformExtension> {
            sourceSets.commonMain.dependencies {
                implementation(libs.library("navigation-compose"))
                implementation(libs.library("androidx-lifecycle-viewmodel"))
                implementation(libs.library("androidx-lifecycle-runtime-compose"))
                implementation(libs.library("kotlinx-serialization-json"))
            }

            sourceSets.commonTest.dependencies {
                implementation(libs.library("androidx-lifecycle-viewmodel-core"))
                implementation(project(":core:testing"))
            }
        }
    }
}
