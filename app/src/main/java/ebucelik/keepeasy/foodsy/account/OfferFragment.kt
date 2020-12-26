package ebucelik.keepeasy.foodsy.account

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.gson.GsonBuilder
import ebucelik.keepeasy.foodsy.R
import ebucelik.keepeasy.foodsy.adapter.MealListAdapter
import ebucelik.keepeasy.foodsy.databinding.FragmentOfferBinding
import ebucelik.keepeasy.foodsy.home.HomeActivity
import ebucelik.keepeasy.foodsy.meal.Meal
import ebucelik.keepeasy.foodsy.meal.MealFeed
import okhttp3.*
import java.io.IOException

class OfferFragment() : Fragment(R.layout.fragment_offer) {

    private lateinit var binding: FragmentOfferBinding
    private lateinit var mealFeed: MealFeed
    private var names: Array<String> = arrayOf("Ebu", "Gabriel", "Ahmedin", "Turgut", "Leon", "Max", "Abdi", "Haki", "Maxi", "Taxi", "Susi", "Sushi", "Taki", "Maki", "Saki", "Laki", "Fifa", "Fortnite", "CoD");

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentOfferBinding.bind(view)

        fetchJson()
    }

    private fun fetchJson(){
        val url = "https://www.themealdb.com/api/json/v1/1/search.php?f=e"

        val request = Request.Builder().url(url).build()

        val client = OkHttpClient()
        client.newCall(request).enqueue(object: Callback {
            override fun onResponse(call: Call, response: Response){
                val body = response?.body?.string()

                val gson = GsonBuilder().create()
                mealFeed = gson.fromJson(body, MealFeed::class.java)

                activity?.runOnUiThread {
                    binding.offerListView.adapter = MealListAdapter(activity?.baseContext!!, mealFeed, names)
                }
            }

            override fun onFailure(call: Call, e: IOException) {
                println("Failure")
            }
        })
    }
}