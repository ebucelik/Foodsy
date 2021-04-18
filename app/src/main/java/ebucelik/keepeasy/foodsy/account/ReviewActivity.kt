package ebucelik.keepeasy.foodsy.account

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Base64
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import ebucelik.keepeasy.foodsy.MainActivity
import ebucelik.keepeasy.foodsy.R
import ebucelik.keepeasy.foodsy.entitiy.Offer
import ebucelik.keepeasy.foodsy.entitiy.Order
import ebucelik.keepeasy.foodsy.home.HomeActivity
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.ArrayList

class ReviewActivity : AppCompatActivity() {
    private lateinit var rates: ArrayList<ImageView>
    private lateinit var offeredUserUuid: String
    private lateinit var order: Order
    private var reviewedPoints: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_review)

        rates = arrayListOf(
                findViewById(R.id.rate1),
                findViewById(R.id.rate2),
                findViewById(R.id.rate3),
                findViewById(R.id.rate4),
                findViewById(R.id.rate5)
        )

        val username = findViewById<TextView>(R.id.username)
        val mealName = findViewById<TextView>(R.id.mealName)
        val mealImage = findViewById<ImageView>(R.id.mealImage)
        val mealCategory = findViewById<TextView>(R.id.mealCategory)
        val mealArea = findViewById<TextView>(R.id.mealArea)
        val mealIngredients = findViewById<TextView>(R.id.mealIngredients)
        val reviewMeal = findViewById<Button>(R.id.rateBtn)
        val offeredDate = findViewById<TextView>(R.id.offeredDate)
        val profileImage = findViewById<ImageView>(R.id.profileImage)

        order = intent.getSerializableExtra(HomeActivity.ORDER) as Order
        offeredUserUuid = order.offer.user?.userUUID ?: ""
        mealName.text = order.offer.mealName
        mealCategory.text = order.offer.category
        mealArea.text = order.offer.area
        mealIngredients.text = order.offer.ingredients

        val simpleDateFormat = SimpleDateFormat("dd.MM.yyyy HH:mm")
        offeredDate.text = simpleDateFormat.format(order.offer.currentTimestamp)
        username.text = order.offer.user?.username

        if(order.offer.encodedImage != ""){
            mealImage.setImageBitmap(decodeImage(order.offer.encodedImage))
        }

        if(order.offer.user?.profileImage != ""){
            profileImage.setImageBitmap(order.offer.user?.let { decodeImage(it.profileImage) })
        }

        reviewMeal.setOnClickListener {
            sendReview()
        }

        rates[0].setOnClickListener {
            setStarReviews(5, R.drawable.ic_round_star_rate_24)
            setStarReviews(1, R.drawable.ic_round_star_rate_24_gold)
        }
        rates[1].setOnClickListener {
            setStarReviews(5, R.drawable.ic_round_star_rate_24)
            setStarReviews(2, R.drawable.ic_round_star_rate_24_gold)
        }
        rates[2].setOnClickListener {
            setStarReviews(5, R.drawable.ic_round_star_rate_24)
            setStarReviews(3, R.drawable.ic_round_star_rate_24_gold)
        }
        rates[3].setOnClickListener {
            setStarReviews(5, R.drawable.ic_round_star_rate_24)
            setStarReviews(4, R.drawable.ic_round_star_rate_24_gold)
        }
        rates[4].setOnClickListener {
            setStarReviews(5, R.drawable.ic_round_star_rate_24)
            setStarReviews(5, R.drawable.ic_round_star_rate_24_gold)
        }
    }

    private fun setStarReviews(reviewInt:Int, resId: Int){
        for (i in 0 until reviewInt){
            rates[i].setImageResource(resId)
        }

        reviewedPoints = reviewInt
    }

    private fun sendReview(){
        val url = "${MainActivity.IP}/review"

        val jsonObject = JSONObject()
        try{
            jsonObject
                    .put("reviewPoints", reviewedPoints)
                    .put("orderId", order.id)
                    .put("reviewText", "")
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
                this@ReviewActivity.runOnUiThread {
                    if(response.code == 201){
                        Toast.makeText(baseContext, "Your rating is successfully sent.", Toast.LENGTH_SHORT).show()
                        finish()
                    }else{
                        Toast.makeText(baseContext, "Server error.", Toast.LENGTH_SHORT).show()
                    }
                }

                response.close()
            }

            override fun onFailure(call: Call, e: IOException) {}
        })
    }

    private fun decodeImage(encodedImage: String): Bitmap {
        val imageBytes = Base64.decode(encodedImage, Base64.DEFAULT)
        return BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
    }
}