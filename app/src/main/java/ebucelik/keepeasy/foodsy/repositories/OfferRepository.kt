package ebucelik.keepeasy.foodsy.repositories

import androidx.lifecycle.MutableLiveData
import ebucelik.keepeasy.foodsy.entitiy.Offer

//Singleton
object OfferRepository {

    private lateinit var offer: Offer

    fun getOffer() : Offer{
        setOffer()

        return offer
    }

    private fun setOffer(){
        //TODO: Implement http request/response logic here to set the offer
    }
}