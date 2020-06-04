package se.mobileinteraction.sleip.ui.mainFragment

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.rxkotlin.subscribeBy
import se.mobileinteraction.sleip.data.SleipRepository
import se.mobileinteraction.sleip.entities.Horse
import timber.log.Timber

class MainHorsesListViewModel(val repo: SleipRepository) : ViewModel() {

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
}
