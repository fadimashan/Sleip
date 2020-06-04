package se.mobileinteraction.sleip.ui.newHorse

import android.content.ClipDescription
import android.content.Context
import android.webkit.MimeTypeMap
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.google.android.exoplayer2.util.Log
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import okhttp3.MultipartBody
import okhttp3.RequestBody
import se.mobileinteraction.sleip.data.api.SleipAPI
import se.mobileinteraction.sleip.entities.Horse
import se.mobileinteraction.sleip.ui.progressBar.CountingRequestResult
import se.mobileinteraction.sleip.util.MyWorker
import se.mobileinteraction.sleip.util.ProgressRequestBody
import se.mobileinteraction.sleip.util.applyNetworkSchedulers
import java.io.File
import java.util.*

class CreateHorseViewModel(val context: Context,  val api: SleipAPI) :
    ViewModel() {

    private val _createHorseResponse: MutableLiveData<State> = MutableLiveData()
    val createHorseResponse: LiveData<State> = _createHorseResponse

    private val _deleteHorseResponse: MutableLiveData<Boolean> = MutableLiveData()
    val deleteHorseResponse: LiveData<Boolean> = _deleteHorseResponse

    private val request = OneTimeWorkRequestBuilder<MyWorker>().build()



        fun uploadImage(builder: MultipartBody.Builder) {
            api.createHorse(builder.build()).applyNetworkSchedulers()
                .subscribeBy(
                    onSuccess = {
                        _createHorseResponse.postValue(State(horse = it))
                        WorkManager.getInstance(context).enqueue(request)
                    },
                    onError = { _createHorseResponse.postValue(State(error = it)) }
                )
        }





//     fun uploadImage(name: RequestBody, date: RequestBody, description: RequestBody, image: MultipartBody.Part) {
//            api.createHorse(name,date,description,image).applyNetworkSchedulers()
//                .subscribeBy(
//                    onSuccess = {
//                        _createHorseResponse.postValue(State(horse = it))
//                        WorkManager.getInstance(context).enqueue(request)
//                    },
//                    onError = { _createHorseResponse.postValue(State(error = it)) }
//                )
//        }




    data class State(val horse: Horse? = null, val error: Throwable? = null)
}
