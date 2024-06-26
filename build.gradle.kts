// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    id("com.android.application") version "8.3.2" apply false
    id("org.jetbrains.kotlin.android") version "1.8.10" apply false
    id("com.google.dagger.hilt.android") version "2.44" apply false
    id("org.jlleitschuh.gradle.ktlint") version "10.1.0"
}

buildscript {
    dependencies {
        classpath("org.jlleitschuh.gradle:ktlint-gradle:10.2.1")
    }
}
