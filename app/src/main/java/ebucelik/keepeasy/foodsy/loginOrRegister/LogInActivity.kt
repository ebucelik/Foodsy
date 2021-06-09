package ebucelik.keepeasy.foodsy.loginOrRegister

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import ebucelik.keepeasy.foodsy.R
import ebucelik.keepeasy.foodsy.home.HomeActivity
import ebucelik.keepeasy.foodsy.viewmodels.LoginActivityViewModel

class LogInActivity : AppCompatActivity() {

    companion object{
        lateinit var loginActivityViewModel: LoginActivityViewModel
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_log_in)

        loginActivityViewModel = ViewModelProvider(this).get(LoginActivityViewModel::class.java)
    }

    fun openHomeActivity(){
        startActivity(Intent(this@LogInActivity, HomeActivity::class.java))
        finish()
    }
}