package ebucelik.keepeasy.foodsy

import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

class LogInActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_log_in)

        val username: TextInputEditText = findViewById(R.id.username)
        val password: TextInputEditText = findViewById(R.id.password)
        val loginBtn: MaterialButton = findViewById(R.id.loginBtn)

        loginBtn.setOnClickListener {
            checkUsername()
            checkPassword()
        }
    }

    private fun checkPassword() {
        val passwordTxt: TextInputEditText = findViewById(R.id.password)
        val passwordLayout: TextInputLayout = findViewById(R.id.passwordLayout)

        if (passwordTxt.length() == 0){
            passwordLayout.isHelperTextEnabled = true
            passwordLayout.error = getString(R.string.enterPassword)
        }else{
            passwordLayout.error = null
            System.out.println("Hashed password: " + passwordTxt.text!!.hashCode())
        }
    }

    private fun checkUsername() {
        val usernametxt: TextInputEditText = findViewById(R.id.username)
        val usernameLayout: TextInputLayout = findViewById(R.id.usernameLayout)

        if (usernametxt.length() == 0){
            usernameLayout.isHelperTextEnabled = true
            usernameLayout.error = getString(R.string.enterUsername)
        }else if(usernametxt.text!!.contains(" ")){
            usernameLayout.isHelperTextEnabled = true
            usernameLayout.error = getString(R.string.usernameWithSpace)
        }else{
            usernameLayout.error = null
        }
    }
}