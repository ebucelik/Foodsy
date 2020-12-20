package ebucelik.keepeasy.foodsy.loginOrRegister


import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import ebucelik.keepeasy.foodsy.R
import ebucelik.keepeasy.foodsy.databinding.FragmentLogInBinding
import ebucelik.keepeasy.foodsy.home.HomeActivity

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
            if(checkUsername() && checkPassword()){
                logInActivity.openHomeActivity()
            }
        }

        binding.notRegistered.setOnClickListener {
            logInActivity.changeFragment(logInActivity.registrationFragment)
        }
    }

    private fun checkPassword():Boolean {
        binding.apply {
            if (binding.password.length() == 0){
                binding.passwordLayout.isHelperTextEnabled = true
                binding.passwordLayout.error = getString(R.string.enterPassword)
                return false
            }else{
                binding.passwordLayout.error = null
                return true
            }
        }
    }

    private fun checkUsername():Boolean {
        binding.apply {
            if (binding.username.length() == 0){
                binding.usernameLayout.isHelperTextEnabled = true
                binding.usernameLayout.error = getString(R.string.enterUsername)
                return false
            }else if(binding.username.text!!.contains(" ")){
                binding.usernameLayout.isHelperTextEnabled = true
                binding.usernameLayout.error = getString(R.string.usernameWithSpace)
                return false
            }else{
                binding.usernameLayout.error = null
                return true
            }
        }
    }
}