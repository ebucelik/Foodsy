package ebucelik.keepeasy.foodsy.home

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Base64
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.core.graphics.drawable.toBitmap
import ebucelik.keepeasy.foodsy.MainActivity
import ebucelik.keepeasy.foodsy.R
import ebucelik.keepeasy.foodsy.databinding.FragmentSellBinding
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONException
import org.json.JSONObject
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.IOException
import java.io.InputStream
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

class SellFragment(private val uuid:String) : Fragment(R.layout.fragment_sell) {

    private val REQUEST_SELECT_IMAGE_IN_ALBUM = 1
    private lateinit var binding: FragmentSellBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentSellBinding.bind(view)

        binding.mealImage.setOnClickListener {
            selectImageFromGallery()
        }

        binding.offerMealBtn.setOnClickListener {
            if(checkMealName() && checkMealCategory() && checkMealArea()){
                createOffer()
            }
        }
    }

    private fun checkMealName():Boolean{
        binding.apply {
            return if (binding.mealName.length() == 0){
                binding.mealNameLayout.isHelperTextEnabled = true
                binding.mealNameLayout.error = getString(R.string.mealName)
                false
            }else{
                binding.mealNameLayout.error = null
                true
            }
        }
    }

    private fun checkMealCategory():Boolean{
        binding.apply {
            return if (binding.mealCategory.length() == 0){
                binding.mealCategoryLayout.isHelperTextEnabled = true
                binding.mealCategoryLayout.error = getString(R.string.mealCategory)
                false
            }else{
                binding.mealCategoryLayout.error = null
                true
            }
        }
    }

    private fun checkMealArea():Boolean{
        binding.apply {
            return if (binding.mealArea.length() == 0){
                binding.mealAreaLayout.isHelperTextEnabled = true
                binding.mealAreaLayout.error = getString(R.string.mealArea)
                false
            }else{
                binding.mealAreaLayout.error = null
                true
            }
        }
    }

    private fun selectImageFromGallery(){
        val intent = Intent(Intent.ACTION_GET_CONTENT, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
        intent.type = "image/*"
        startActivityForResult(intent, REQUEST_SELECT_IMAGE_IN_ALBUM)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(resultCode == RESULT_OK && requestCode == REQUEST_SELECT_IMAGE_IN_ALBUM){
            val imageURI = data?.data
            binding.mealImage.setImageURI(imageURI)
            encodeImage()
        }
    }

    private fun encodeImage():String{
        val bitmap = binding.mealImage.drawable.toBitmap()
        val byteArrayOutputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 50, byteArrayOutputStream);
        val byteArray = byteArrayOutputStream.toByteArray()
        return Base64.encodeToString(byteArray, Base64.DEFAULT)
    }

    private fun createOffer(){
        val url = "http://${MainActivity.IP}:8080/offer"

        val jsonObject = JSONObject()

        try {
            jsonObject
                    .put("mealName", binding.mealName.text)
                    .put("category", binding.mealCategory.text)
                    .put("area", binding.mealArea.text)
                    .put("encodedImage", encodeImage())
                    .put("ingredients", binding.mealIngredients.text)
                    .put("timestamp", LocalDateTime.now())
                    .put("userUUID", uuid)
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
        client.newCall(request).enqueue(object : Callback{
            override fun onResponse(call: Call, response: Response) {
                if(response.code == 201){
                    activity?.runOnUiThread{
                        Toast.makeText(activity, "Your meal offer is successfully created.", Toast.LENGTH_SHORT).show()
                        binding.mealName.setText("")
                        binding.mealCategory.setText("")
                        binding.mealArea.setText("")
                        binding.mealIngredients.setText("")
                        binding.mealImage.setImageResource(R.drawable.ic_round_add_photo_alternate_24)
                    }
                }

                response.close()
            }

            override fun onFailure(call: Call, e: IOException) {
                e.printStackTrace()
            }
        })
    }
}