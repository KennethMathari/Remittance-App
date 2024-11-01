import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidLibrary)
    kotlin("native.cocoapods")
    kotlin("plugin.serialization") version "1.9.0"
    id("kotlin-parcelize")
    id("dev.mokkery") version "2.3.0"
}

kotlin {
    androidTarget {
        @OptIn(ExperimentalKotlinGradlePluginApi::class)
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_11)
        }
    }

    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "Shared"
            isStatic = true
        }
    }

    cocoapods {
        // Specify the podspec version
        version = "1.15.2"
        summary = "iOS platform-specific libraries"
        homepage = "https://example.com/shared"
        ios.deploymentTarget = "15.0"
        pod("MSAL")
        podfile = project.file("../iosApp/Podfile") // Path to your iOS app's Podfile
        framework {
            baseName = "Shared"
            isStatic = true
        }
    }

    sourceSets {
        commonMain.dependencies {
            //Koin
            implementation(project.dependencies.platform(libs.koin.bom))
            implementation(libs.koin.core)
            //Datastore
            api(libs.datastore)
            api(libs.datastore.preferences)
            //Ktor
            implementation(libs.ktor.client.core)
            implementation(libs.ktor.client.content.negotiation)
            implementation(libs.ktor.client.logging)
            implementation(libs.ktor.serialization.kotlinx.json)
            implementation(libs.ktor.client.auth)
            //DateTime
            implementation(libs.kotlinx.datetime)
            // For Base64 encoding
            implementation(libs.kotlinx.serialization.core)
            // For hashing in KMP
            implementation(libs.okio)


        }

        androidMain.dependencies {
            //Koin
            implementation(project.dependencies.platform(libs.koin.bom))
            implementation(libs.koin.android)
            //Ktor
            implementation(libs.ktor.client.okhttp)
            //MSAL
            implementation("com.microsoft.identity.client:msal:5.4.2") {
                exclude(group = "com.microsoft.device.display")
            }
        }

        iosMain.dependencies {
            //Ktor
            implementation(libs.ktor.client.darwin)
        }
    }
}

android {
    namespace = "org.tawakal.composemphelloworld.shared"
    compileSdk = libs.versions.android.compileSdk.get().toInt()
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    defaultConfig {
        minSdk = libs.versions.android.minSdk.get().toInt()
    }
}
