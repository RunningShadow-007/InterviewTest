plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.compose) apply false
    alias(libs.plugins.kotlin.parcelize) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.kotlin.kapt) apply false
//    id ("com.google.dagger.hilt.android") version "2.55" apply false
    kotlin("jvm") version "2.0.0" apply false
    alias(libs.plugins.ksp) apply false
    alias(libs.plugins.hilt.android) apply false
    alias(libs.plugins.kotlin.serialization) apply false
}

