package ebucelik.keepeasy.foodsy

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import ebucelik.keepeasy.foodsy.databinding.FragmentLogInBinding

/**
 * A simple [Fragment] subclass.
 */
class LogInFragment(_logInActivity: LogInActivity) : Fragment(R.layout.fragment_log_in) {

    private lateinit var binding: FragmentLogInBinding

    private var logInActivity: LogInActivity = _logInActivity

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentLogInBinding.bind(view)

        binding.loginBtn.setOnClickListener {
            checkUsername()
            checkPassword()
        }

        binding.notRegistered.setOnClickListener {
            logInActivity.changeFragment(logInActivity.registrationFragment)
        }
    }

    private fun checkPassword() {
        binding.apply {
            if (binding.password.length() == 0){
                binding.passwordLayout.isHelperTextEnabled = true
                binding.passwordLayout.error = getString(R.string.enterPassword)
            }else{
                binding.passwordLayout.error = null
            }
        }
    }

    private fun checkUsername() {
        binding.apply {
            if (binding.username.length() == 0){
                binding.usernameLayout.isHelperTextEnabled = true
                binding.usernameLayout.error = getString(R.string.enterUsername)
            }else if(binding.username.text!!.contains(" ")){
                binding.usernameLayout.isHelperTextEnabled = true
                binding.usernameLayout.error = getString(R.string.usernameWithSpace)
            }else{
                binding.usernameLayout.error = null
            }
        }
    }
}