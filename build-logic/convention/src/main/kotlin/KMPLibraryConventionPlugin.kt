import com.android.build.gradle.LibraryExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

class KMPLibraryConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) = with(target) {
        with(pluginManager) {
            apply(libs.plugin("kotlin-multiplatform"))
            apply(libs.plugin("android-library"))
        }

        extensions.configure<KotlinMultiplatformExtension> {
            androidTarget {
                compilerOptions {
                    jvmTarget.set(JvmTarget.JVM_17)
                }
            }

            listOf(
                iosX64(),
                iosArm64(),
                iosSimulatorArm64(),
            ).forEach { iosTarget ->
                iosTarget.binaries.framework {
                    baseName = target.name
                    isStatic = true
                }
            }

            sourceSets.commonMain.dependencies {
                implementation(libs.library("kotlinx-coroutines-core"))
            }

            sourceSets.commonTest.dependencies {
                implementation(libs.library("kotlin-test"))
                implementation(libs.library("kotlinx-coroutines-test"))
            }
        }

        extensions.configure<LibraryExtension> {
            configureAndroid(this)
        }
    }
}
