import org.jetbrains.kotlin.config.AnalysisFlags.experimental

//apply(plugin = "com.google.android.gms.oss-licenses-plugin")
apply(plugin = "com.android.application")
apply(plugin = "kotlin-android-extensions")
apply(plugin = "com.google.gms.google-services")


plugins {
    id("com.android.application")
    kotlin("android")
    kotlin("android.extensions")
    kotlin("kapt")
    id("androidx.navigation.safeargs.kotlin")
    id("kotlin-android")
    id("kotlin-android-extensions")
    id("kotlinx-serialization")

}


android {
    compileSdkVersion(Android.compileSdk)
    defaultConfig {
        resConfigs("en", "sv")
        applicationId = Android.applicationId
        minSdkVersion(Android.minSdk)
        targetSdkVersion(Android.targetSdk)
        versionCode = Android.versionCode
        versionName = Android.versionName
        testInstrumentationRunner
        testOptions {
            animationsDisabled = true
        }


    }


    signingConfigs {
        create("release") {
            // Note that none of this should be in any file that is on git or any other version control.
            // Preferably in the local.properties file.
            storeFile = file("../sample-keystore")
            storePassword = "sample"
            keyAlias = "sample"
            keyPassword = "sample"
        }
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = true
            isShrinkResources = true
            setProguardFiles(
                listOf(
                    getDefaultProguardFile("proguard-android-optimize.txt"),
                    "proguard-rules.pro"
                )
            )

            signingConfig = signingConfigs.getByName("release")
        }
        getByName("debug") {
            ext["alwaysUpdateBuildId"] = false
            isTestCoverageEnabled = true
            splits.abi.isEnable = false
            splits.density.isEnable = false
            aaptOptions.cruncherEnabled = false
        }
    }


    flavorDimensions("version")
    productFlavors {
        create("dev") {
            setDimension("version")
            applicationIdSuffix = ".dev"
            versionNameSuffix = "-dev"
        }

        create("prod") {
            setDimension("version")

            splits {
                density {
                    isEnable = true
                    exclude("ldpi")
                    compatibleScreens("small", "normal", "large", "xlarge")
                }

                abi {
                    isEnable = true
                    reset()

                    include("x86", "x86_64", "arm64-v8a", "armeabi-v7a")
                    isUniversalApk = false
                }
            }
        }
    }

    applicationVariants.all {
        val lintTask = tasks["lint${name.capitalize()}"]
        assembleProvider?.get()?.run {
            dependsOn += lintTask
        }
    }

    lintOptions {
        isWarningsAsErrors = true
        isCheckAllWarnings = true
        isAbortOnError = false
        lintConfig = file("../lint.xml")
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    kotlinOptions {
        jvmTarget = "1.8"
    }

    useLibrary("android.test.runner")

    androidExtensions {
        experimental
    }
}


dependencies {
    implementation(kotlin("stdlib", org.jetbrains.kotlin.config.KotlinCompilerVersion.VERSION))
    implementation(Deps.appCompat)
    implementation(Deps.constraintLayout)
    implementation(Deps.material)
    implementation(Deps.design)

    implementation(Deps.okHttp)
    implementation(Deps.retrofit)
    implementation(Deps.retrofitMoshi)
    implementation(Deps.retrofitRx)
    implementation(Deps.glide)
    implementation(Deps.legacy)
    implementation(Deps.lifecycleExtensions)
    implementation(Deps.lifecycleViewModel)
    implementation(Deps.jsonKotlinSerialization)
    implementation("androidx.legacy:legacy-support-v4:1.0.0")
    implementation("androidx.lifecycle:lifecycle-extensions:2.2.0")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.2.0")
    implementation("androidx.appcompat:appcompat:1.1.0")
    implementation("androidx.constraintlayout:constraintlayout:1.1.3")
    kapt(Deps.glideAnnotation)

    implementation ("androidx.camera:camera-core:1.0.0-alpha10")
    implementation ("androidx.camera:camera-camera2:1.0.0-alpha10")
    implementation ("androidx.camera:camera-lifecycle:1.0.0-alpha10")
    implementation ("androidx.camera:camera-view:1.0.0-alpha08")
    implementation ("androidx.camera:camera-extensions:1.0.0-alpha08")
    implementation ("com.google.firebase:firebase-messaging:20.1.7")
    implementation ("com.google.firebase:firebase-analytics:17.4.1")

    implementation(Deps.httpLogger)

    implementation(Deps.androidxCompat)
    implementation(Deps.archLifecycle)
    implementation(Deps.navigationFragment)
    implementation(Deps.navigationUi)
    implementation(Deps.karumiDexter)
    implementation(Deps.serializationConverter)

    implementation(Deps.rxJava)
    implementation(Deps.rxKotlin)
    implementation(Deps.rxAndroid)
    implementation(Deps.ChiliPhotoPicker)
    implementation(Deps.pickphotoview)
    implementation(Deps.dexter)


    implementation(Deps.koin)
    implementation(Deps.koinViewModel)
    implementation(Deps.koinScope)
    implementation(Deps.koinFragment)
    implementation(Deps.androidxCoreKtx)
    implementation(Deps.betterVideoPlayer)

    implementation(Deps.timber)
    implementation(Deps.androidOss)
    implementation(Deps.landscapeVideoCamera)
    implementation(Deps.exoplayerCore)
    implementation(Deps.exoplayerDash)
    implementation(Deps.exoplayer)
    implementation(Deps.exoplayerUi)
    implementation(Deps.workManager)

    implementation(Deps.paging)
    implementation(Deps.pagingrx)
    implementation(Deps.permissions)
    implementation(Deps.legacyPre)
    implementation(Deps.circularimageview)
    implementation(Deps.rxpermissions)


    testImplementation(Deps.junit)
    testImplementation(Deps.mockk)
    testImplementation(Deps.okHttpMockServer)
    testImplementation(Deps.archTesting)
    testImplementation(Deps.livedataTesting)
    testImplementation(Deps.expekt)

    androidTestImplementation(Deps.espresso)
    androidTestImplementation(Deps.testCore)
    androidTestImplementation(Deps.testRunner)
    androidTestImplementation(Deps.androidTestRule)
    androidTestImplementation(Deps.androidMockk)

}
