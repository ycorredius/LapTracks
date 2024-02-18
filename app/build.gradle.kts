plugins {
  id("com.android.application")
  id("org.jetbrains.kotlin.android")
  kotlin("kapt")
  id("com.google.devtools.ksp")
  id("com.google.dagger.hilt.android")
  id("com.google.gms.google-services")
}

android {
  namespace = "com.shaunyarbrough.laptracks"
  compileSdk = 34

  defaultConfig {
    applicationId = "com.shaunyarbrough.laptracks"
    minSdk = 24
    targetSdk = 34
    versionCode = 3
    versionName = "1.0.2"

    testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    vectorDrawables {
      useSupportLibrary = true
    }
  }

  buildTypes {
    release {
      isMinifyEnabled = false
      proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
    }
  }
  compileOptions {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
  }
  kotlinOptions {
    jvmTarget = "1.8"
  }
  buildFeatures {
    compose = true
  }
  composeOptions {
    kotlinCompilerExtensionVersion = "1.5.1"
  }
  packaging {
    resources {
      excludes += "/META-INF/{AL2.0,LGPL2.1}"
    }
  }
}

dependencies {
  implementation("androidx.navigation:navigation-testing:2.7.6")
  val nav_version = "2.7.6"
  val hilt_version = "2.50"

  implementation("androidx.core:core-ktx:1.12.0")
  implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.6.2")
  implementation("androidx.activity:activity-compose:1.8.2")
  implementation(platform("androidx.compose:compose-bom:2023.08.00"))
  implementation("androidx.compose.ui:ui")
  implementation("androidx.compose.ui:ui-graphics")
  implementation("androidx.compose.ui:ui-tooling-preview")
  implementation("androidx.compose.material3:material3")
  implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.6.2")
  implementation("androidx.navigation:navigation-compose:$nav_version")

  //Room database dependencies
  implementation("androidx.room:room-runtime:${rootProject.extra["room_version"]}")
  ksp("androidx.room:room-compiler:${rootProject.extra["room_version"]}")
  implementation("androidx.room:room-ktx:${rootProject.extra["room_version"]}")

  // Compose navhost dependencies
  implementation("androidx.hilt:hilt-navigation-compose:1.1.0")

  //Dagger hilt dependencies
  implementation("com.google.dagger:hilt-android:$hilt_version")
  kapt("com.google.dagger:hilt-android-compiler:$hilt_version")
  kapt("androidx.hilt:hilt-compiler:1.1.0")

  //Firebase dependencies
  implementation(platform("com.google.firebase:firebase-bom:32.7.1"))
  implementation("com.google.firebase:firebase-analytics")

  //hilt testing dependencies - currently not in use.
  kaptTest("com.google.dagger:hilt-android-compiler:2.50")
  androidTestImplementation("com.google.dagger:hilt-android-testing:2.50")
  kaptAndroidTest("com.google.dagger:hilt-android-compiler:2.50")

  //Retrofit dependencies
  // Retrofit
  implementation("com.squareup.retrofit2:retrofit:2.9.0")

  // Retrofit with Scalar Converter
  implementation("com.squareup.retrofit2:converter-gson:2.9.0")

  //DataStore preferences
  implementation("androidx.datastore:datastore-preferences:1.0.0")

  //Hilt dagger testing dependencies
  androidTestImplementation ("com.google.dagger:hilt-android-testing:2.50")
  androidTestAnnotationProcessor("com.google.dagger:hilt-compiler:2.50")

  testImplementation("com.google.dagger:hilt-android-testing:2.50")
  testAnnotationProcessor("com.google.dagger:hilt-compiler:2.50")

  testImplementation("junit:junit:4.13.2")
  testImplementation("org.robolectric:robolectric:4.11.1")
  androidTestImplementation("androidx.test.ext:junit:1.1.5")
  androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
  androidTestImplementation(platform("androidx.compose:compose-bom:2023.08.00"))
  androidTestImplementation("androidx.compose.ui:ui-test-junit4")
  debugImplementation("androidx.compose.ui:ui-tooling")
  debugImplementation("androidx.compose.ui:ui-test-manifest")
}

kapt {
  correctErrorTypes = true
}