package ebucelik.keepeasy.foodsy.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ebucelik.keepeasy.foodsy.entitiy.Offer
import ebucelik.keepeasy.foodsy.repositories.AccountRepository

class HomeActivityViewModel : ViewModel() {

    private var _currentOffer = MutableLiveData<Offer>()
    val currentOffer: LiveData<Offer>
        get() = _currentOffer

    private var _reviewQuantity = MutableLiveData<Int>()
    val reviewQuantity: LiveData<Int>
        get() = _reviewQuantity

    fun setOffer(offer: Offer){
        _currentOffer.value = offer
    }

    fun setReviewQuantity(reviewQty: Int){
        _reviewQuantity.value = reviewQty
    }
}