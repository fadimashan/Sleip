object Versions {
    const val androidGradle = "4.0.0"
    const val kotlinGradle = "1.3.70"
    const val fragmentVersion = "1.2.0"
    const val design = "28.0.0"
    const val karumiDexter = "5.0.0"
    const val appCompat = "1.1.0"
    const val constraintLayout = "2.0.0-beta4"
    const val detekt = "1.1.1"
    const val okHttp = "4.2.2"
    const val retrofit = "2.6.1"
    const val serializationConverter = "0.5.0"
    const val archLifecycle = "2.2.0-rc02"
    const val archCoreTesting = "2.0.0"
    const val navigation = "2.2.0-rc02"
    const val rxJava = "2.2.14"
    const val rxAndroid = "2.1.1"
    const val pickphotoview = "0.4.8"
    const val ChiliPhotoPicker = "0.3.1"
    const val dexter = "5.0.0"
    const val rxKotlin = "2.4.0"
    const val koin = "2.1.3"
    const val androidxCoreKtx = "1.2.0"
    const val betterVideoPlayer = "2.0.0-alpha01"
    const val material = "1.1.0-beta02"
    const val googleCloudStorage = "1.106.0"
    const val androidxCompat = "1.0.0"
    const val navigationSafeArgs = "2.1.0"
    const val glide = "4.11.0"
    const val livedataTesting = "1.0.0"
    const val timber = "4.7.1"
    const val legacy = "1.0.0"
    const val legacyPre = "1.1.0"
    const val lifecycleExtensions = "2.2.0"
    const val lifecycleViewModel = "2.2.0"
    const val jsonKotlinSerialization = "0.20.0"
    const val androidOssPlugin = "0.9.5"
    const val androidOss = "16.0.2"
    const val circularimageview = "3.2.0"
    const val rxpermissions = "0.10.2"
    const val landscapeVideoCamera = "1.3.0"
    const val exoplayer = "2.10.7"
    const val workManager = "2.3.4"
    const val junit = "4.13-rc-1"
    const val testRunner = "1.1.0"
    const val testCore = "1.0.0"
    const val espresso = "3.1.0"
    const val mockk = "1.9.3.kotlin12"
    const val expekt = "0.5.0"
    const val paging = "2.1.0"
    const val permission = "1.1.1"

}


object BuildTools {
    const val androidOssPlugin =
        "com.google.android.gms:oss-licenses-plugin:${Versions.androidOssPlugin}"
    const val androidGradle = "com.android.tools.build:gradle:${Versions.androidGradle}"
    const val detekt = "io.gitlab.arturbosch.detekt"
    const val detektGradle = "io.gitlab.arturbosch.detekt:detekt-gradle-plugin:${Versions.detekt}"
    const val detektFormatting = "io.gitlab.arturbosch.detekt:detekt-formatting:${Versions.detekt}"
    const val navigationSafeArgs =
        "androidx.navigation:navigation-safe-args-gradle-plugin:${Versions.navigationSafeArgs}"
}

@Suppress("unused")
object Deps {
    const val appCompat = "androidx.appcompat:appcompat:${Versions.appCompat}"
    const val constraintLayout =
        "androidx.constraintlayout:constraintlayout:${Versions.constraintLayout}"
    const val material = "com.google.android.material:material:${Versions.material}"
    const val googleCloudStorage =
        "com.google.cloud:google-cloud-storage:${Versions.googleCloudStorage}"
    const val androidxCompat = "androidx.legacy:legacy-support-v4:${Versions.androidxCompat}"
    const val design = "com.android.support:design:${Versions.design}"
    const val karumiDexter = "com.karumi:dexter:${Versions.karumiDexter}"

    const val archLifecycle = "androidx.lifecycle:lifecycle-extensions:${Versions.archLifecycle}"
    const val navigationFragment =
        "androidx.navigation:navigation-fragment-ktx:${Versions.navigation}"
    const val navigationUi = "androidx.navigation:navigation-ui-ktx:${Versions.navigation}"

    const val glide = "com.github.bumptech.glide:glide:${Versions.glide}"
    const val glideAnnotation = "com.github.bumptech.glide:compiler:${Versions.glide}"
    const val timber = "com.jakewharton.timber:timber:${Versions.timber}"
    const val legacy = "androidx.legacy:legacy-support-v4:${Versions.legacy}"
    const val legacyPre = "androidx.preference:preference-ktx:${Versions.legacyPre}"
    const val lifecycleExtensions =
        "androidx.lifecycle:lifecycle-extensions:${Versions.lifecycleExtensions}"
    const val lifecycleViewModel =
        "androidx.lifecycle:lifecycle-viewmodel-ktx:${Versions.lifecycleViewModel}"
    const val jsonKotlinSerialization =
        "org.jetbrains.kotlinx:kotlinx-serialization-runtime:${Versions.jsonKotlinSerialization}"
    const val circularimageview = "com.mikhaellopez:circularimageview:${Versions.circularimageview}"
    const val rxpermissions = "com.github.tbruyelle:rxpermissions:${Versions.rxpermissions}"
    const val landscapeVideoCamera = "com.github.jeroenmols:LandscapeVideoCamera:${Versions.landscapeVideoCamera}"
    const val exoplayerCore = "com.google.android.exoplayer:exoplayer-core:${Versions.exoplayer}"
    const val exoplayerDash = "com.google.android.exoplayer:exoplayer-dash:${Versions.exoplayer}"
    const val exoplayer = "com.google.android.exoplayer:exoplayer:${Versions.exoplayer}"
    const val exoplayerUi = "com.google.android.exoplayer:exoplayer-ui:${Versions.exoplayer}"
    const val workManager = "androidx.work:work-runtime-ktx:${Versions.workManager}"


    const val rxJava = "io.reactivex.rxjava2:rxjava:${Versions.rxJava}"
    const val rxKotlin = "io.reactivex.rxjava2:rxkotlin:${Versions.rxKotlin}"
    const val rxAndroid = "io.reactivex.rxjava2:rxandroid:${Versions.rxAndroid}"


    const val pickphotoview = "com.werb.pickphotoview:pickphotoview:${Versions.pickphotoview}"
    const val ChiliPhotoPicker =
        "com.github.ChiliLabs:ChiliPhotoPicker:${Versions.ChiliPhotoPicker}"
    const val dexter = "com.karumi:dexter:${Versions.dexter}"


    const val fragmentKtx = "androidx.fragment:fragment-ktx:${Versions.fragmentVersion}"
    const val fragmentKtxTesting = "androidx.fragment:fragment-testing:${Versions.fragmentVersion}"

    const val httpLogger = "com.squareup.okhttp3:logging-interceptor:${Versions.okHttp}"
    const val okHttp = "com.squareup.okhttp3:okhttp:${Versions.okHttp}"
    const val retrofit = "com.squareup.retrofit2:retrofit:${Versions.retrofit}"
    const val retrofitMoshi = "com.squareup.retrofit2:converter-moshi:${Versions.retrofit}"
    const val retrofitRx = "com.squareup.retrofit2:adapter-rxjava2:${Versions.retrofit}"
    const val serializationConverter =
        "com.jakewharton.retrofit:retrofit2-kotlinx-serialization-converter:${Versions.serializationConverter}"

    const val koin = "org.koin:koin-android:${Versions.koin}"
    const val koinViewModel = "org.koin:koin-androidx-viewmodel:${Versions.koin}"
    const val koinScope = "org.koin:koin-androidx-scope:${Versions.koin}"
    const val koinFragment = "org.koin:koin-androidx-fragment:${Versions.koin}"
    const val androidxCoreKtx = "androidx.core:core-ktx:${Versions.androidxCoreKtx}"
    const val betterVideoPlayer = "com.github.halilozercan:BetterVideoPlayer:${Versions.betterVideoPlayer}"


    const val junit = "junit:junit:${Versions.junit}"
    const val testCore = "androidx.test:core:${Versions.testCore}"
    const val testRunner = "androidx.test:runner:${Versions.testRunner}"
    const val espresso = "androidx.test.espresso:espresso-core:${Versions.espresso}"
    const val mockk = "io.mockk:mockk:${Versions.mockk}"
    const val okHttpMockServer = "com.squareup.okhttp3:mockwebserver:${Versions.okHttp}"
    const val archTesting = "androidx.arch.core:core-testing:${Versions.archCoreTesting}"
    const val livedataTesting = "com.jraska.livedata:testing-ktx:${Versions.livedataTesting}"
    const val expekt = "com.winterbe:expekt:${Versions.expekt}"

    const val paging = "androidx.paging:paging-runtime-ktx:${Versions.paging}"
    const val pagingrx = "androidx.paging:paging-rxjava2-ktx:${Versions.paging}"

    const val androidOss =
        "com.google.android.gms:play-services-oss-licenses:${Versions.androidOss}"

    const val permissions = "pub.devrel:easypermissions:${Versions.permission}"
    const val androidTestRule = "androidx.test:rules:1.2.0"
    const val androidMockk = "io.mockk:mockk-android:1.9.3"
}

@Suppress("unused")
object Android {
    const val applicationId = "se.mobileinteraction.sleip"
    const val compileSdk = 29
    const val minSdk = 26
    const val targetSdk = 29
    const val versionCode = 1
    const val versionName = "1.0"
    const val testRunner = "androidx.test.runner.AndroidJUnitRunner"
}
