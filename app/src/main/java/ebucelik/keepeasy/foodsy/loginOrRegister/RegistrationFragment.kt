package ebucelik.keepeasy.foodsy.loginOrRegister

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import ebucelik.keepeasy.foodsy.R
import ebucelik.keepeasy.foodsy.databinding.FragmentRegistrationBinding
import ebucelik.keepeasy.foodsy.home.HomeActivity

/**
 * A simple [Fragment] subclass.
 */
class RegistrationFragment(_logInActivity: LogInActivity) : Fragment(R.layout.fragment_registration) {

    private lateinit var binding: FragmentRegistrationBinding

    private var logInActivity: LogInActivity = _logInActivity

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentRegistrationBinding.bind(view)

        binding.registered.setOnClickListener {
            logInActivity.changeFragment(logInActivity.loginFragment)
        }

        binding.registerBtn.setOnClickListener {
            this.checkSurname()
            this.checkLastname()
            this.checkUsername()
            this.checkPassword()
        }

        binding.registerBtn.setOnClickListener {
            //TODO: Check input credentials
            logInActivity.openHomeActivity()
        }
    }

    private fun checkSurname(){
//TODO: Gabriel's Backend User Management
    }

    private fun checkLastname(){

    }

    private fun checkUsername(){

    }

    private fun checkPassword(){

    }
}
