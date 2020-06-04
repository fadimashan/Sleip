package se.mobileinteraction.sleip.ui.details

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.rxkotlin.subscribeBy
import se.mobileinteraction.sleip.data.SleipRepository
import se.mobileinteraction.sleip.entities.GetHorse
import se.mobileinteraction.sleip.entities.Horse
import se.mobileinteraction.sleip.entities.Recording
import timber.log.Timber


class DetailsPageViewModel(val repo: SleipRepository, horse: Horse) : ViewModel() {

    private val _horseProfile: MutableLiveData<Horse> = MutableLiveData()
    val horseProfile: LiveData<Horse> = _horseProfile

     private val _horseRec: MutableLiveData<List<Recording>> = MutableLiveData()
    var horseRec: MutableLiveData<List<Recording>> = _horseRec

    fun getHorse(horseId: Int) {
        repo.getHorseProfile(horseId)
            .subscribeBy(
                onSuccess = { _horseProfile.postValue(it) },
                onError = { Timber.e(it) }
            )
    }

     fun getRecList(horseId: Int) {
        repo.getRecordsList(horseId)
            .subscribeBy(
                onSuccess = { _horseRec.postValue(it) },
                onError = { Timber.e(it) }
            )
    }


}
