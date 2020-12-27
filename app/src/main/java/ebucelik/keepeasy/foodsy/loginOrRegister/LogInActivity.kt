package ebucelik.keepeasy.foodsy.loginOrRegister

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import ebucelik.keepeasy.foodsy.R
import ebucelik.keepeasy.foodsy.home.HomeActivity

class LogInActivity : AppCompatActivity() {

    lateinit var loginFragment: LogInFragment
    lateinit var registrationFragment: RegistrationFragment

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

    fun openHomeActivity(){
        startActivity(Intent(this@LogInActivity, HomeActivity::class.java))
        finish()
    }
}