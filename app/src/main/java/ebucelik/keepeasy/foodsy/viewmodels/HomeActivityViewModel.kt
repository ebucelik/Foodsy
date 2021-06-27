package ebucelik.keepeasy.foodsy.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ebucelik.keepeasy.foodsy.entitiy.Chat
import ebucelik.keepeasy.foodsy.entitiy.ChatPool
import ebucelik.keepeasy.foodsy.entitiy.Offer
import ebucelik.keepeasy.foodsy.repositories.AccountRepository

class HomeActivityViewModel : ViewModel() {

    val currentOffer = MutableLiveData<Offer>()

    val reviewQuantity = MutableLiveData<Int>()

    private var _offerList = MutableLiveData<List<Offer>>()
    val offerList: LiveData<List<Offer>>
        get() = _offerList

    var chatPoolList = MutableLiveData<List<ChatPool>>()
}