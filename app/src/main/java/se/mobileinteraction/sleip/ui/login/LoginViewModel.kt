package se.mobileinteraction.sleip.ui.login

import android.annotation.SuppressLint
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.rxkotlin.subscribeBy
import se.mobileinteraction.sleip.data.SleipRepository
import se.mobileinteraction.sleip.entities.State
import timber.log.Timber

class LoginViewModel(private val loginRepository: SleipRepository) : ViewModel() {

    private val _stateEmitter: MutableLiveData<State> = MutableLiveData()
    val stateEmitter: LiveData<State> = _stateEmitter
    val currentToken = loginRepository.readTokenCache()

    @SuppressLint("CheckResult")
    fun login(username: String, password: String) {
        loginRepository
            .login(username, password)
            .subscribeBy(
                onSuccess = {
                    _stateEmitter.postValue(State(it, null))

                }, onError = {
                    Timber.e(it)
                    _stateEmitter.postValue(State(null, Exception(it)))
                }
            )
    }
}