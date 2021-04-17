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
import ebucelik.keepeasy.foodsy.entitiy.OrderList
import java.text.SimpleDateFormat

class OrderListAdapter(context: Context, orderList: OrderList) : BaseAdapter(){

    private val homeContext: Context = context
    private val order: OrderList = orderList

    override fun getCount(): Int {
        return order.orderingList.size
    }

    override fun getItem(position: Int): Any {
        return order.orderingList[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, view: View?, viewGroup: ViewGroup?): View {
        val layoutInflater = LayoutInflater.from(homeContext)
        val rowAccOrder = layoutInflater.inflate(R.layout.row_home, viewGroup, false)

        val mealName = rowAccOrder.findViewById<TextView>(R.id.mealName)
        mealName.text = order.orderingList[position].offer.mealName

        val mealArea = rowAccOrder.findViewById<TextView>(R.id.mealArea)
        mealArea.text = order.orderingList[position].offer.area

        val mealOfferDate = rowAccOrder.findViewById<TextView>(R.id.offeredDate)

        val simpleDateFormat = SimpleDateFormat("dd.MM.yyyy HH:mm")
        mealOfferDate.text = simpleDateFormat.format(order.orderingList[position].offer.currentTimestamp)

        val mealImage = rowAccOrder.findViewById<ImageView>(R.id.mealImage)
        try {
            if(order.orderingList[position].offer.encodedImage != null){
                mealImage.setImageBitmap(decodeImage(order.orderingList[position].offer.encodedImage))
            }
        }catch (e: Exception){
            e.printStackTrace()
        }

        val mealProfileImage = rowAccOrder.findViewById<ImageView>(R.id.profileImage)
        try {
            if(order.orderingList[position].offer.user.profileImage != null){
                mealProfileImage.setImageBitmap(decodeImage(order.orderingList[position].offer.user.profileImage))
            }
        }catch (e: Exception){
            e.printStackTrace()
        }

        return rowAccOrder
    }

    private fun decodeImage(encodedImage: String): Bitmap {
        val imageBytes = Base64.decode(encodedImage, Base64.DEFAULT)
        return BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
    }
}