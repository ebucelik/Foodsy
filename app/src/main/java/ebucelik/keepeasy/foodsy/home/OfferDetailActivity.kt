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
import androidx.databinding.DataBindingUtil
import com.google.gson.GsonBuilder
import com.squareup.picasso.Picasso
import ebucelik.keepeasy.foodsy.MainActivity
import ebucelik.keepeasy.foodsy.R
import ebucelik.keepeasy.foodsy.account.UserActivity
import ebucelik.keepeasy.foodsy.databinding.ActivityMealDetailBinding
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

    private lateinit var orderingUserUuid: String
    private lateinit var offer: Offer
    private lateinit var binding: ActivityMealDetailBinding

    companion object{
        const val USER = "user"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_meal_detail)

        orderingUserUuid = intent.getStringExtra(HomeActivity.ORDERINGUUID).toString()
        offer = intent.getSerializableExtra(HomeActivity.OFFER) as Offer

        binding.meal = offer

        val simpleDateFormat = SimpleDateFormat("dd.MM.yyyy HH:mm")
        binding.offeredDate.text = simpleDateFormat.format(offer.currentTimestamp)

        if(offer.encodedImage != ""){
            binding.mealImage.setImageBitmap(decodeImage(offer.encodedImage))
        }

        if(offer.user?.profileImage != ""){
            binding.profileImage.setImageBitmap(offer.user?.let { decodeImage(it.profileImage) })
        }

        binding.orderBtn.setOnClickListener {
            orderMeal(offer.id)
        }

        binding.profileImage.setOnClickListener {
            offer.user?.let { it1 -> openUserProfile(it1) }
        }

        binding.price.text = "${offer.price} €"
    }

    private fun orderMeal(offeringId:Long){
        val url = "${MainActivity.IP}/ordering"

        val jsonObject = JSONObject()
        try{
            jsonObject.put("userUUID", orderingUserUuid)
                    .put("offerId", offeringId)
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