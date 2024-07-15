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

    kotlinOptions {
        jvmTarget = "1.8"
    }

    // Apply Hilt plugin and configuration
    /*hilt {
        enableExperimentalClasspathAggregation = true
    }*/
}

androidComponents {
    // Remove mockRelease as it's not needed.
    beforeVariants { variantBuilder ->
        if (variantBuilder.buildType == "release" && variantBuilder.productFlavors.any { it.second == "mock" }) {
            variantBuilder.enable = false
        }
    }
}

dependencies {
    implementation("androidx.core:core-ktx:1.13.1")
    implementation("androidx.appcompat:appcompat:1.7.0")
    implementation("com.google.android.material:material:1.12.0")
    implementation("org.jetbrains.kotlin:kotlin-stdlib:1.9.23")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.8.3")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.8.3")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.3.0")

    //Retrofit
    implementation("com.squareup.retrofit2:retrofit:2.11.0")
    implementation("com.squareup.retrofit2:converter-moshi:2.9.0")
    implementation("com.squareup.moshi:moshi-kotlin:1.12.0")
    implementation("com.squareup.okhttp3:logging-interceptor:5.0.0-alpha.14")
    implementation("com.localebro:okhttpprofiler:1.0.8")

    // Dagger
    /*implementation("com.google.dagger:dagger:2.51.1")
    kapt("com.google.dagger:dagger-compiler:2.51.1")*/

    // Hilt dependencies
    implementation("com.google.dagger:hilt-android:2.51.1")
    kapt("com.google.dagger:hilt-android-compiler:2.51.1")

    // Test
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.2.1")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.6.1")
}