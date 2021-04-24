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
import androidx.databinding.DataBindingUtil
import ebucelik.keepeasy.foodsy.R
import ebucelik.keepeasy.foodsy.databinding.RowHomeBinding
import ebucelik.keepeasy.foodsy.entitiy.OfferList
import java.text.SimpleDateFormat

class OfferListAdapter(context: Context, _offerList: OfferList) : BaseAdapter(){

    private val homeContext: Context = context
    private val offerList: OfferList = _offerList
    private lateinit var binding: RowHomeBinding

    override fun getCount(): Int {
        return offerList.offeringList.size
    }

    override fun getItem(position: Int): Any {
        return offerList.offeringList[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, view: View?, viewGroup: ViewGroup?): View {
        val layoutInflater = LayoutInflater.from(homeContext)
        binding = DataBindingUtil.inflate(layoutInflater, R.layout.row_home, viewGroup, false)

        binding.offer = offerList.offeringList[position]
        val offer = offerList.offeringList[position]

        val simpleDateFormat = SimpleDateFormat("dd.MM.yyyy HH:mm")
        binding.offeredDate.text = simpleDateFormat.format(offer.currentTimestamp)

        binding.price.text = offer.price.toString() + "€"

        try {
            if(!offer.encodedImage.isNullOrEmpty()){
                binding.mealImage.setImageBitmap(decodeImage(offer.encodedImage))
            }

            if(!offer.user?.profileImage.isNullOrEmpty()){
                binding.profileImage.setImageBitmap(offer.user?.let { decodeImage(it.profileImage) })
            }
        }catch (e: Exception){
            e.printStackTrace()
        }

        return binding.root
    }

    private fun decodeImage(encodedImage: String):Bitmap{
        val imageBytes = Base64.decode(encodedImage, Base64.DEFAULT)
        return BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
    }
}