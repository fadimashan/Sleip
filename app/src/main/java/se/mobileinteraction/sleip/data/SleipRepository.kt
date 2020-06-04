package se.mobileinteraction.sleip.data

import android.content.Context
import io.reactivex.Single
import se.mobileinteraction.sleip.data.api.SleipAPI
import se.mobileinteraction.sleip.entities.*
import se.mobileinteraction.sleip.util.applyNetworkSchedulers

class SleipRepository(
    val application: Context,
    private val api: SleipAPI,
    private val store: Store
) {


    fun login(userName: String, password: String) =
        api.userLogin(LoginData(userName, password))
            .doOnSuccess { /* Cache token for later use */
                cacheToken(it)
            }
            .applyNetworkSchedulers()

    fun getHorseList(): Single<List<Horse>> {
        return api.getHorseList().applyNetworkSchedulers()
    }

    fun getHorseProfile(id:Int): Single<Horse>{
        return api.getHorse(id).applyNetworkSchedulers()
    }

    fun getRecordsList(id: Int):Single<List<RecordingResponse>>{
        return api.recordingList(id).applyNetworkSchedulers()
    }


       fun createDevice(device_token: String) {
        cacheDeviceToken(RegistrationID(device_token))
    }
    fun cacheDeviceToken(deviceToken: RegistrationID) {
        store.store(PREF_DEVICE_TOKEN, deviceToken, RegistrationID::class)
    }

    private fun cacheToken(token: TokenResponse) {
        store.store(PREF_LOGIN_TOKEN, token, TokenResponse::class)
    }

    fun readTokenCache(): TokenResponse? {
        return store.readCache(PREF_LOGIN_TOKEN, TokenResponse::class)
    }

    fun readDeviceToken(): RegistrationID?{
        return  store.readCache(PREF_DEVICE_TOKEN,RegistrationID::class)
    }

    fun remove() {
        store.remove(PREF_LOGIN_TOKEN)
    }

    companion object {
        const val PREF_LOGIN_TOKEN = "pref_login_token"
        const val PREF_DEVICE_TOKEN = "pref_device_token"
    }
}
