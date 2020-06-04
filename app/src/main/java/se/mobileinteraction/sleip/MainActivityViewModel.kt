package se.mobileinteraction.sleip

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.rxkotlin.subscribeBy
import se.mobileinteraction.sleip.data.SleipRepository
import se.mobileinteraction.sleip.data.api.SleipAPI
import se.mobileinteraction.sleip.entities.Device
import se.mobileinteraction.sleip.entities.RegistrationID
import se.mobileinteraction.sleip.util.applyNetworkSchedulers
import timber.log.Timber


class MainActivityViewModel(
    val context: Context, private val repo: SleipRepository,val api: SleipAPI
) : ViewModel() {

    private val _showToolbar: MutableLiveData<Boolean> = MutableLiveData()

    private val showToolbarList = listOf(R.id.loginFragment, R.id.splashFragment)

    fun showToolbar(currentId: Int) {
        _showToolbar.postValue(!showToolbarList.contains(currentId))
    }


    fun cacheNewDevice(deviceToken: String){
        repo.cacheDeviceToken(RegistrationID(deviceToken)).apply {
            api.createDevice(Device(repo.readDeviceToken()!!.registration_id)).applyNetworkSchedulers()
                    .subscribeBy (
            onSuccess = { Log.i("TAG", it.active.toString())},
            onError = { Timber.i("Error with add new device") }
            )
        }
    }

//     fun cacheNewDevice(deviceToken: String) {
//         repo.cacheDeviceToken(RegistrationID(deviceToken))
//     }

    fun registerDevice(){
         api.createDevice(Device(repo.readDeviceToken()!!.registration_id)).applyNetworkSchedulers()
                    .subscribeBy (
            onSuccess = { Log.i("TAG", it.active.toString())},
            onError = { Timber.i("Error with add new device") }
            )
        }

}
