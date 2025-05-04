plugins {
    id("com.android.library")
    alias(libs.plugins.google.hilt)
    alias(libs.plugins.jetbrains.kotlin.android)
    alias(libs.plugins.jetbrains.kotlin.compose)
    alias(libs.plugins.jetbrains.kotlin.serialization)
    kotlin("kapt")
}

android {
    namespace = "com.libraries.core"
    compileSdk = libs.versions.compileSdk.get().toInt()

    defaultConfig {
        minSdk = libs.versions.minSdk.get().toInt()

        kapt {
            arguments {
                arg("room.schemaLocation", "$projectDir/schemas")
            }
        }
    }

    buildFeatures {
        compose = true
        buildConfig = true
    }

    compileOptions {
        sourceCompatibility = JavaVersion.valueOf(libs.versions.java.get())
        targetCompatibility = JavaVersion.valueOf(libs.versions.java.get())
    }

    kotlinOptions {
        jvmTarget = JavaVersion.valueOf(libs.versions.java.get()).majorVersion
    }
}

dependencies {
    // Libraries
    implementation(project(":libraries:ui"))

    // Androidx
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.core)
    implementation(libs.androidx.room.runtime)
    kapt(libs.androidx.room.compiler)
    implementation(libs.androidx.room.ktx)

    // Androidx Compose
    implementation(libs.androidx.compose.runtime)

    // Flipper
    debugImplementation(libs.facebook.flipper)
    debugImplementation(libs.facebook.flipper.network)
    debugImplementation(libs.facebook.soloader)

    // Hilt
    implementation(libs.google.hilt)
    kapt(libs.google.hilt.compiler)

    // Jet Brains
    implementation(libs.jetbrains.kotlin.coroutines)
    implementation(libs.jetbrains.kotlin.serialization)

    // Square
    implementation(libs.square.okhttp)
    implementation(libs.square.retrofit)
    implementation(libs.square.retrofit.converter.kotlinx)
    implementation(libs.square.retrofit.converter.scalars)
}
