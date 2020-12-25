package ebucelik.keepeasy.foodsy.meal

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
        val mealCategory = findViewById<TextView>(R.id.mealCategory)
        val mealArea = findViewById<TextView>(R.id.mealArea)
        val mealIngredients = findViewById<TextView>(R.id.mealIngredients)

        username.text = intent.getStringExtra(HomeActivity.USERNAME)
        mealName.text = intent.getStringExtra(HomeActivity.MEALNAME)
        mealCategory.text = intent.getStringExtra(HomeActivity.MEALCATEGORY)
        mealArea.text = intent.getStringExtra(HomeActivity.MEALAREA)
        mealIngredients.text = intent.getStringExtra(HomeActivity.INGREDIENTS)

        Picasso.get().load(intent.getStringExtra(HomeActivity.MEALIMAGE)).into(mealImage)
    }
}