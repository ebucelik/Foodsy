package ebucelik.keepeasy.foodsy.home

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.View
import com.google.gson.GsonBuilder
import ebucelik.keepeasy.foodsy.MainActivity
import ebucelik.keepeasy.foodsy.R
import ebucelik.keepeasy.foodsy.adapter.OfferListAdapter
import ebucelik.keepeasy.foodsy.databinding.FragmentSearchBinding
import ebucelik.keepeasy.foodsy.entitiy.Offer
import ebucelik.keepeasy.foodsy.entitiy.OfferList
import okhttp3.*
import java.io.IOException
import java.lang.Exception
import java.lang.NullPointerException
import java.text.SimpleDateFormat

class SearchFragment(val home: HomeActivity) : Fragment(R.layout.fragment_search) {

    private lateinit var offerList: OfferList
    private lateinit var binding: FragmentSearchBinding
    private val homeActivity: HomeActivity = home

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentSearchBinding.bind(view)

        binding.searchTxt.addTextChangedListener(object: TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                //Don't need yet
            }

            override fun onTextChanged(meal: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if(meal != null && meal.isNotEmpty()){
                    fetchJson(meal.toString())
                }else{
                    binding.searchedMealsList.adapter = null
                }
            }

            override fun afterTextChanged(p0: Editable?) {
                //Don't need yet
            }
        })

        binding.searchedMealsList.setOnItemClickListener { parent, view, position, id ->
            val offer = parent.getItemAtPosition(position) as Offer
            try {
                homeActivity.openOfferDetailActivity(offer)
            }catch (e: ArrayIndexOutOfBoundsException){
                e.printStackTrace()
            }
        }
    }

    private fun fetchJson(meal: String){
        val url = "http://${MainActivity.IP}:8080/offeringSearch?mealName=$meal"

        val request = Request.Builder()
                .url(url)
                .get()
                .build()

        val client = OkHttpClient()
        client.newCall(request).enqueue(object: Callback {
            override fun onResponse(call: Call, response: Response){
                val body = response.body?.string()

                val gson = GsonBuilder().create()
                try {
                    if(body.isNullOrEmpty())
                        throw NullPointerException("Don't found any meals.")

                    offerList = gson.fromJson(body, OfferList::class.java)

                    if(offerList.offerList.isNullOrEmpty())
                        throw NullPointerException("Don't found any meals.")

                    activity?.runOnUiThread {
                        binding.searchedMealsList.adapter = OfferListAdapter(activity?.baseContext!!, offerList)
                    }
                }catch (e: Exception){
                    e.printStackTrace()
                }
            }

            override fun onFailure(call: Call, e: IOException) {
                println("Failure")
            }
        })
    }
}