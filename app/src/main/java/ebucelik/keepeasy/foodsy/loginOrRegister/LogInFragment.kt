package ebucelik.keepeasy.foodsy.loginOrRegister


import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.Toast
import androidx.navigation.findNavController
import ebucelik.keepeasy.foodsy.Globals
import ebucelik.keepeasy.foodsy.MainActivity
import ebucelik.keepeasy.foodsy.R
import ebucelik.keepeasy.foodsy.databinding.FragmentLogInBinding
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException

/**
 * A simple [Fragment] subclass.
 */
class LogInFragment() : Fragment(R.layout.fragment_log_in) {

    private lateinit var binding: FragmentLogInBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentLogInBinding.bind(view)

        binding.loginRegisterViewModel = LogInActivity.loginActivityViewModel

        binding.loginBtn.setOnClickListener {
            if(checkUsername() && checkPassword()){
                login()
            }
        }

        binding.notRegistered.setOnClickListener {
            view.findNavController().navigate(R.id.action_logInFragment_to_registrationFragment)
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
        val url = "${MainActivity.IP}/login"

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
                val body = response.body?.string()

                activity?.runOnUiThread {
                    when (response.code) {
                        202 -> {
                            Globals.uuid = body.toString()
                            saveUUID(body.toString())
                            (activity as LogInActivity).openHomeActivity()
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

                response.close()
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