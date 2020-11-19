package ebucelik.keepeasy.foodsy

import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

class LogInActivity : AppCompatActivity() {

    //lateinit promises that this variable will be initialized before calling any operations on it.
    lateinit var passwordTxt: TextInputEditText
    lateinit var passwordLayout: TextInputLayout
    lateinit var usernameTxt: TextInputEditText
    lateinit var usernameLayout: TextInputLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_log_in)

        val loginBtn: MaterialButton = findViewById(R.id.loginBtn)

        loginBtn.setOnClickListener {
            checkUsername()
            checkPassword()
        }

        passwordTxt = findViewById(R.id.password)
        passwordLayout = findViewById(R.id.passwordLayout)
        usernameTxt = findViewById(R.id.username)
        usernameLayout = findViewById(R.id.usernameLayout)
    }

    private fun checkPassword() {
        if (passwordTxt.length() == 0){
            passwordLayout.isHelperTextEnabled = true
            passwordLayout.error = getString(R.string.enterPassword)
        }else{
            passwordLayout.error = null
        }
    }

    private fun checkUsername() {
        if (usernameTxt.length() == 0){
            usernameLayout.isHelperTextEnabled = true
            usernameLayout.error = getString(R.string.enterUsername)
        }else if(usernameTxt.text!!.contains(" ")){
            usernameLayout.isHelperTextEnabled = true
            usernameLayout.error = getString(R.string.usernameWithSpace)
        }else{
            usernameLayout.error = null
        }
    }
}