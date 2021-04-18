package ebucelik.keepeasy.foodsy.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ebucelik.keepeasy.foodsy.entitiy.Offer

class HomeActivityViewModel : ViewModel() {

    private var _currentOffer = MutableLiveData<Offer>()
    val currentOffer: LiveData<Offer>
        get() = _currentOffer

    fun setOffer(offer: Offer){
        _currentOffer.value = offer
    }
}