package ebucelik.keepeasy.foodsy.viewmodels

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ebucelik.keepeasy.foodsy.databinding.FragmentRegistrationBinding

class LoginActivityViewModel : ViewModel() {

    private var _username = MutableLiveData<String>()
    val username: LiveData<String>
        get() = _username

    private var _password = MutableLiveData<String>()
    val password: LiveData<String>
        get() = _password

    private var _profileImageRegistration = MutableLiveData<Uri>()
    val profileImageRegistration: LiveData<Uri>
        get() = _profileImageRegistration

    private var _usernameRegistration = MutableLiveData<String>()
    val usernameRegistration: LiveData<String>
        get() = _usernameRegistration

    private var _passwordRegistration = MutableLiveData<String>()
    val passwordRegistration: LiveData<String>
        get() = _passwordRegistration

    private var _firstnameRegistration = MutableLiveData<String>()
    val firstnameRegistration: LiveData<String>
        get() = _firstnameRegistration

    private var _lastnameRegistration = MutableLiveData<String>()
    val lastnameRegistration: LiveData<String>
        get() = _lastnameRegistration

    fun setProfileImage(uri: Uri){
        _profileImageRegistration.value = uri
    }

    fun setUsername(username: String){
        _username.value = username
    }

    fun setPassword(password: String){
        _password.value = password
    }

    fun setUsernameRegistration(usernameRegistration: String){
        _usernameRegistration.value = usernameRegistration
    }

    fun setPasswordRegistration(passwordRegistration: String){
        _passwordRegistration.value = passwordRegistration
    }

    fun setFirstnameRegistration(firstnameRegistration: String){
        _firstnameRegistration.value = firstnameRegistration
    }

    fun setLastnameRegistration(lastnameRegistration: String){
        _lastnameRegistration.value = lastnameRegistration
    }
}