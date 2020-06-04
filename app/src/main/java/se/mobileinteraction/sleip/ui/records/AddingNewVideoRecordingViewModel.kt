package se.mobileinteraction.sleip.ui.records

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.rxkotlin.subscribeBy
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import se.mobileinteraction.sleip.data.api.SleipAPI
import se.mobileinteraction.sleip.entities.RecordingResponse
import se.mobileinteraction.sleip.util.applyNetworkSchedulers
import java.io.File

class AddingNewVideoRecordingViewModel(val api: SleipAPI) : ViewModel() {


    private val _createRecordResponse: MutableLiveData<State> =
        MutableLiveData()
    val createRecordResponse: LiveData<State> = _createRecordResponse


    fun uploadRecord(
        horseId: Int,
        comment: String?,
        name: String?,
        video: File?
    ) {

        val builder = MultipartBody.Builder().setType(MultipartBody.FORM).apply {
            addFormDataPart("horse", horseId.toString())
            comment?.let { addFormDataPart("comment", it) }
            name?.let { addFormDataPart("name", it) }
            video?.let {
                addFormDataPart(
                    "video", video.name,
                    it.asRequestBody(MultipartBody.FORM)
                )
            }
        }

        api.createRec(builder.build()).applyNetworkSchedulers().subscribeBy(
            onSuccess = {
                     _createRecordResponse.postValue(State(complete = it))

//                if (it.analysis?.id != null) {
//                    _createRecordResponse.postValue(State(complete = it))
//                } else if (it != null)
//                    _createRecordResponse.postValue(State(pending = it))

                },
            onError = {
                _createRecordResponse.postValue(State(error = it))


            }
        )

    }

    data class State(
    val complete: RecordingResponse? = null,
//    val pending: RecordingResponse? = null,
    val error: Throwable? = null
)
}


