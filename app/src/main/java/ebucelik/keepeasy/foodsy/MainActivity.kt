package ebucelik.keepeasy.foodsy

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        showLogInScreen()
    }

    private fun showLogInScreen() {
        Handler().postDelayed({
            val intent = Intent(this@MainActivity, LogInActivity::class.java)
            startActivity(intent)
            finish() //If user go back, the app closes.
        }, 3000)
    }
}