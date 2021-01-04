package ebucelik.keepeasy.foodsy.adapter

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import ebucelik.keepeasy.foodsy.R
import ebucelik.keepeasy.foodsy.entitiy.OfferList
import java.text.SimpleDateFormat


class OfferListAdapter(context: Context, offerList: OfferList) : BaseAdapter(){

    private val homeContext: Context = context
    private val offer: OfferList = offerList

    override fun getCount(): Int {
        return offer.offeringList.size
    }

    override fun getItem(position: Int): Any {
        return offer.offeringList[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, view: View?, viewGroup: ViewGroup?): View {
        val layoutInflater = LayoutInflater.from(homeContext)
        val rowHome = layoutInflater.inflate(R.layout.row_home, viewGroup, false)

        val mealName = rowHome.findViewById<TextView>(R.id.mealName)
        mealName.text = offer.offeringList[position].mealName

        val mealArea = rowHome.findViewById<TextView>(R.id.mealArea)
        mealArea.text = offer.offeringList[position].area

        val mealOfferDate = rowHome.findViewById<TextView>(R.id.offeredDate)

        val simpleDateFormat = SimpleDateFormat("dd.MM.yyyy HH:mm")
        mealOfferDate.text = simpleDateFormat.format(offer.offeringList[position].currentTimestamp)

        val mealImage = rowHome.findViewById<ImageView>(R.id.mealImage)
        try {
            if(offer.offeringList[position].encodedImage != null){
                mealImage.setImageBitmap(decodeImage(offer.offeringList[position].encodedImage))
            }
        }catch (e: Exception){
            e.printStackTrace()
        }

        val mealProfileImage = rowHome.findViewById<ImageView>(R.id.profileImage)
        try {
            if(offer.offeringList[position].user.getProfileImage() != null){
                mealProfileImage.setImageBitmap(decodeImage(offer.offeringList[position].user.getProfileImage()))
            }
        }catch (e: Exception){
            e.printStackTrace()
        }

        return rowHome
    }

    private fun decodeImage(encodedImage: String):Bitmap{
        val imageBytes = Base64.decode(encodedImage, Base64.DEFAULT)
        return BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
    }
}