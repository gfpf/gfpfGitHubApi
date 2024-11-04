plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    id("kotlin-kapt")
    kotlin("plugin.serialization") version "1.6.10"
}

android {
    namespace = "com.gfpf.github_api"
    compileSdk = 34

    defaultConfig {
        minSdk = 21

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
        debug {
        }
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    flavorDimensions.add("default")
    productFlavors {
        create("mock") {
            dimension = "default"
            //applicationIdSuffix = ".mock.debug"
            //versionNameSuffix = "-mock-debug"
        }

        create("prod") {
            dimension = "default"
            /*applicationIdSuffix = ".prod.debug"
            versionNameSuffix = "-prod-debug"
            versionName = "${defaultConfig.versionName}.test"*/
        }
    }

    androidComponents {
        // Remove mockRelease as it's not needed.
        beforeVariants { variantBuilder ->
            if (variantBuilder.buildType == "release" && variantBuilder.productFlavors.any { it.second == "mock" }) {
                variantBuilder.enable = false
            }
        }
    }

    kotlinOptions {
        jvmTarget = "1.8"
    }

    // Apply Hilt plugin and configuration
    /*hilt {
        enableExperimentalClasspathAggregation = true
    }*/
}

dependencies {
    implementation("androidx.core:core-ktx:1.15.0")
    implementation("androidx.appcompat:appcompat:1.7.0")
    implementation("com.google.android.material:material:1.12.0")
    implementation("org.jetbrains.kotlin:kotlin-stdlib:2.0.21")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.8.7")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.8.7")

    // Retrofit
    implementation("com.squareup.retrofit2:retrofit:2.11.0")

    // Moshi without KotlinJsonAdapterFactory
    implementation("com.squareup.moshi:moshi:1.15.1")
    kapt("com.squareup.moshi:moshi-kotlin-codegen:1.15.1")
    //implementation("com.squareup.moshi:moshi-kotlin:1.12.0")

    // Moshi Converter
    implementation("com.squareup.retrofit2:converter-moshi:2.11.0")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.7.3")
    //implementation("com.squareup.retrofit2:converter-gson:2.11.0")

    // Logging
    implementation("com.squareup.okhttp3:logging-interceptor:5.0.0-alpha.14")
    implementation("com.localebro:okhttpprofiler:1.0.8")

    // Hilt
    implementation("com.google.dagger:hilt-android:2.52")
    kapt("com.google.dagger:hilt-android-compiler:2.52")

    // Test
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.2.1")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.6.1")
}