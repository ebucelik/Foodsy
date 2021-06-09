package ebucelik.keepeasy.foodsy.loginOrRegister

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Base64
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.Toast
import androidx.core.graphics.drawable.toBitmap
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import com.google.gson.GsonBuilder
import ebucelik.keepeasy.foodsy.Globals
import ebucelik.keepeasy.foodsy.Globals.user
import ebucelik.keepeasy.foodsy.MainActivity
import ebucelik.keepeasy.foodsy.R
import ebucelik.keepeasy.foodsy.databinding.FragmentRegistrationBinding
import ebucelik.keepeasy.foodsy.entitiy.User
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONException
import org.json.JSONObject
import java.io.ByteArrayOutputStream
import java.io.IOException

/**
 * A simple [Fragment] subclass.
 */
class RegistrationFragment() : Fragment(R.layout.fragment_registration) {

    private lateinit var binding: FragmentRegistrationBinding
    private var imageURI: Uri? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentRegistrationBinding.bind(view)

        binding.loginRegisterViewModel = LogInActivity.loginActivityViewModel

        binding.registered.setOnClickListener {
            view.findNavController().popBackStack()
        }

        binding.profileImage.setOnClickListener {
            selectImageFromGallery()
        }

        binding.registerBtn.setOnClickListener {
            if(checkFirstname() && checkLastname() && checkUsername() && checkPassword() && checkProfileImageSelected()){
                createUser()
            }
        }

        setViewContent()
    }

    private fun setViewContent(){
        LogInActivity.loginActivityViewModel.profileImageRegistration.observe(viewLifecycleOwner, Observer { profileImage ->
            setProfileImage(profileImage)
        })
    }

    private fun setProfileImage(uri: Uri){
        binding.profileImage.setImageURI(uri)
        imageURI = uri
    }

    private fun selectImageFromGallery(){
        val intent = Intent(Intent.ACTION_GET_CONTENT, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
        intent.type = "image/*"
        startActivityForResult(intent, 1)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(resultCode == Activity.RESULT_OK && requestCode == 1){
            imageURI = data?.data
            imageURI?.let { LogInActivity.loginActivityViewModel.profileImageRegistration.value = it }
            binding.profileImage.setImageURI(imageURI)
            encodeImage()
        }
    }

    private fun encodeImage():String{
        val bitmap = binding.profileImage.drawable.toBitmap()
        val byteArrayOutputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 50, byteArrayOutputStream)
        val byteArray = byteArrayOutputStream.toByteArray()
        return Base64.encodeToString(byteArray, Base64.DEFAULT)
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

    private fun checkProfileImageSelected(): Boolean{
        return if(imageURI == null){
            Toast.makeText(activity, "Please select a profile image.", Toast.LENGTH_SHORT).show()
            false
        }else{
            true
        }
    }

    private fun createUser(){
        /*
        Normally our backend runs at localhost:8080 but the simulator uses also this port on the localhost for that reason
        we must use this IP address 10.0.2.2
        */
        val url = "${MainActivity.IP}/user"

        val jsonObject = JSONObject()

        try {
            jsonObject.put("username", binding.username.text.toString())
                    .put("firstname", binding.firstname.text.toString())
                    .put("surname", binding.lastname.text.toString())
                    .put("password", binding.password.text.toString())
                    .put("profileImage", encodeImage())
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
                            Globals.uuid = user.userUUID
                            saveUUID()
                            (activity as LogInActivity).openHomeActivity()
                        }
                        409 -> {
                            Toast.makeText(activity, "This Username already exists.", Toast.LENGTH_SHORT).show()
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

    private fun saveUUID(){
        val sharedPref = activity?.getSharedPreferences("uuid", Context.MODE_PRIVATE)
        sharedPref
                ?.edit()
                ?.putString(R.string.uuid.toString(), user.userUUID)
                ?.apply()
    }
}
