package ebucelik.keepeasy.foodsy.home

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Base64
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.Toast
import androidx.core.graphics.drawable.toBitmap
import androidx.core.graphics.drawable.toDrawable
import ebucelik.keepeasy.foodsy.Globals.user
import ebucelik.keepeasy.foodsy.Globals.uuid
import ebucelik.keepeasy.foodsy.MainActivity
import ebucelik.keepeasy.foodsy.R
import ebucelik.keepeasy.foodsy.databinding.FragmentSellBinding
import ebucelik.keepeasy.foodsy.entitiy.Offer
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONException
import org.json.JSONObject
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.time.LocalDateTime
import java.util.*

class SellFragment() : Fragment(R.layout.fragment_sell) {

    private val REQUEST_SELECT_IMAGE_IN_ALBUM = 1
    private lateinit var binding: FragmentSellBinding
    private var imageUriList = mutableListOf("", "", "")
    private var imageEncodedList = mutableListOf("", "", "")

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentSellBinding.bind(view)

        binding.homeActivityViewModel = HomeActivity.homeActivityViewModel

        setEditTextValues()

        binding.firstMealImage.setOnClickListener {
            selectImageFromGallery()
        }
        binding.secMealImage.setOnClickListener {
            selectImageFromGallery()
        }
        binding.thirdMealImage.setOnClickListener {
            selectImageFromGallery()
        }

        binding.offerMealBtn.setOnClickListener {
            if(checkMealName() && checkMealCategory() && checkMealArea() && checkMealPrice()){
                createOffer()
            }
        }

        setOnFocusChangeListener()
    }

    private fun setEditTextValues(){
        if(!HomeActivity.homeActivityViewModel.currentOffer.value?.encodedImage.isNullOrEmpty()){
            binding.firstMealImage.setImageURI(Uri.parse(HomeActivity.homeActivityViewModel.currentOffer.value?.encodedImage))
            imageEncodedList[0] = HomeActivity.homeActivityViewModel.currentOffer.value?.encodedImage.toString()
        }

        if(!HomeActivity.homeActivityViewModel.currentOffer.value?.encodedImage1.isNullOrEmpty()){
            binding.secMealImage.setImageURI(Uri.parse(HomeActivity.homeActivityViewModel.currentOffer.value?.encodedImage1))
            imageEncodedList[1] = HomeActivity.homeActivityViewModel.currentOffer.value?.encodedImage1.toString()
        }

        if(!HomeActivity.homeActivityViewModel.currentOffer.value?.encodedImage2.isNullOrEmpty()){
            binding.thirdMealImage.setImageURI(Uri.parse(HomeActivity.homeActivityViewModel.currentOffer.value?.encodedImage2))
            imageEncodedList[2] = HomeActivity.homeActivityViewModel.currentOffer.value?.encodedImage2.toString()
        }
    }

    private fun setOnFocusChangeListener(){
        binding.mealName.setOnFocusChangeListener { view, focus ->
            if(!focus){
                setOffer()
            }
        }

        binding.mealCategory.setOnFocusChangeListener { view, focus ->
            if(!focus){
                setOffer()
            }
        }

        binding.mealArea.setOnFocusChangeListener { view, focus ->
            if(!focus){
                setOffer()
            }
        }

        binding.mealIngredients.setOnFocusChangeListener { view, focus ->
            if(!focus){
                setOffer()
            }
        }

        binding.mealPrice.setOnFocusChangeListener { view, focus ->
            if(!focus){
                //TODO: Error will be invoke. This function executes his-self after clicking on offer meal button.
                //setOffer()
            }
        }
    }

    private fun setOffer(){
        HomeActivity.homeActivityViewModel.currentOffer.value = Offer(1, binding.mealName.text.toString(), binding.mealCategory.text.toString(), binding.mealArea.text.toString(), imageUriList[0], imageUriList[1], imageUriList[2], binding.mealIngredients.text.toString(), Date(), binding.mealPrice.text.toString().toInt(), user)
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

    private fun checkMealPrice():Boolean{
        binding.apply {
            return if (mealPrice.text.toString().toInt() < 1 || mealPrice.text.toString().toInt() > 13){
                mealPriceLayout.isHelperTextEnabled = true
                mealPriceLayout.error = "Please enter a price between 1 and 13€."
                false
            }else{
                mealPriceLayout.error = null
                true
            }
        }
    }

    private fun selectImageFromGallery(){
        val intent = Intent(Intent.ACTION_GET_CONTENT, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
        intent.addCategory(Intent.CATEGORY_OPENABLE)
        intent.type = "image/*"
        startActivityForResult(intent, REQUEST_SELECT_IMAGE_IN_ALBUM)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        imageEncodedList = mutableListOf("", "", "")
        imageUriList = mutableListOf("", "", "")

        if(resultCode == RESULT_OK && requestCode == REQUEST_SELECT_IMAGE_IN_ALBUM){
            if(data?.clipData != null){
                val count = data.clipData!!.itemCount

                for (i in 0 until count){
                    val imageUri: Uri = data.clipData!!.getItemAt(i).uri

                    when(i){
                        0 -> {
                            binding.firstMealImage.setImageURI(imageUri)
                            imageUriList[0] = imageUri.toString()
                            imageEncodedList[0] = encodeImage(binding.firstMealImage.drawable.toBitmap())
                        }
                        1 -> {
                            binding.secMealImage.setImageURI(imageUri)
                            imageUriList[1] = imageUri.toString()
                            imageEncodedList[1] = encodeImage(binding.secMealImage.drawable.toBitmap())
                        }
                        2 -> {
                            binding.thirdMealImage.setImageURI(imageUri)
                            imageUriList[2] = imageUri.toString()
                            imageEncodedList[2] = encodeImage(binding.thirdMealImage.drawable.toBitmap())
                        }
                    }
                }
            }else{
                val imageURI = data?.data
                imageUriList[0] = imageURI.toString()
                binding.firstMealImage.setImageURI(imageURI)
                imageEncodedList[0] = encodeImage(binding.firstMealImage.drawable.toBitmap())
            }
        }
    }

    private fun encodeImage(bitmap: Bitmap):String{
        val byteArrayOutputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 50, byteArrayOutputStream);
        val byteArray = byteArrayOutputStream.toByteArray()
        return Base64.encodeToString(byteArray, Base64.DEFAULT)
    }

    private fun createOffer(){
        val url = "${MainActivity.IP}/offer"

        val jsonObject = JSONObject()

        try {
            jsonObject
                    .put("mealName", binding.mealName.text)
                    .put("category", binding.mealCategory.text)
                    .put("area", binding.mealArea.text)
                    .put("encodedImage", encodeImage(binding.firstMealImage.drawable.toBitmap()))
                    .put("encodedImage1", encodeImage(binding.secMealImage.drawable.toBitmap()))
                    .put("encodedImage2", encodeImage(binding.thirdMealImage.drawable.toBitmap()))
                    .put("ingredients", binding.mealIngredients.text)
                    .put("timestamp", LocalDateTime.now())
                    .put("price", binding.mealPrice.text.toString().toInt())
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
                        imageUriList = mutableListOf()
                        imageEncodedList = mutableListOf()
                        binding.mealName.setText("")
                        binding.mealCategory.setText("")
                        binding.mealArea.setText("")
                        binding.mealIngredients.setText("")
                        binding.firstMealImage.setImageResource(R.drawable.ic_round_add_photo_alternate_24)
                        binding.secMealImage.setImageResource(R.drawable.ic_round_add_photo_alternate_24)
                        binding.thirdMealImage.setImageResource(R.drawable.ic_round_add_photo_alternate_24)
                        binding.mealPrice.setText("0")
                        HomeActivity.homeActivityViewModel.currentOffer.value = null
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