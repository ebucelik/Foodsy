package ebucelik.keepeasy.foodsy.home

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import com.google.gson.GsonBuilder
import com.squareup.picasso.Picasso
import ebucelik.keepeasy.foodsy.R
import ebucelik.keepeasy.foodsy.entitiy.User
import okhttp3.*
import java.io.IOException

class OfferDetailActivity() : AppCompatActivity() {

    private lateinit var username: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_meal_detail)

        username = findViewById(R.id.username)
        val mealName = findViewById<TextView>(R.id.mealName)
        val mealImage = findViewById<ImageView>(R.id.mealImage)
        val mealCategory = findViewById<TextView>(R.id.mealCategory)
        val mealArea = findViewById<TextView>(R.id.mealArea)
        val mealIngredients = findViewById<TextView>(R.id.mealIngredients)

        mealName.text = intent.getStringExtra(HomeActivity.MEALNAME)
        mealCategory.text = intent.getStringExtra(HomeActivity.MEALCATEGORY)
        mealArea.text = intent.getStringExtra(HomeActivity.MEALAREA)
        mealIngredients.text = intent.getStringExtra(HomeActivity.INGREDIENTS)

        getUsername()

        if(intent.getStringExtra(HomeActivity.MEALIMAGE) != ""){
            Picasso.get().load(intent.getStringExtra(HomeActivity.MEALIMAGE)).into(mealImage)
        }
    }

    private fun getUsername(){
        val url = "http://10.0.2.2:8080/user?userUUID=${readUUID()}"

        val request = Request.Builder()
                .url(url)
                .get()
                .build()

        val client = OkHttpClient()
        client.newCall(request).enqueue(object: Callback{
            override fun onResponse(call: Call, response: Response) {
                val body = response.body?.string()

                val gson = GsonBuilder().create()
                val user = gson.fromJson(body, User::class.java)

                username.text = user.getUsername()
            }

            override fun onFailure(call: Call, e: IOException) {}
        })
    }

    private fun readUUID(): String {
        val sharedPref = this.getSharedPreferences("uuid", Context.MODE_PRIVATE)
        return sharedPref.getString(R.string.uuid.toString(), null) as String
    }
}