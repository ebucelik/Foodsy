package ebucelik.keepeasy.foodsy.home

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ebucelik.keepeasy.foodsy.R
import ebucelik.keepeasy.foodsy.databinding.FragmentSellBinding

class SellFragment : Fragment(R.layout.fragment_sell) {

    private val REQUEST_SELECT_IMAGE_IN_ALBUM = 1
    private lateinit var binding: FragmentSellBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentSellBinding.bind(view)

        binding.mealImage.setOnClickListener {
            selectImageFromGallery()
        }
    }

    private fun selectImageFromGallery(){
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
        startActivityForResult(intent, REQUEST_SELECT_IMAGE_IN_ALBUM)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(resultCode == RESULT_OK && requestCode == REQUEST_SELECT_IMAGE_IN_ALBUM){
            val imageURI = data?.data
            binding.mealImage.setImageURI(imageURI)
        }
    }
}