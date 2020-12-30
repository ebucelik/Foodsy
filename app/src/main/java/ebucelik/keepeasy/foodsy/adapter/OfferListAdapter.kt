package ebucelik.keepeasy.foodsy.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import ebucelik.keepeasy.foodsy.R
import ebucelik.keepeasy.foodsy.entitiy.OfferList

class OfferListAdapter(context: Context, offerList: OfferList) : BaseAdapter(){

    private val homeContext: Context = context
    private val offer: OfferList = offerList

    override fun getCount(): Int {
        return offer.offerList.size
    }

    override fun getItem(position: Int): Any {
        return offer.offerList[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, view: View?, viewGroup: ViewGroup?): View {
        val layoutInflater = LayoutInflater.from(homeContext)
        val rowHome = layoutInflater.inflate(R.layout.row_home, viewGroup, false)

        /*val mealImage = rowHome.findViewById<ImageView>(R.id.mealImage)
        Picasso.get().load(meal.meals[position].strMealThumb).into(mealImage)

        val sdf = SimpleDateFormat("dd.MM.yyyy", Locale.GERMAN)

        val username = rowHome.findViewById<TextView>(R.id.username)
        try {
            username.text = names[position]
        }catch (e: ArrayIndexOutOfBoundsException){
            e.printStackTrace()
        }

        val offeredDate = rowHome.findViewById<TextView>(R.id.offeredDate)
        offeredDate.text = "Date: "  + sdf.format(Date())*/

        val mealName = rowHome.findViewById<TextView>(R.id.mealName)
        mealName.text = offer.offerList[position].mealName

        val mealCategory = rowHome.findViewById<TextView>(R.id.mealCategory)
        mealCategory.text = offer.offerList[position].category

        val mealArea = rowHome.findViewById<TextView>(R.id.mealArea)
        mealArea.text = offer.offerList[position].area

        return rowHome
    }
}