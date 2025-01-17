package se.mobileinteraction.sleip.di

import androidx.preference.PreferenceManager
import com.google.android.exoplayer2.util.Log
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.squareup.moshi.Moshi
import kotlinx.serialization.UnstableDefault
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonConfiguration
import lv.chi.photopicker.ChiliPhotoPicker
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidApplication
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.CallAdapter
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import se.mobileinteraction.sleip.MainActivityViewModel
import se.mobileinteraction.sleip.R
import se.mobileinteraction.sleip.data.AuthorizationInterceptor
import se.mobileinteraction.sleip.data.SleipRepository
import se.mobileinteraction.sleip.data.Store
import se.mobileinteraction.sleip.data.api.SleipAPI
import se.mobileinteraction.sleip.entities.Horse
import se.mobileinteraction.sleip.ui.details.DetailsPageViewModel
import se.mobileinteraction.sleip.ui.login.LoginViewModel
import se.mobileinteraction.sleip.ui.mainFragment.MainHorsesListViewModel
import se.mobileinteraction.sleip.ui.newHorse.CreateHorseViewModel
import se.mobileinteraction.sleip.ui.records.AddingNewVideoRecordingViewModel
import se.mobileinteraction.sleip.ui.splash.SplashViewModel
import se.mobileinteraction.sleip.util.GlideImageLoader


@UnstableDefault
val appModule = module {
    factory<CallAdapter.Factory> { RxJava2CallAdapterFactory.create() }
    factory { Json.asConverterFactory("application/json".toMediaType()) }
    single {
        val clientBuilder = OkHttpClient.Builder()
            .addInterceptor(AuthorizationInterceptor(get()))
            //todo work manager api for upload file

        val httpInterceptor = HttpLoggingInterceptor()
        clientBuilder.addInterceptor(httpInterceptor)
        clientBuilder.addInterceptor { chain ->
            val request = chain.request()
            val logLevel = if (request.url.toString().contains("/recording/")) {
                HttpLoggingInterceptor.Level.NONE
            } else {
                HttpLoggingInterceptor.Level.BODY
            }
            Log.d("Log Level is: Level.NONE",request.url.toString().contains("/recording/").toString())
            clientBuilder.interceptors().forEach {
                if(it is HttpLoggingInterceptor){
                    it.level = logLevel

                }
            }
            chain.proceed(request)
        }


        Retrofit.Builder()
            .addCallAdapterFactory(get())
            .client(clientBuilder.build())
            .baseUrl(androidApplication().getString(R.string.base_api))
            .addConverterFactory(get())
            .build()
    }

    single {
        Json(JsonConfiguration.Stable)
    }

    single {
        val retrofit: Retrofit = get()
        retrofit.create(SleipAPI::class.java)

    }

    single {
        PreferenceManager.getDefaultSharedPreferences(androidContext())
    }

    single {
        SleipRepository(get(), get(), get())
    }

    single {
        Store(get(), get())
    }

    ChiliPhotoPicker.init(GlideImageLoader(),"se.mobileinteraction.sleip.fileprovider")

    single { Moshi.Builder().build() }
    viewModel { LoginViewModel(get()) }
    viewModel { MainActivityViewModel(androidContext(), get(),get()) }
    viewModel { SplashViewModel(get()) }
    viewModel { MainHorsesListViewModel(get(),get()) }
    viewModel { CreateHorseViewModel(get(), get()) }
    viewModel { AddingNewVideoRecordingViewModel(get()) }
    viewModel { (horse: Horse) -> DetailsPageViewModel(get(), horse) }
}
