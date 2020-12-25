package ebucelik.keepeasy.foodsy.home

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ListAdapter
import com.google.gson.GsonBuilder
import ebucelik.keepeasy.foodsy.Adapter.MealListAdapter
import ebucelik.keepeasy.foodsy.R
import ebucelik.keepeasy.foodsy.databinding.FragmentSearchBinding
import ebucelik.keepeasy.foodsy.meal.Meal
import ebucelik.keepeasy.foodsy.meal.MealFeed
import okhttp3.*
import java.io.IOException
import java.lang.Exception
import java.lang.NullPointerException

class SearchFragment(val home: HomeActivity) : Fragment(R.layout.fragment_search) {

    private lateinit var mealFeed: MealFeed
    private lateinit var binding: FragmentSearchBinding
    private var names: Array<String> = arrayOf("Ebu", "Gabriel", "Ahmedin", "Turgut", "Leon", "Max", "Abdi", "Haki", "Maxi", "Taxi", "Susi", "Sushi", "Taki", "Maki", "Saki", "Laki", "Fifa", "Fortnite", "CoD");
    private val homeActivity: HomeActivity = home

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentSearchBinding.bind(view)

        binding.searchTxt.addTextChangedListener(object: TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                //Don't need yet
            }

            override fun onTextChanged(meal: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if(meal != null && meal.isNotEmpty()){
                    fetchJson(meal.toString())
                }else{
                    binding.searchedMealsList.adapter = null
                }
            }

            override fun afterTextChanged(p0: Editable?) {
                //Don't need yet
            }
        })

        binding.searchedMealsList.setOnItemClickListener { parent, view, position, id ->
            val meal = parent.getItemAtPosition(position) as Meal
            try {
                homeActivity.openMealDetailActivity(names[position], meal.strMeal, meal.strMealThumb, meal.strCategory, meal.strArea, meal.strIngredient1, meal.strIngredient2, meal.strIngredient3)
            }catch (e: ArrayIndexOutOfBoundsException){
                e.printStackTrace()
            }
        }
    }

    private fun fetchJson(meal: String){
        val url = "https://www.themealdb.com/api/json/v1/1/search.php?f=$meal"

        val request = Request.Builder().url(url).build()

        val client = OkHttpClient()
        client.newCall(request).enqueue(object: Callback {
            override fun onResponse(call: Call, response: Response){
                val body = response?.body?.string()

                val gson = GsonBuilder().create()
                try {
                    if(body.isNullOrEmpty())
                        throw NullPointerException("Don't found any meals.")

                    mealFeed = gson.fromJson(body, MealFeed::class.java)

                    if(mealFeed.meals.isNullOrEmpty())
                        throw NullPointerException("Don't found any meals.")

                    activity?.runOnUiThread {
                        binding.searchedMealsList.adapter = MealListAdapter(activity?.baseContext!!, mealFeed, names)
                    }
                }catch (e: Exception){
                    e.printStackTrace()
                }
            }

            override fun onFailure(call: Call, e: IOException) {
                println("Failure")
            }
        })
    }
}