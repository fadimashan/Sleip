package se.mobileinteraction.sleip


import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.iid.FirebaseInstanceId
import org.koin.androidx.viewmodel.ext.android.viewModel
import se.mobileinteraction.sleip.entities.RegistrationID
import java.io.File

class MainActivity : AppCompatActivity(R.layout.activity_main) {

    private val viewModel by viewModel<MainActivityViewModel>()

    var tokenCached: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        val navController = findNavController(R.id.nav_host_fragment)
        observedData()

        FirebaseInstanceId.getInstance().instanceId
            .addOnCompleteListener(OnCompleteListener { task ->
                if (!task.isSuccessful) {
                    Log.w("TAG", "getInstanceId failed", task.exception)
                    return@OnCompleteListener
                }

                val token = task.result?.token
                Log.d("TAG", token)
                tokenCached = token
                token?.let { viewModel.cacheNewDevice(it) }
            })



        navController.addOnDestinationChangedListener { _, destination, _ ->
            viewModel.showToolbar(destination.id)
        }

    }

    private fun observedData() {

    }

//    override fun onSharedPreferenceChanged(key: String, sharedPreferences: SharedPreferences) {
//        when (key) {
//            "pref_device_token" ->
//               viewModel.registerDevice()
//        }
//
//    }


    companion object {

        fun getOutputDirectory(context: Context): File {
            val appContext = context.applicationContext

            val mediaDir = context.externalMediaDirs
                .firstOrNull()?.let {
                    File(it, appContext.resources.getString(R.string.app_name)).apply { mkdirs() }
                }
            return if (mediaDir != null && mediaDir.exists())
                mediaDir else appContext.filesDir
        }
    }
}
