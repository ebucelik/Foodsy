package ebucelik.keepeasy.foodsy.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.squareup.picasso.Picasso
import ebucelik.keepeasy.foodsy.R
import ebucelik.keepeasy.foodsy.meal.MealFeed
import java.text.SimpleDateFormat
import java.util.*

class MealListAdapter(context: Context, mealFeed: MealFeed, private val names: Array<String>) : BaseAdapter(){

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
        try {
            username.text = names[position]
        }catch (e: ArrayIndexOutOfBoundsException){
            e.printStackTrace()
        }

        val offeredDate = rowHome.findViewById<TextView>(R.id.offeredDate)
        offeredDate.text = "Date: "  + sdf.format(Date())

        val mealName = rowHome.findViewById<TextView>(R.id.mealName)
        mealName.text = meal.meals[position].strMeal

        val mealCategory = rowHome.findViewById<TextView>(R.id.mealCategory)
        mealCategory.text = meal.meals[position].strCategory

        val mealArea = rowHome.findViewById<TextView>(R.id.mealArea)
        mealArea.text = meal.meals[position].strArea

        return rowHome
    }
}