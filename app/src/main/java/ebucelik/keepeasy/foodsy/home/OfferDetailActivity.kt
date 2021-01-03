package ebucelik.keepeasy.foodsy.home

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Base64
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.google.gson.GsonBuilder
import com.squareup.picasso.Picasso
import ebucelik.keepeasy.foodsy.MainActivity
import ebucelik.keepeasy.foodsy.R
import ebucelik.keepeasy.foodsy.account.UserActivity
import ebucelik.keepeasy.foodsy.entitiy.Offer
import ebucelik.keepeasy.foodsy.entitiy.User
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException
import java.text.SimpleDateFormat

class OfferDetailActivity : AppCompatActivity() {

    private lateinit var offeredUserUuid: String
    private lateinit var orderingUserUuid: String

    companion object{
        const val USER = "user"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_meal_detail)

        val username = findViewById<TextView>(R.id.username)
        val mealName = findViewById<TextView>(R.id.mealName)
        val mealImage = findViewById<ImageView>(R.id.mealImage)
        val mealCategory = findViewById<TextView>(R.id.mealCategory)
        val mealArea = findViewById<TextView>(R.id.mealArea)
        val mealIngredients = findViewById<TextView>(R.id.mealIngredients)
        val orderMeal = findViewById<Button>(R.id.orderBtn)
        val offeredDate = findViewById<TextView>(R.id.offeredDate)
        val profileImage = findViewById<ImageView>(R.id.profileImage)

        orderingUserUuid = intent.getStringExtra(HomeActivity.ORDERINGUUID).toString()
        val offer = intent.getSerializableExtra(HomeActivity.OFFER) as Offer
        val offeringId = offer.id
        offeredUserUuid = offer.userUUID
        mealName.text = offer.mealName
        mealCategory.text = offer.category
        mealArea.text = offer.area
        mealIngredients.text = offer.ingredients

        val simpleDateFormat = SimpleDateFormat("dd.MM.yyyy HH:mm")
        offeredDate.text = simpleDateFormat.format(offer.currentTimestamp)
        username.text = offer.user.getUsername()

        if(offer.encodedImage != ""){
            mealImage.setImageBitmap(decodeImage(offer.encodedImage))
        }

        if(offer.user.getProfileImage() != ""){
            profileImage.setImageBitmap(decodeImage(offer.user.getProfileImage()))
        }

        orderMeal.setOnClickListener {
            orderMeal(offeringId)
        }

        profileImage.setOnClickListener {
            openUserProfile(offer.user)
        }
    }

    private fun orderMeal(offeringId:Long){
        val url = "http://${MainActivity.IP}:8080/ordering"

        val jsonObject = JSONObject()
        try{
            jsonObject.put("orderingUuid", orderingUserUuid)
                    .put("offeringUuid", offeredUserUuid)
                    .put("offeringId", offeringId)
        }catch (e: JSONException){
            e.printStackTrace()
        }

        val mediaType = "application/json; charset=utf-8".toMediaType()
        val body = jsonObject.toString().toRequestBody(mediaType)

        val request = Request.Builder()
                .url(url)
                .post(body)
                .build()

        val client = OkHttpClient()
        client.newCall(request).enqueue(object : Callback{
            override fun onResponse(call: Call, response: Response) {
                if(response.code == 201){
                    this@OfferDetailActivity.runOnUiThread {
                        Toast.makeText(baseContext, "Meal is successfully ordered.", Toast.LENGTH_SHORT).show()
                        finish()
                    }
                }

                response.close()
            }

            override fun onFailure(call: Call, e: IOException) {
                e.printStackTrace()
            }
        })
    }

    private fun decodeImage(encodedImage: String): Bitmap {
        val imageBytes = Base64.decode(encodedImage, Base64.DEFAULT)
        return BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
    }

    private fun openUserProfile(user: User){
        val intent = Intent(this, UserActivity::class.java)
        intent.putExtra(USER, user)
        startActivity(intent)
    }
}