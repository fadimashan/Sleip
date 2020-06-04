package se.mobileinteraction.sleip

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import se.mobileinteraction.sleip.data.SleipRepository


class MainActivityViewModel(
    val context: Context, val repo: SleipRepository
) : ViewModel() {

    private val _showToolbar: MutableLiveData<Boolean> = MutableLiveData()
    val showToolbar: LiveData<Boolean> = _showToolbar
    private val showToolbarList = listOf(R.id.loginFragment, R.id.splashFragment)

    fun showToolbar(currentId: Int) {
        _showToolbar.postValue(!showToolbarList.contains(currentId))
    }
}
