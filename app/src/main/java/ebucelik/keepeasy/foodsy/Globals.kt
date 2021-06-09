package ebucelik.keepeasy.foodsy

import android.content.Context
import ebucelik.keepeasy.foodsy.entitiy.Offer
import ebucelik.keepeasy.foodsy.entitiy.Order
import ebucelik.keepeasy.foodsy.entitiy.User

object Globals {
    lateinit var uuid: String
    var user: User = User()
    var openChatTab = false
    lateinit var selectedOffer: Offer
    lateinit var selectedOrder: Order

    fun setUUIDtoEmpty(context: Context){
        val sharedPref = context.getSharedPreferences("uuid", Context.MODE_PRIVATE)
        sharedPref
                ?.edit()
                ?.putString(R.string.uuid.toString(), "")
                ?.apply()
    }
}