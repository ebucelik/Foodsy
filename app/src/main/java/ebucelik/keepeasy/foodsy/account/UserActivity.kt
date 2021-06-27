package ebucelik.keepeasy.foodsy.account

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Base64
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.tabs.TabLayout
import ebucelik.keepeasy.foodsy.MainActivity
import ebucelik.keepeasy.foodsy.R
import ebucelik.keepeasy.foodsy.entitiy.User
import ebucelik.keepeasy.foodsy.home.OfferDetailActivity
import ebucelik.keepeasy.foodsy.repositories.UserRepository
import ebucelik.keepeasy.foodsy.viewmodels.UserActivityViewModel
import okhttp3.*
import java.io.IOException
import java.util.ArrayList

class UserActivity : AppCompatActivity() {

    private lateinit var offerFragment: OfferFragment
    private lateinit var receivedReviewFragment: ReceivedReviewFragment
    private lateinit var rates: ArrayList<ImageView>
    private lateinit var user: User
    private lateinit var usernameView: TextView
    private lateinit var profileImage: ImageView
    private lateinit var ratingQuantity: TextView
    private lateinit var tabLayout: TabLayout

    companion object{
        lateinit var userActivityViewModel: UserActivityViewModel
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user)

        user = intent.getSerializableExtra(OfferDetailActivity.USER) as User

        usernameView = findViewById(R.id.username)
        profileImage = findViewById(R.id.profileImage)
        offerFragment = OfferFragment(user.userUUID)
        receivedReviewFragment = ReceivedReviewFragment(user.userUUID)
        ratingQuantity = findViewById(R.id.ratingQuantity)
        tabLayout = findViewById(R.id.accountTabLayout)

        userActivityViewModel = ViewModelProvider(this).get(UserActivityViewModel::class.java)

        userActivityViewModel.reviewQuantity.observe(this, Observer { reviewQuantity ->
            ratingQuantity.text = "${reviewQuantity} ratings"
        })

        UserRepository.getReviewQuantity(user.userUUID)

        rates = arrayListOf(
                findViewById(R.id.rate1),
                findViewById(R.id.rate2),
                findViewById(R.id.rate3),
                findViewById(R.id.rate4),
                findViewById(R.id.rate5)
        )

        usernameView.text = user.username

        if (user.profileImage != null){
            profileImage.setImageBitmap(decodeImage(user.profileImage))
        }

        getStarReview()

        tabLayout.addOnTabSelectedListener(object: TabLayout.OnTabSelectedListener{
            override fun onTabSelected(tab: TabLayout.Tab?) {
                when(tab?.position){
                    0 -> changeFragment(offerFragment)
                    1 -> changeFragment(receivedReviewFragment)
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {}

            override fun onTabReselected(tab: TabLayout.Tab?) {}
        })

        changeFragment(offerFragment)
    }

    private fun changeFragment(fragment: Fragment){
        this.supportFragmentManager.beginTransaction().apply {
            replace(R.id.fLAccountFragment, fragment)
            commit()
        }
    }

    private fun getStarReview(){
        val url = "${MainActivity.IP}/reviewAverage?uuid=${user.userUUID}"

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