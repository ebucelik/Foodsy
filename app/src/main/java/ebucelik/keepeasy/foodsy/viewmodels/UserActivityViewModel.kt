package ebucelik.keepeasy.foodsy.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class UserActivityViewModel:ViewModel() {

    private var _reviewQuantity = MutableLiveData<Int>()
    val reviewQuantity: LiveData<Int>
        get() = _reviewQuantity

    fun setReviewQuantity(reviewQty: Int){
        _reviewQuantity.value = reviewQty
    }
}