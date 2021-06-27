package ebucelik.keepeasy.foodsy.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ebucelik.keepeasy.foodsy.entitiy.Chat

class MessageActivityViewModel:ViewModel() {

    var chatList = MutableLiveData<List<Chat>>()

}