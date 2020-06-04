package se.mobileinteraction.sleip

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import kotlinx.android.synthetic.main.activity_main.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import se.mobileinteraction.sleip.data.Store
import se.mobileinteraction.sleip.entities.TokenResponse

class MainActivity : AppCompatActivity(R.layout.activity_main) {

    private val viewModel by viewModel<MainActivityViewModel>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val navController = findNavController(R.id.nav_host_fragment)
        observedData()

        navController.addOnDestinationChangedListener { _, destination, _ ->
            viewModel.showToolbar(destination.id)
        }


//        sign_out_btn.setOnClickListener{
//            viewModel.repo.remove()
//            findNavController(R.id.nav_host_fragment).navigate(R.id.loginFragment)
//        }



    }

    private fun observedData(){
//        viewModel.showToolbar.observe(this, Observer {
//            toolbar.isVisible = it
//        })
    }
}
