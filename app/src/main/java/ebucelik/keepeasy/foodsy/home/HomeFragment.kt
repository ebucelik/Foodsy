package ebucelik.keepeasy.foodsy.home

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import com.google.gson.GsonBuilder
import ebucelik.keepeasy.foodsy.R
import ebucelik.keepeasy.foodsy.adapter.OfferListAdapter
import ebucelik.keepeasy.foodsy.databinding.FragmentHomeBinding
import ebucelik.keepeasy.foodsy.entitiy.Offer
import ebucelik.keepeasy.foodsy.entitiy.OfferList
import okhttp3.*
import java.io.IOException

class HomeFragment(home: HomeActivity) : Fragment(R.layout.fragment_home){

    private lateinit var binding: FragmentHomeBinding
    private lateinit var offerList: OfferList
    private val homeActivity: HomeActivity = home

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentHomeBinding.bind(view)

        getAllOffers()

        binding.homeListView.setOnItemClickListener { parent, view, position, id ->
            val offer = parent.getItemAtPosition(position) as Offer
            try {
                homeActivity.openOfferDetailActivity(offer.mealName, "", offer.category, offer.area, "", "", "")
            }catch (e: ArrayIndexOutOfBoundsException){
                e.printStackTrace()
            }
        }

        binding.logout.setOnClickListener {
            setUUIDtoEmpty()
            homeActivity.openLoginActivity()
        }
    }

    private fun getAllOffers(){
        val url = "http://10.0.2.2:8080/offeringAll"

        val request = Request.Builder()
                .url(url)
                .get()
                .build()

        val client = OkHttpClient()
        client.newCall(request).enqueue(object: Callback{
            override fun onResponse(call: Call, response: Response) {
                val body = response.body?.string()

                if(response.code == 200){
                    val gson = GsonBuilder().create()
                    offerList = gson.fromJson(body, OfferList::class.java)

                    activity?.runOnUiThread {
                        binding.homeListView.adapter = OfferListAdapter(activity?.baseContext!!, offerList)
                    }
                }
            }

            override fun onFailure(call: Call, e: IOException) {
                println("Failure")
            }
        })
    }

    private fun setUUIDtoEmpty(){
        val sharedPref = activity?.getSharedPreferences("uuid", Context.MODE_PRIVATE)
        sharedPref
                ?.edit()
                ?.putString(R.string.uuid.toString(), "")
                ?.apply()
    }
}