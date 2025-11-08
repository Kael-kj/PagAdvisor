// app/build.gradle.kts (Nível App)

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("org.jetbrains.kotlin.plugin.compose")
}

android {
    namespace = "com.Kenji.pagadvisor"
    // Definimos as versões diretamente com números
    compileSdk = 36

    defaultConfig {
        applicationId = "com.Kenji.pagadvisor"
        minSdk = 26
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
    buildFeatures {
        compose = true
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

// Definimos as dependências diretamente com suas strings
dependencies {

    // Compose BOM (Bill of Materials) - Esta ainda é a melhor prática
    // O BOM gerencia as versões de todas as bibliotecas do Compose
    val composeBom = platform("androidx.compose:compose-bom:2025.10.01")
    implementation(composeBom)
    androidTestImplementation(composeBom)

    // Dependências do Compose (sem versão, o BOM cuida)
    implementation("androidx.compose.material3:material3")
    implementation("androidx.compose.ui:ui-tooling-preview")
    debugImplementation("androidx.compose.ui:ui-tooling")

    // Activity & Lifecycle (para MVVM e Compose)
    implementation("androidx.activity:activity-compose:1.11.0")
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.9.4")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.9.4")

    // Navigation (Navegação)
    implementation("androidx.navigation:navigation-compose:2.9.6")

    // Retrofit (para a API)
    implementation("com.squareup.retrofit2:retrofit:3.0.0")
    implementation("com.squareup.retrofit2:converter-gson:3.0.0")
    implementation("com.squareup.okhttp3:logging-interceptor:4.12.0") // Log de API

    // Coroutines
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.10.2")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.10.2")

    // DataStore (para salvar login/onboarding)
    implementation("androidx.datastore:datastore-preferences:1.2.0-rc01")

    // Testes Unitários
    testImplementation("junit:junit:4.13.2")
    testImplementation("io.mockk:mockk:1.13.11")
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.10.2")

    // Testes de Instrumentação (AndroidTest)
    androidTestImplementation("androidx.test.ext:junit:1.1.5")

    // Lista de Icon
    implementation("androidx.compose.material:material-icons-extended")
}