package ebucelik.keepeasy.foodsy.account

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Base64
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayout
import ebucelik.keepeasy.foodsy.MainActivity
import ebucelik.keepeasy.foodsy.R
import ebucelik.keepeasy.foodsy.entitiy.User
import ebucelik.keepeasy.foodsy.home.HomeActivity
import ebucelik.keepeasy.foodsy.home.OfferDetailActivity
import okhttp3.*
import java.io.IOException
import java.util.ArrayList

class UserActivity : AppCompatActivity() {

    private lateinit var offerFragment: OfferFragment
    private lateinit var rates: ArrayList<ImageView>
    private lateinit var user: User
    private lateinit var firstnameView: TextView
    private lateinit var lastnameView: TextView
    private lateinit var usernameView: TextView
    private lateinit var profileImage: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user)

        user = intent.getSerializableExtra(OfferDetailActivity.USER) as User

        firstnameView = findViewById(R.id.firstname)
        lastnameView = findViewById(R.id.lastname)
        usernameView = findViewById(R.id.username)
        profileImage = findViewById(R.id.profileImage)
        offerFragment = OfferFragment(user.userUUID)

        rates = arrayListOf(
                findViewById(R.id.rate1),
                findViewById(R.id.rate2),
                findViewById(R.id.rate3),
                findViewById(R.id.rate4),
                findViewById(R.id.rate5)
        )

        firstnameView.text = user.firstname
        lastnameView.text = user.surname
        usernameView.text = user.username

        if (user.profileImage != null){
            profileImage.setImageBitmap(decodeImage(user.profileImage))
        }

        getStarReview()

        changeFragment(offerFragment)
    }

    private fun changeFragment(fragment: Fragment){
        this.supportFragmentManager.beginTransaction().apply {
            replace(R.id.fLAccountFragment, fragment)
            commit()
        }
    }

    private fun getStarReview(){
        val url = "http://${MainActivity.IP}:8080/reviewAverage?uuid=${user.userUUID}"

        val request = Request.Builder()
                .url(url)
                .get()
                .build()

        val client = OkHttpClient()
        client.newCall(request).enqueue(object: Callback {
            override fun onResponse(call: Call, response: Response) {
                val body = response.body?.string()

                if(response.code == 200){
                    if (body != null) {
                        setStarReviews(body.toInt())
                    }
                }

                response.close()
            }

            override fun onFailure(call: Call, e: IOException) {}
        })
    }

    private fun setStarReviews(reviewInt:Int){
        if(reviewInt >= 1){
            for (i in 0 until reviewInt){
                rates[i].setImageResource(R.drawable.ic_round_star_rate_24_gold)
            }
        }
    }

    private fun decodeImage(encodedImage: String): Bitmap {
        val imageBytes = Base64.decode(encodedImage, Base64.DEFAULT)
        return BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
    }
}