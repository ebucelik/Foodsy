package ebucelik.keepeasy.foodsy.mealDetail

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import com.squareup.picasso.Picasso
import ebucelik.keepeasy.foodsy.R
import ebucelik.keepeasy.foodsy.home.HomeActivity

class MealDetailActivity() : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_meal_detail)

        val username = findViewById<TextView>(R.id.username)
        val mealName = findViewById<TextView>(R.id.mealName)
        val mealImage = findViewById<ImageView>(R.id.mealImage)

        username.text = intent.getStringExtra(HomeActivity.USERNAME)
        mealName.text = intent.getStringExtra(HomeActivity.MEALNAME)

        Picasso.get().load(intent.getStringExtra(HomeActivity.MEALIMAGE)).into(mealImage)
    }
}