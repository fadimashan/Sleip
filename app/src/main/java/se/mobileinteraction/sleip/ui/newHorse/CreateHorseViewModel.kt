package se.mobileinteraction.sleip.ui.newHorse

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.rxkotlin.subscribeBy
import okhttp3.MultipartBody
import se.mobileinteraction.sleip.data.SleipRepository
import se.mobileinteraction.sleip.data.api.SleipAPI
import se.mobileinteraction.sleip.entities.CreateHorseBody
import se.mobileinteraction.sleip.entities.Horse
import se.mobileinteraction.sleip.entities.UploadResponse
import se.mobileinteraction.sleip.util.applyNetworkSchedulers
import timber.log.Timber

class CreateHorseViewModel(val repo: SleipRepository, val api: SleipAPI) :
    ViewModel() {

    private val _createHorseResponse: MutableLiveData<State> = MutableLiveData()
    val createHorseResponse: LiveData<State> = _createHorseResponse

    private val _createHorseImageResponse: MutableLiveData<UploadResponse> = MutableLiveData()
    val createHorseImageResponse: LiveData<UploadResponse> = _createHorseImageResponse

    fun createHorse(
        name: String,
        date: String?,
        description: String?,
        horsePhoto: MultipartBody.Part?
    ) {
        api.createHorse(CreateHorseBody(name, date, description, null))
            .applyNetworkSchedulers()
            .subscribeBy(
                onSuccess = {
                    _createHorseResponse.postValue(State(horse = it))
                },
                onError = { _createHorseResponse.postValue(State(error = it)) }
            )
    }

    fun uploadImage(img: MultipartBody.Part) {
        api.uploadImage(img)?.applyNetworkSchedulers()?.subscribeBy(
            onSuccess = {_createHorseImageResponse.postValue(it)},
            onError = { Timber.e(it)}
        )
    }

    data class State(val horse: Horse? = null, val error: Throwable? = null)
}
