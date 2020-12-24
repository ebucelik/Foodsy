package ebucelik.keepeasy.foodsy.home

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.google.gson.GsonBuilder
import com.squareup.picasso.Picasso
import ebucelik.keepeasy.foodsy.R
import ebucelik.keepeasy.foodsy.databinding.FragmentHomeBinding
import ebucelik.keepeasy.foodsy.mealDetail.MealDetailActivity
import okhttp3.*
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class HomeFragment(private val home: HomeActivity) : Fragment(R.layout.fragment_home){

    private lateinit var binding: FragmentHomeBinding
    private lateinit var mealFeed: MealFeed
    private val homeActivity: HomeActivity = home

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentHomeBinding.bind(view)

        fetchJson()

        binding.homeListView.setOnItemClickListener { parent, view, position, id ->
            val meal = parent.getItemAtPosition(position) as Meals
            homeActivity.openMealDetailActivity("Ebu", meal.strMeal, meal.strMealThumb)
        }
    }

    private fun fetchJson(){
        val url = "https://www.themealdb.com/api/json/v1/1/search.php?f=m"

        val request = Request.Builder().url(url).build()

        val client = OkHttpClient()
        client.newCall(request).enqueue(object: Callback{
            override fun onResponse(call: Call, response: Response){
                val body = response?.body?.string()

                val gson = GsonBuilder().create()
                mealFeed = gson.fromJson(body, MealFeed::class.java)

                activity?.runOnUiThread {
                    binding.homeListView.adapter = HomeAdapter(activity?.baseContext!!, mealFeed)
                }
            }

            override fun onFailure(call: Call, e: IOException) {
                println("Failure")
            }
        })
    }

    private class HomeAdapter(context: Context, mealFeed: MealFeed) : BaseAdapter(){

        private var names: Array<String> = arrayOf("Ebu", "Gabriel", "Ahmedin", "Turgut", "Leon", "Max", "Abdi", "Haki", "Maxi", "Taxi", "Susi", "Sushi", "Taki", "Maki", "Saki", "Laki", "Fifa", "Fortnite", "CoD");
        private val homeContext: Context = context
        private val meal: MealFeed = mealFeed

        override fun getCount(): Int {
            return meal.meals.size
        }

        override fun getItem(position: Int): Any {
            return meal.meals[position]
        }

        override fun getItemId(position: Int): Long {
            return position.toLong()
        }

        override fun getView(position: Int, view: View?, viewGroup: ViewGroup?): View {
            val layoutInflater = LayoutInflater.from(homeContext)
            val rowHome = layoutInflater.inflate(R.layout.row_home, viewGroup, false)

            val mealImage = rowHome.findViewById<ImageView>(R.id.mealImage)
            Picasso.get().load(meal.meals[position].strMealThumb).into(mealImage)

            val sdf = SimpleDateFormat("dd.MM.yyyy", Locale.GERMAN)

            val username = rowHome.findViewById<TextView>(R.id.username)
            username.text = names[position]

            val offeredDate = rowHome.findViewById<TextView>(R.id.offeredDate)
            offeredDate.text = "Date: "  + sdf.format(Date())

            val mealName = rowHome.findViewById<TextView>(R.id.mealName)
            mealName.text = meal.meals[position].strMeal

            val validToDate = rowHome.findViewById<TextView>(R.id.validToDate)
            validToDate.text = "Valid until " + sdf.format(Date())

            val mealCategory = rowHome.findViewById<TextView>(R.id.mealCategory)
            mealCategory.text = meal.meals[position].strCategory

            val mealArea = rowHome.findViewById<TextView>(R.id.mealArea)
            mealArea.text = meal.meals[position].strArea

            return rowHome
        }
    }

    private class MealFeed(val meals: List<Meals>)

    //TODO: Um Usernamen, Datum usw. erweitern...
    private class Meals(val strMeal: String, val strMealThumb: String, val strCategory: String, val strArea: String)
}