package ebucelik.keepeasy.foodsy.loginOrRegister

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.google.gson.GsonBuilder
import ebucelik.keepeasy.foodsy.R
import ebucelik.keepeasy.foodsy.adapter.MealListAdapter
import ebucelik.keepeasy.foodsy.databinding.FragmentRegistrationBinding
import ebucelik.keepeasy.foodsy.home.HomeActivity
import ebucelik.keepeasy.foodsy.meal.MealFeed
import ebucelik.keepeasy.foodsy.user.User
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException

/**
 * A simple [Fragment] subclass.
 */
class RegistrationFragment(_logInActivity: LogInActivity) : Fragment(R.layout.fragment_registration) {

    private lateinit var binding: FragmentRegistrationBinding

    private var logInActivity: LogInActivity = _logInActivity

    private lateinit var user: User

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentRegistrationBinding.bind(view)

        binding.registered.setOnClickListener {
            logInActivity.changeFragment(logInActivity.loginFragment)
        }

        binding.registerBtn.setOnClickListener {
            if(checkFirstname() && checkLastname() && checkUsername() && checkPassword()){
                createUser()
            }
        }
    }

    private fun checkFirstname(): Boolean{
        binding.apply {
            if(binding.firstname.length() == 0){
                binding.firstnameLayout.isHelperTextEnabled = true
                binding.firstnameLayout.error = "Please specify your firstname"
                return false
            }else{
                binding.firstnameLayout.error = null
                return true
            }
        }
    }

    private fun checkLastname(): Boolean{
        binding.apply {
            if(binding.lastname.length() == 0){
                binding.lastnameLayout.isHelperTextEnabled = true
                binding.lastnameLayout.error = "Please specify your lastname"
                return false
            }else{
                binding.lastnameLayout.error = null
                return true
            }
        }
    }

    private fun checkUsername(): Boolean{
        binding.apply {
            if(binding.username.length() == 0){
                binding.usernameLayout.isHelperTextEnabled = true
                binding.usernameLayout.error = "Please specify your username"
                return false
            }else{
                binding.usernameLayout.error = null
                return true
            }
        }
    }

    private fun checkPassword(): Boolean{
        binding.apply {
            if(binding.password.length() == 0){
                binding.passwordLayout.isHelperTextEnabled = true
                binding.passwordLayout.error = "Please specify your password"
                return false
            }else{
                binding.passwordLayout.error = null
                return true
            }
        }
    }

    private fun createUser(){
        /*
        Normally our backend runs at localhost:8080 but the simulator uses also this port on the localhost for that reason
        we must use this IP address 10.0.2.2
        */
        val url = "http://10.0.2.2:8080/user"

        val jsonObject = JSONObject()

        try {
            jsonObject.put("username", binding.username.text.toString())
                    .put("firstname", binding.firstname.text.toString())
                    .put("surname", binding.lastname.text.toString())
                    .put("password", binding.password.text.toString())
        }catch (e: JSONException){
            e.printStackTrace()
        }

        val mediaType = "application/json; charset=utf-8".toMediaType()
        val body = jsonObject.toString().toRequestBody(mediaType)

        val request = Request.Builder()
                .url(url)
                .post(body)
                .build()

        val client = OkHttpClient()
        client.newCall(request).enqueue(object: Callback {
            override fun onResponse(call: Call, response: Response){
                val body = response.body?.string()

                val gson = GsonBuilder().create()
                user = gson.fromJson(body, User::class.java)

                activity?.runOnUiThread {
                    when (response.code) {
                        201 -> {
                            saveUUID()
                            logInActivity.openHomeActivity()
                        }
                        409 -> {
                            Toast.makeText(activity, "This Username already exists.", Toast.LENGTH_SHORT).show()
                        }
                        else -> {
                            Toast.makeText(activity, "Server error.", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }

            override fun onFailure(call: Call, e: IOException) {
                e.printStackTrace()
            }
        })
    }

    private fun saveUUID(){
        val sharedPref = activity?.getSharedPreferences("uuid", Context.MODE_PRIVATE)
        sharedPref
                ?.edit()
                ?.putString(R.string.uuid.toString(), user.getUUID())
                ?.apply()
    }
}
