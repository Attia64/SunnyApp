// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    id("com.android.application") version "8.2.2" apply false
    id("org.jetbrains.kotlin.android") version "1.9.22" apply false
    //Digger Hilt
    id("com.google.dagger.hilt.android") version "2.51.1" apply false
}
val blurviewVersion by extra("version-2.0.3")
