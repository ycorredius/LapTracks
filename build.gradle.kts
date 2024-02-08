
// Top-level build file where you can add configuration options common to all sub-projects/modules.

buildscript{
  extra.apply {
    set("room_version", "2.6.1")
  }
}

plugins {
  id("com.android.application") version "8.2.1" apply false
  id("org.jetbrains.kotlin.android") version "1.9.0" apply false
  id("com.google.dagger.hilt.android") version "2.50" apply false
  id("com.google.devtools.ksp") version "1.9.22-1.0.16"
  id("com.google.gms.google-services") version "4.4.0" apply false
}
