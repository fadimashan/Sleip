package se.mobileinteraction.sleip.data

import okhttp3.Interceptor
import okhttp3.Response
import se.mobileinteraction.sleip.data.SleipRepository.Companion.PREF_LOGIN_TOKEN
import se.mobileinteraction.sleip.entities.TokenResponse


class AuthorizationInterceptor(private val store: Store) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        var request = chain.request()
        val accessToken = store.readCache(PREF_LOGIN_TOKEN, TokenResponse::class)?.token

        request = request.newBuilder()
            .addHeader("Authorization", "Token $accessToken")
            .build()
        return chain.proceed(request)
    }

}
