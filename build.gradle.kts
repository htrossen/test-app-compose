// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.jetbrains.kotlin.compose) apply false
    alias(libs.plugins.jetbrains.kotlin.android) apply false
    alias(libs.plugins.google.hilt) apply false
    alias(libs.plugins.ktlint)
}

buildscript {
    dependencies {
        classpath(libs.tools.ktlint)
    }
}

subprojects {
    apply {
        plugin("org.jlleitschuh.gradle.ktlint")
    }

    tasks.withType<Test> {
        useJUnitPlatform()
    }
}
