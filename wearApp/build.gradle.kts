@file:Suppress("UnstableApiUsage")

import java.util.Properties

val localProperties = Properties().apply {
    load(project.rootProject.file("local.properties").inputStream())
}

plugins {
    alias(libs.plugins.androidApplication)
    kotlin("android")
    alias(libs.plugins.kotlinx.serialization)
}

android {
    compileSdk = 34
    defaultConfig {
        applicationId = "dev.johnoreilly.gemini"
        minSdk = 30
        targetSdk = 33
        versionCode = 1
        versionName = "1.0"

        buildConfigField(
            "String",
            "GEMINI_API_KEY",
            "\"" + localProperties["GEMINI_API_KEY"] + "\"",
        )
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
        isCoreLibraryDesugaringEnabled = true
    }

    buildFeatures {
        compose = true
        buildConfig = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = libs.versions.compose.compiler.get()
    }

    testOptions {
        unitTests {
            isIncludeAndroidResources = true
        }
    }

    kotlinOptions {
        this.jvmTarget = "1.8"
        freeCompilerArgs += "-opt-in=kotlin.RequiresOptIn"
        freeCompilerArgs += "-opt-in=com.google.android.horologist.annotations.ExperimentalHorologistApi"
    }

    packaging {
        resources {
            excludes += listOf(
                "/META-INF/AL2.0",
                "/META-INF/LGPL2.1",
                "/META-INF/versions/**"
            )
        }
    }

    namespace = "dev.johnoreilly.gemini"
}

kotlin {
    sourceSets.all {
        languageSettings {
            optIn("kotlin.RequiresOptIn")
        }
    }
}

dependencies {
    implementation(libs.compose.foundation)
    implementation(libs.horologist.composables)
    implementation(libs.horologist.compose.layout)
    implementation(libs.horologist.ai.ui)
    implementation(libs.markdown.renderer.core)
    implementation(libs.compose.material.iconsext)
    implementation(libs.kotlinx.coroutines)
    implementation(libs.bundles.ktor.common)
    implementation(libs.ktor.client.okhttp)
    coreLibraryDesugaring(libs.desugar)
}
