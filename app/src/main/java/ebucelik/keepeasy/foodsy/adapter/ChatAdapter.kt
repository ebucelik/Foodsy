package ebucelik.keepeasy.foodsy.adapter

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import ebucelik.keepeasy.foodsy.Globals
import ebucelik.keepeasy.foodsy.R
import ebucelik.keepeasy.foodsy.databinding.ItemChatBinding
import ebucelik.keepeasy.foodsy.entitiy.ChatPool
import ebucelik.keepeasy.foodsy.entitiy.OfferList

class ChatAdapter(context: Context, val mChats: List<ChatPool>) : BaseAdapter() {

    private val chatPoolContext: Context = context
    private lateinit var binding: ItemChatBinding

    private fun decodeImage(encodedImage: String): Bitmap {
        val imageBytes = Base64.decode(encodedImage, Base64.DEFAULT)
        return BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
    }

    override fun getCount(): Int {
        return mChats.size
    }

    override fun getItem(p0: Int): Any {
        return mChats.get(p0)
    }

    override fun getItemId(p0: Int): Long {
        return p0.toLong()
    }

    override fun getView(p0: Int, p1: View?, p2: ViewGroup?): View {
        val layoutInflater = LayoutInflater.from(chatPoolContext)
        binding = DataBindingUtil.inflate(layoutInflater, R.layout.item_chat, p2, false)

        val chatPool: ChatPool = mChats.get(p0)

        if(chatPool.user.userUUID != Globals.uuid)
            binding.personName.setText(chatPool.user.username)
        else
            binding.personName.setText(chatPool.withUser.username)

        try {
            if(!chatPool.user.profileImage.isNullOrEmpty()){
                binding.profileImage.setImageBitmap(decodeImage(chatPool.user.profileImage))
            } else {
                binding.profileImage.setImageBitmap(decodeImage(chatPool.withUser.profileImage))
            }
        }catch (e: Exception){
            e.printStackTrace()
        }

        return binding.root
    }
}