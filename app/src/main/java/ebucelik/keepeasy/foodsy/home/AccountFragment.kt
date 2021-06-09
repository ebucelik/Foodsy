package ebucelik.keepeasy.foodsy.home

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Base64
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.google.android.material.button.MaterialButton
import com.google.android.material.tabs.TabLayout
import ebucelik.keepeasy.foodsy.Globals.setUUIDtoEmpty
import ebucelik.keepeasy.foodsy.MainActivity
import ebucelik.keepeasy.foodsy.R
import ebucelik.keepeasy.foodsy.Globals.user
import ebucelik.keepeasy.foodsy.Globals.uuid
import ebucelik.keepeasy.foodsy.account.OfferFragment
import ebucelik.keepeasy.foodsy.account.OrderFragment
import ebucelik.keepeasy.foodsy.account.ReceivedReviewFragment
import ebucelik.keepeasy.foodsy.entitiy.User
import ebucelik.keepeasy.foodsy.repositories.AccountRepository
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException
import java.util.ArrayList

class AccountFragment() : Fragment(R.layout.fragment_account) {

    private lateinit var offerFragment: OfferFragment
    private lateinit var orderFragment: OrderFragment
    private lateinit var receivedReviewFragment: ReceivedReviewFragment
    private lateinit var rates: ArrayList<ImageView>
    private lateinit var firstnameView: TextView
    private lateinit var lastnameView: TextView
    private lateinit var usernameView: TextView
    private lateinit var profileImage: ImageView
    private lateinit var ratingQuantity: TextView
    private lateinit var accountTabLayout: TabLayout
    private lateinit var lastSelectedTabFragment: Fragment
    private lateinit var deleteAccountBtn: MaterialButton

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //Binding don't work. Don't know why exactly but with the included TabItems it don't work.
        accountTabLayout = view.findViewById<TabLayout>(R.id.accountTabLayout)
        firstnameView = view.findViewById(R.id.firstname)
        lastnameView = view.findViewById(R.id.lastname)
        usernameView = view.findViewById(R.id.username)
        profileImage = view.findViewById(R.id.profileImage)
        ratingQuantity = view.findViewById(R.id.ratingQuantity)
        deleteAccountBtn = view.findViewById(R.id.deleteAccountBtn)

        initUserDetails()

        rates = arrayListOf(
                view.findViewById(R.id.rate1),
                view.findViewById(R.id.rate2),
                view.findViewById(R.id.rate3),
                view.findViewById(R.id.rate4),
                view.findViewById(R.id.rate5)
        )

        offerFragment = OfferFragment()
        orderFragment = OrderFragment()
        receivedReviewFragment = ReceivedReviewFragment()

        getStarReview()

        accountTabLayout.addOnTabSelectedListener(object: TabLayout.OnTabSelectedListener{
            override fun onTabSelected(tab: TabLayout.Tab?) {
                when(tab?.position){
                    0 -> changeFragment(offerFragment)
                    1 -> changeFragment(orderFragment)
                    2 -> changeFragment(receivedReviewFragment)
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {}
            override fun onTabReselected(tab: TabLayout.Tab?) {}
        })

        lastSelectedTabFragment = offerFragment

        deleteAccountBtn.setOnClickListener {
            deleteAccount()
        }
    }

    override fun onStart() {
        super.onStart()

        changeFragment(lastSelectedTabFragment)
    }

    private fun changeFragment(fragment: Fragment){
        (activity as HomeActivity).supportFragmentManager.beginTransaction().apply {
            replace(R.id.fLAccountFragment, fragment)
            commit()
        }

        lastSelectedTabFragment = fragment
    }

    private fun deleteAccount(){
        val url = "${MainActivity.IP}/delete"

        val jsonObject = JSONObject()

        try {
            jsonObject.put("userUUID", user.userUUID)
                    .put("username", user.username)
                    .put("firstname", user.firstname)
                    .put("surname", user.surname)
                    .put("profileImage", user.profileImage)
        }catch (e: JSONException){
            e.printStackTrace()
        }

        val mediaType = "application/json; charset=utf-8".toMediaType()
        val body = jsonObject.toString().toRequestBody(mediaType)

        val request = Request.Builder()
                .url(url)
                .delete(body)
                .build()

        val client = OkHttpClient()
        client.newCall(request).enqueue(object: Callback{
            override fun onResponse(call: Call, response: Response) {
                if(response.code == 200){
                    uuid = ""
                    user = User()
                    setUUIDtoEmpty(requireContext())
                    (activity as HomeActivity).openLoginActivity()
                }

                response.close()
            }

            override fun onFailure(call: Call, e: IOException) {}
        })
    }

    private fun getStarReview(){
        val url = "${MainActivity.IP}/reviewAverage?uuid=$uuid"

        val request = Request.Builder()
                .url(url)
                .get()
                .build()

        val client = OkHttpClient()
        client.newCall(request).enqueue(object: Callback{
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

    private fun initUserDetails(){
        firstnameView.text = user.firstname
        lastnameView.text = user.surname
        usernameView.text = user.username
        profileImage.setImageBitmap(decodeImage(user.profileImage))
        ratingQuantity.text = "${HomeActivity.homeActivityViewModel.reviewQuantity.value} ratings"
    }

    private fun decodeImage(encodedImage: String): Bitmap {
        val imageBytes = Base64.decode(encodedImage, Base64.DEFAULT)
        return BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
    }
}