package se.mobileinteraction.sleip.ui.mainFragment

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.rxkotlin.subscribeBy
import se.mobileinteraction.sleip.data.SleipRepository
import se.mobileinteraction.sleip.data.Store
import se.mobileinteraction.sleip.data.api.SleipAPI
import se.mobileinteraction.sleip.entities.Device
import se.mobileinteraction.sleip.entities.Horse
import se.mobileinteraction.sleip.util.applyNetworkSchedulers
import timber.log.Timber

class MainHorsesListViewModel(val repo: SleipRepository, val api: SleipAPI) : ViewModel() {

    private val _horseData: MutableLiveData<List<Horse>> = MutableLiveData()
    var horseData: MutableLiveData<List<Horse>> = _horseData

    init {
        getHorsesList()

    }

    fun getHorsesList() {
        repo.getHorseList()
            .subscribeBy(
                onSuccess = { _horseData.postValue(it) },
                onError = { Timber.e(it) }
            )
    }

     fun deleteHorse(horseId: Int){
        api.deleteHorse(horseId).applyNetworkSchedulers().subscribeBy(
            onError = {
                Log.e("DELETE", "Error")
            }
        )
    }
}
