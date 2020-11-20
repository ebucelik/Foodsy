package ebucelik.keepeasy.foodsy

import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import ebucelik.keepeasy.foodsy.databinding.ActivityLogInBinding

class LogInActivity : AppCompatActivity() {

    //lateinit promises that this variable will be initialized before calling any operations on it.
    //lateinit var passwordTxt: TextInputEditText

    //Data Type is the activity name + Binding
    private lateinit var binding: ActivityLogInBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_log_in)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_log_in)

        binding.loginBtn.setOnClickListener {
            checkUsername()
            checkPassword()
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

            invalidateAll() //refresh UI
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

            invalidateAll()
        }
    }
}