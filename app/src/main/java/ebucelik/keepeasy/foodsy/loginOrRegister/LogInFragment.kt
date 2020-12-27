package ebucelik.keepeasy.foodsy.loginOrRegister


import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.Toast
import com.google.gson.GsonBuilder
import ebucelik.keepeasy.foodsy.R
import ebucelik.keepeasy.foodsy.databinding.FragmentLogInBinding
import ebucelik.keepeasy.foodsy.home.HomeActivity
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
class LogInFragment(_logInActivity: LogInActivity) : Fragment(R.layout.fragment_log_in) {

    private lateinit var binding: FragmentLogInBinding

    private var logInActivity: LogInActivity = _logInActivity

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentLogInBinding.bind(view)

        binding.loginBtn.setOnClickListener {
            if(checkUsername() && checkPassword()){
                login()
            }
        }

        binding.notRegistered.setOnClickListener {
            logInActivity.changeFragment(logInActivity.registrationFragment)
        }
    }

    private fun checkPassword():Boolean {
        binding.apply {
            if (binding.password.length() == 0){
                binding.passwordLayout.isHelperTextEnabled = true
                binding.passwordLayout.error = getString(R.string.enterPassword)
                return false
            }else{
                binding.passwordLayout.error = null
                return true
            }
        }
    }

    private fun checkUsername():Boolean {
        binding.apply {
            if (binding.username.length() == 0){
                binding.usernameLayout.isHelperTextEnabled = true
                binding.usernameLayout.error = getString(R.string.enterUsername)
                return false
            }else if(binding.username.text!!.contains(" ")){
                binding.usernameLayout.isHelperTextEnabled = true
                binding.usernameLayout.error = getString(R.string.usernameWithSpace)
                return false
            }else{
                binding.usernameLayout.error = null
                return true
            }
        }
    }

    private fun login(){
        val url = "http://10.0.2.2:8080/login"

        val jsonObject = JSONObject()

        try {
            jsonObject.put("username", binding.username.text.toString())
                    .put("password", binding.password.text.toString())
        }catch (e: JSONException){
            e.printStackTrace()
        }

        val mediaType = "application/json; charset=utf-8".toMediaType()
        val body = jsonObject.toString().toRequestBody(mediaType)

        val request = Request.Builder()
                .url(url)
                .put(body)
                .build()

        val client = OkHttpClient()
        client.newCall(request).enqueue(object: Callback {
            override fun onResponse(call: Call, response: Response){
                val body = response?.body?.string()

                activity?.runOnUiThread {
                    when (response.code) {
                        202 -> {
                            saveUUID(body.toString())
                            logInActivity.openHomeActivity()
                        }
                        401 -> {
                            Toast.makeText(activity, "The Username or password is wrong.", Toast.LENGTH_SHORT).show()
                        }
                        404 -> {
                            Toast.makeText(activity, "The User don't exist.", Toast.LENGTH_SHORT).show()
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

    private fun saveUUID(uuid: String){
        val sharedPref = activity?.getSharedPreferences("uuid", Context.MODE_PRIVATE)
        sharedPref
                ?.edit()
                ?.putString(R.string.uuid.toString(), uuid)
                ?.apply()
    }
}