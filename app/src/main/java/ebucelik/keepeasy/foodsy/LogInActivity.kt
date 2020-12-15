package ebucelik.keepeasy.foodsy

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import ebucelik.keepeasy.foodsy.databinding.ActivityLogInBinding

class LogInActivity : AppCompatActivity() {

    lateinit var loginFragment:LogInFragment
    lateinit var registrationFragment:RegistrationFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_log_in)

        loginFragment = LogInFragment(this)
        registrationFragment = RegistrationFragment(this)

        supportFragmentManager.beginTransaction().apply {
            replace(R.id.flLoginActivity, loginFragment)
            commit()
        }
    }

    fun changeFragment(frag: Fragment){
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.flLoginActivity, frag)
            commit()
        }
    }
}