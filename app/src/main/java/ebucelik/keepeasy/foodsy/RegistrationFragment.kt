package ebucelik.keepeasy.foodsy

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import ebucelik.keepeasy.foodsy.databinding.FragmentRegistrationBinding

/**
 * A simple [Fragment] subclass.
 */
class RegistrationFragment(_logInActivity: LogInActivity) : Fragment(R.layout.fragment_registration) {

    private lateinit var binding: FragmentRegistrationBinding

    private var logInActivity: LogInActivity = _logInActivity

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentRegistrationBinding.bind(view)

        binding.blankFrag.setOnClickListener {
            logInActivity.changeFragment(logInActivity.loginFragment)
        }
    }
}