package se.mobileinteraction.sleip.ui.login

import androidx.lifecycle.Observer
import androidx.fragment.app.Fragment
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import kotlinx.android.synthetic.main.fragment_login.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import se.mobileinteraction.sleip.R
import se.mobileinteraction.sleip.data.Store


class LoginFragment : Fragment(R.layout.fragment_login) {

    private val viewModel by viewModel<LoginViewModel>()

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        observeData()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        /** Login process **/
        login.setOnClickListener {
            val userName = username.text.toString().trim()
            val password1 = password.text.toString().trim()

            if (userName.isEmpty()){
                username.error = "Email required"
                username.requestFocus()
                return@setOnClickListener
            }

            if (password1.isEmpty()){
                password.error = "Password required"
                password.requestFocus()
                password.setText("")
                return@setOnClickListener
            }

            viewModel.login(userName, password1)
        }
    }

    private fun observeData() {
        viewModel.stateEmitter.observe(viewLifecycleOwner, Observer {
           if (it.defaultResponse?.token != null) {
//               if (viewModel.currentToken != null ){
//               viewModel.registerNewDevice(viewModel.currentToken!!.registration_id)}
               findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToNavHostFragment())
           }else{
               Toast.makeText(requireContext(), "Email or Password incorrect!", Toast.LENGTH_SHORT).show()
           }
        })
    }
}
