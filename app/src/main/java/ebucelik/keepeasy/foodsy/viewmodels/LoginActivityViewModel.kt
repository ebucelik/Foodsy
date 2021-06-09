package ebucelik.keepeasy.foodsy.viewmodels

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class LoginActivityViewModel : ViewModel() {

    val username = MutableLiveData<String>()

    val password = MutableLiveData<String>()

    val profileImageRegistration = MutableLiveData<Uri>()

    val usernameRegistration = MutableLiveData<String>()

    val passwordRegistration = MutableLiveData<String>()

    val firstnameRegistration = MutableLiveData<String>()

    val lastnameRegistration = MutableLiveData<String>()
}