package ebucelik.keepeasy.foodsy.home

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import com.google.gson.GsonBuilder
import ebucelik.keepeasy.foodsy.Globals.user
import ebucelik.keepeasy.foodsy.MainActivity
import ebucelik.keepeasy.foodsy.R
import ebucelik.keepeasy.foodsy.adapter.OfferListAdapter
import ebucelik.keepeasy.foodsy.databinding.FragmentHomeBinding
import ebucelik.keepeasy.foodsy.entitiy.Offer
import ebucelik.keepeasy.foodsy.entitiy.OfferList
import ebucelik.keepeasy.foodsy.entitiy.User
import okhttp3.*
import java.io.IOException
import java.text.SimpleDateFormat

class HomeFragment() : Fragment(R.layout.fragment_home){

    private lateinit var binding: FragmentHomeBinding
    private lateinit var offerList: OfferList

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentHomeBinding.bind(view)

        getAllOffers()

        binding.homeListView.setOnItemClickListener { parent, view, position, id ->
            val offer = parent.getItemAtPosition(position) as Offer
            try {
                (activity as HomeActivity).openOfferDetailActivity(offer)
            }catch (e: ArrayIndexOutOfBoundsException){
                e.printStackTrace()
            }
        }

        binding.logout.setOnClickListener {
            setUUIDtoEmpty()
            user = User()
            (activity as HomeActivity).openLoginActivity()
        }
    }

    private fun getAllOffers(){
        val url = "${MainActivity.IP}/offer"

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

                response.close()
            }

            override fun onFailure(call: Call, e: IOException) {
                e.printStackTrace()
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