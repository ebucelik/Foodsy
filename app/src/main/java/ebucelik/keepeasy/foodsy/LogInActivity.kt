package ebucelik.keepeasy.foodsy

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import ebucelik.keepeasy.foodsy.databinding.ActivityLogInBinding

class LogInActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = DataBindingUtil.setContentView<ActivityLogInBinding>(this, R.layout.activity_log_in)
    }

}