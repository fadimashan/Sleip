// Top-level build file where you can add configuration options common to all sub-projects/modules.

buildscript {
    repositories {
        // Gradle 4.1 and higher include support for Google's Maven repo using
        // the google() method. And you need to include this repo to download
        // Android Gradle plugin 3.0.0 or higher.
        google()
        jcenter()
        mavenCentral()
        maven { url = uri("https://plugins.gradle.org/m2/") }
        maven { url = uri("https://maven.google.com") }

    }
    dependencies {
        classpath(BuildTools.androidGradle)
        classpath(kotlin("gradle-plugin", version = Versions.kotlinGradle))
        classpath(BuildTools.navigationSafeArgs)
        classpath(BuildTools.detektGradle)
        classpath(BuildTools.androidOssPlugin)
        classpath("org.koin:koin-gradle-plugin:${Versions.koin}")
        classpath("org.jetbrains.kotlin:kotlin-serialization:${Versions.kotlinGradle}")
        classpath("com.google.gms:google-services:4.3.3")
        // NOTE: Do not place your application dependencies here; they belong
        // in the individual module build.gradle files
    }

}

allprojects {
    repositories {
        google()
        jcenter()
        maven { url = uri("https://jitpack.io") }
    }

    apply(plugin = BuildTools.detekt)
}

plugins {
    id(BuildTools.detekt).version(Versions.detekt)
}

detekt {
    debug = true
    parallel = true
    config = files("$projectDir/default-detekt-config.yml")
    input = files("$projectDir/src/navGraph/java")
    filters = ".*build.*,.*/resources/.*,.*/tmp/.*"
}

dependencies {
    detektPlugins(BuildTools.detektFormatting)

}


tasks.register<Delete>("clean").configure {
    delete(rootProject.buildDir)
}
