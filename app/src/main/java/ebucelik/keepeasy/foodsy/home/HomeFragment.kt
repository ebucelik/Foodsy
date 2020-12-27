package ebucelik.keepeasy.foodsy.home

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import com.google.gson.GsonBuilder
import ebucelik.keepeasy.foodsy.adapter.MealListAdapter
import ebucelik.keepeasy.foodsy.R
import ebucelik.keepeasy.foodsy.databinding.FragmentHomeBinding
import ebucelik.keepeasy.foodsy.meal.Meal
import ebucelik.keepeasy.foodsy.meal.MealFeed
import okhttp3.*
import java.io.IOException

class HomeFragment(home: HomeActivity) : Fragment(R.layout.fragment_home){

    private lateinit var binding: FragmentHomeBinding
    private lateinit var mealFeed: MealFeed
    private val homeActivity: HomeActivity = home
    private var names: Array<String> = arrayOf("Ebu", "Gabriel", "Ahmedin", "Turgut", "Leon", "Max", "Abdi", "Haki", "Maxi", "Taxi", "Susi", "Sushi", "Taki", "Maki", "Saki", "Laki", "Fifa", "Fortnite", "CoD");

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentHomeBinding.bind(view)

        fetchJson()

        binding.homeListView.setOnItemClickListener { parent, view, position, id ->
            val meal = parent.getItemAtPosition(position) as Meal
            try {
                homeActivity.openMealDetailActivity(names[position], meal.strMeal, meal.strMealThumb, meal.strCategory, meal.strArea, meal.strIngredient1, meal.strIngredient2, meal.strIngredient3)
            }catch (e: ArrayIndexOutOfBoundsException){
                e.printStackTrace()
            }
        }

        binding.logout.setOnClickListener {
            setUUIDtoEmpty()
            homeActivity.openLoginActivity()
        }
    }

    private fun fetchJson(){
        val url = "https://www.themealdb.com/api/json/v1/1/search.php?f=s"

        val request = Request.Builder().url(url).build()

        val client = OkHttpClient()
        client.newCall(request).enqueue(object: Callback{
            override fun onResponse(call: Call, response: Response){
                val body = response?.body?.string()

                val gson = GsonBuilder().create()
                mealFeed = gson.fromJson(body, MealFeed::class.java)

                activity?.runOnUiThread {
                    binding.homeListView.adapter = MealListAdapter(activity?.baseContext!!, mealFeed, names)
                }
            }

            override fun onFailure(call: Call, e: IOException) {
                println("Failure")
            }
        })
    }

    private fun setUUIDtoEmpty(){
        val sharedPref = activity?.getSharedPreferences("uuid", Context.MODE_PRIVATE)
        sharedPref
                ?.edit()
                ?.putString(R.string.uuid.toString(), "")
                ?.apply()
    }
}