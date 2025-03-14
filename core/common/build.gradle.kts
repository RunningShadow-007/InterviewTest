plugins {
    id("java-library")
    alias(libs.plugins.jetbrains.kotlin.jvm)
    kotlin("kapt")
}
java {
    sourceCompatibility = JavaVersion.valueOf(libs.versions.javaVersion.get())
    targetCompatibility = JavaVersion.valueOf(libs.versions.javaVersion.get())
}
kotlin {
    compilerOptions {
        jvmTarget =
            org.jetbrains.kotlin.gradle.dsl.JvmTarget.fromTarget(libs.versions.jvmTarget.get())
    }
}

dependencies {
    implementation(libs.hilt.core)
    kapt(libs.hilt.compiler)
    implementation(libs.kotlinx.coroutines.core)
    testImplementation(libs.kotlinx.coroutines.test)
}

